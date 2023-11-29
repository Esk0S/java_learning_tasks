package ru.cft.focus;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import ru.cft.focus.exception.ClientNotConnectedException;

import java.io.*;
import java.lang.reflect.Type;
import java.net.ConnectException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Client {
    private boolean connected;
    private Socket socket;
    private BufferedReader input;
    private PrintWriter output;
    private String host;
    private int port;
    private final String address;
    private final String username;
    private final List<ParticipantsListener> participantsListeners;
    private final List<ErrorListener> errorListeners;
    private final List<ConnectionListener> connectionListeners;
    private final List<MessageListener> messageListeners;
    private final List<InfoListener> infoListeners;
    private static final String USER_LOGGED_IN = "User with this name is already logged in";
    private static final String CANNON_CONNECT_TO_SERVER_ERR = "Cannot connect to server";
    private static final String CONNECTION_ERR = "Connection error";
    private static final String SERV_NOT_RESPONDING_ERR = "The server is not responding";
    private static final String INVALID_PORT_ERR = "Invalid port";
    private static final String INVALID_ADDRESS_ERR = "Invalid address";
    private static final org.apache.logging.log4j.Logger log
            = org.apache.logging.log4j.LogManager.getLogger(Client.class);

    public Client(String username, String address) {
        this.address = address;
        this.username = username;

        participantsListeners = new ArrayList<>();
        errorListeners = new ArrayList<>();
        connectionListeners = new ArrayList<>();
        messageListeners = new ArrayList<>();
        infoListeners = new ArrayList<>();
    }

    public void sendMessageToServer(String messageText) throws ClientNotConnectedException {
        if (!connected) {
            throw new ClientNotConnectedException("Client is not connected to server");
        }

        Message message = new Message(username, messageText, new Date());
        Gson gson = new Gson();
        String messageJson = gson.toJson(new JsonStructure("message", message));
        output.println(messageJson);
    }

    private boolean tryParseAddress() {
        String[] hostPort = address.split(":");
        if (hostPort.length != 2) {
            log.warn(INVALID_ADDRESS_ERR);
            notifyInfoListeners(INVALID_ADDRESS_ERR);
            return false;
        }
        try {
            port = Integer.parseInt(hostPort[1]);
        } catch (NumberFormatException e) {
            log.warn(INVALID_PORT_ERR, e);
            notifyInfoListeners(INVALID_PORT_ERR);
            return false;
        }
        host = hostPort[0];

        return true;
    }

    public void connectToServer() {
        if (connected || !tryParseAddress()) {
            return;
        }
        try {
            socket = new Socket(host, port);
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new PrintWriter(socket.getOutputStream(), true);

            Message message = new Message(username, "", new Date());

            String connectionMessage = new Gson().toJson(new JsonStructure("connection", message));
            output.println(connectionMessage);

            startReadingMessages();
        } catch (ConnectException e) {
            notifyErrorListeners(CANNON_CONNECT_TO_SERVER_ERR);
            log.error(CANNON_CONNECT_TO_SERVER_ERR, e);

        } catch (IOException e) {
            connected = false;
            notifyErrorListeners(CONNECTION_ERR);
            log.error(CONNECTION_ERR, e);
        }
    }

    private void startReadingMessages() {
        new Thread(() -> {
            try {
                String inputLine;
                boolean continueLoop = true;
                while (continueLoop && (inputLine = input.readLine()) != null) {
                    Gson gson = new Gson();
                    JsonStructure json = gson.fromJson(inputLine, JsonStructure.class);
                    String type = json.type();
                    String serverResponseString = "";
                    switch (type) {
                        case "connection" -> {
                            Message message = gson.fromJson(gson.toJson(json.content()), Message.class);
                            serverResponseString = message.formatConnection();
                            notifyConnected();
                            notifyMessageListeners(serverResponseString);
                            connected = true;
                        }
                        case "disconnection" -> {
                            Message message = gson.fromJson(gson.toJson(json.content()), Message.class);
                            serverResponseString = message.formatDisconnection();
                            notifyMessageListeners(serverResponseString);
                            connected = false;
                        }
                        case "message" -> {
                            Message message = gson.fromJson(gson.toJson(json.content()), Message.class);
                            serverResponseString = message.formatMessage();
                            notifyMessageListeners(serverResponseString);
                        }
                        case "participants" -> {
                            Type listType = new TypeToken<ArrayList<String>>() {
                            }.getType();
                            ArrayList<String> participants = gson.fromJson(gson.toJson(json.content()), listType);
                            serverResponseString = participants.toString();
                            notifyParticipantsListeners(participants);
                        }
                        case "error" -> {
                            if (json.content().equals(USER_LOGGED_IN)) {
                                serverResponseString = USER_LOGGED_IN;
                                continueLoop = false;
                                notifyInfoListeners(USER_LOGGED_IN);
                            }
                        }
                    }

                    String finalServerResponseString = serverResponseString;
                    log.info(() -> "Message from server: " + finalServerResponseString);
                }
            } catch (IOException e) {
                notifyErrorListeners(SERV_NOT_RESPONDING_ERR);
                log.error(SERV_NOT_RESPONDING_ERR, e);
            } finally {
                closeConnection();
            }
        }).start();
    }

    private void closeConnection() {
        try {
            if (input != null) {
                input.close();
            }
            if (output != null) {
                output.close();
            }
            if (socket != null && !socket.isClosed()) {
                socket.close();
                log.debug("Socket closed");
            }
        } catch (IOException e) {
            log.error("Error while closing connection", e);
        }
        connected = false;
    }

    public void addParticipantsListener(ParticipantsListener listener) {
        if (!participantsListeners.contains(listener)) {
            participantsListeners.add(listener);
        }
    }

    public void addErrorListener(ErrorListener listener) {
        if (!errorListeners.contains(listener)) {
            errorListeners.add(listener);
        }
    }

    public void addConnectionListener(ConnectionListener listener) {
        if (!connectionListeners.contains(listener)) {
            connectionListeners.add(listener);
        }
    }

    public void addMessageListener(MessageListener listener) {
        if (!messageListeners.contains(listener)) {
            messageListeners.add(listener);
        }
    }

    public void addInfoListener(InfoListener listener) {
        if (!infoListeners.contains(listener)) {
            infoListeners.add(listener);
        }
    }

    private void notifyParticipantsListeners(List<String> participants) {
        for (var listener : participantsListeners) {
            listener.updateParticipants(participants);
        }
    }

    private void notifyErrorListeners(String error) {
        for (var listener : errorListeners) {
            listener.onError(error);
        }
    }

    private void notifyConnected() {
        for (var listener : connectionListeners) {
            listener.onConnected();
        }
    }

    private void notifyMessageListeners(String message) {
        for (var listener : messageListeners) {
            listener.onReceivedMessage(message);
        }
    }

    private void notifyInfoListeners(String message) {
        for (var listener : infoListeners) {
            listener.onInfo(message);
        }
    }

}
