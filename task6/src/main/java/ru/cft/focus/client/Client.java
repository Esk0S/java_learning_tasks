package ru.cft.focus.client;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import ru.cft.focus.client.exception.ClientNotConnectedException;
import ru.cft.focus.common.JsonStructure;
import ru.cft.focus.common.Message;

import java.io.*;
import java.net.ConnectException;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;

@Log4j2
public class Client {
    private int port;
    private boolean connected;
    private Socket socket;
    private BufferedReader input;
    private PrintWriter output;
    private final String address;
    private final String username;

    public Client(String username, String address) {
        this.address = address;
        this.username = username;
        parsePortFromFile();
    }

    public void sendMessageToServer(String messageText) throws ClientNotConnectedException {
        if (!connected) {
            throw new ClientNotConnectedException("Client is not connected to server");
        }

        Message message = new Message(username, messageText, new Date()/*, false*/);
        Gson gson = new Gson();
        String messageJson = gson.toJson(new JsonStructure("message", message));
//                buildJson("message", message);
        output.println(messageJson);
    }

    private void parsePortFromFile() {
        ClassLoader loader = ClassLoader.getSystemClassLoader();
        try (InputStream stream = loader.getResourceAsStream("server.properties")) {
            Properties properties = new Properties();
            properties.load(stream);
            port = Integer.parseInt(properties.getProperty("port"));
        } catch (IOException e) {
            log.error("", e);
        } catch (NumberFormatException e) {
            log.error("Invalid port", e);
        }
    }

    public void connectToServer() {
        if (connected) {
            return;
        }
        try {
            socket = new Socket(address, port);
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new PrintWriter(socket.getOutputStream(), true);
            connected = true;
            Message message = new Message(username, "", new Date()/*, true*/);
            ArrayList<String> participants = new ArrayList<>();
            String inputLine;
//            if ((inputLine = input.readLine()) != null) {
//
//            }

            String connectionMessage = new Gson().toJson(new JsonStructure("connection", message));
//                    buildJson("connection", message);
            output.println(connectionMessage);

            startReadingMessages();
        } catch (ConnectException e) {
            log.warn("Cannot connect to server: " + e.getMessage());
        } catch (IOException e) {
            connected = false;
            log.warn("Connection error", e);
        }
    }

    private void startReadingMessages() {
        new Thread(() -> {
            try {
                String inputLine;
                while ((inputLine = input.readLine()) != null) {
                    Gson gson = new Gson();
                    gson.fromJson(inputLine, JsonObject.class).getAsJsonObject("type");
//                    if () {
//
//                    }
                    Message message = new Gson().fromJson(inputLine, Message.class);

                    log.info(() -> "Message from server: " + message.formatMessage());
                    // Обработка сообщений от сервера здесь
                }
            } catch (IOException e) {
                log.warn("Error while reading messages", e);
            } finally {
                closeConnection(); // Закрытие соединения после чтения сообщений
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
            log.warn("Error while closing connection", e);
        }
        connected = false;
    }

//    private <T> String buildJson(String type, T/*Object*/ content) {
//        JsonObject json = new JsonObject();
//        json.addProperty("type", type);
//        json.addProperty("content", new Gson().toJson(content));
//        return json.toString();
//    }

    public boolean isConnected() {
        return connected;
    }

}
