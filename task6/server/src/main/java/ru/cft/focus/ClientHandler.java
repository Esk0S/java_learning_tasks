package ru.cft.focus;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Date;

public class ClientHandler implements Runnable {
    private PrintWriter output;
    private String currentClientName;
    private final Socket socket;
    private static final ArrayList<ClientHandler> clientSockets = new ArrayList<>();
    private static final ArrayList<String> clientNames = new ArrayList<>();
    private static final String MESSAGE_FROM_CLIENT_STRING = "Message from client: ";
    private static final org.apache.logging.log4j.Logger log
            = org.apache.logging.log4j.LogManager.getLogger(ClientHandler.class);

    public ClientHandler(Socket socket) {
        this.socket = socket;
        try {
            output = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            log.error("Error initializing output stream", e);
        }
        clientSockets.add(this);
    }

    @Override
    public void run() {
        try (
                BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        ) {
            String inputLine;
            while ((inputLine = input.readLine()) != null) {
                Gson gson = new Gson();
                JsonStructure json = gson.fromJson(inputLine, JsonStructure.class);
                String type = json.type();
                if (type.equals("connection")) {
                    Message message = gson.fromJson(gson.toJson(json.content()), Message.class);
                    if (clientNames.contains(message.senderName())) {
                        String jsonErrorResponse = gson.toJson(
                                new JsonStructure("error", "User with this name is already logged in"));
                        log.info(() -> MESSAGE_FROM_CLIENT_STRING + jsonErrorResponse);
                        sendContentToClient(jsonErrorResponse);
                        continue;
                    }
                    currentClientName = message.senderName();
                    clientNames.add(message.senderName());

                    String jsonParticipants = gson.toJson(new JsonStructure("participants", clientNames));
                    log.info(() -> MESSAGE_FROM_CLIENT_STRING + jsonParticipants);
                    sendContentToAllClients(jsonParticipants);
                }

                String finalInputLine = inputLine;
                log.info(() -> MESSAGE_FROM_CLIENT_STRING + finalInputLine);
                sendContentToAllClients(inputLine);
            }
        } catch (SocketException e) {
            if (e.getMessage().equals("Connection reset")) {
                log.warn("Connection reset by client");
            } else {
                log.warn("SocketException occurred", e);
            }
            clientNames.remove(currentClientName);
            clientSockets.remove(this);
            Message message = new Message(currentClientName, "", new Date());
            JsonStructure json = new JsonStructure("disconnection", message);
            Gson gson = new Gson();
            String jsonDisconnected = gson.toJson(json);
            String jsonParticipants = gson.toJson(new JsonStructure("participants", clientNames));
            log.warn(() -> MESSAGE_FROM_CLIENT_STRING + jsonParticipants);
            log.warn(() -> MESSAGE_FROM_CLIENT_STRING + jsonDisconnected);
            sendContentToAllClients(jsonParticipants);
            sendContentToAllClients(jsonDisconnected);
        } catch (IOException e) {
            log.error("Error initializing input stream", e);
        } finally {
            if (output != null) {
                output.close();
            }
        }
    }

    private void sendContentToAllClients(String content) {
        for (var clientSocket : clientSockets) {
            clientSocket.output.println(content);
        }
    }

    private void sendContentToClient(String content) {
        output.println(content);
    }

}
