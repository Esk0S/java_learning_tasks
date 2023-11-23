package ru.cft.focus.server;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import lombok.extern.log4j.Log4j2;
import ru.cft.focus.common.Message;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

@Log4j2
public class ClientHandler implements Runnable {
    private PrintWriter output;
    private final Socket socket;
    private static final ArrayList<ClientHandler> clientSockets = new ArrayList<>();
    private static final ArrayList<String> clientNames = new ArrayList<>();

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
                Message message = gson.fromJson(inputLine, Message.class);
                String formattedMessage = message.format();
                if (message.connect()) {
                    clientNames.add(message.senderName());
                }
                JsonObject json = new JsonObject();
                json.addProperty("type", "message");
                json.addProperty("content", gson.toJson(clientNames));
                json.toString();

                log.info(() -> "Message from client: " + formattedMessage);
                for (var client : clientSockets) {
                    if (message.connect()) {
                        JsonObject json = new JsonObject();
                        json.addProperty("type", "participants");
                        json.addProperty("content", gson.toJson(clientNames));
                        client.sendContentToClient(json.toString());
                    }
                    client.sendContentToClient(inputLine);
                }
//                // Вывод сообщения обратно клиенту
//                output.println("Server received your message: " + formattedMessage);
            }
        } catch (SocketException e) {
            // Обработка исключения при разрыве соединения
            if (e.getMessage().equals("Connection reset")) {
                log.warn("Connection reset by client");
            } else {
                log.warn("SocketException occurred", e);
            }
        } catch (IOException e) {
            log.error("Error initializing input stream", e);
        } finally {
            if (output != null) {
                output.close();
            }
        }
    }

//    private String formatTimeStamp(Date date) {
//        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
//        return "[" + sdf.format(date) + "]";
//    }

    private void sendContentToClient(String message) {
        output.println(message);
    }

//    private String formatMessage(Message message) {
//        if (message.connect()) {
//            return formatTimeStamp(message.date()) + " " + message.senderName() + " JOINED";
//        }
//        return formatTimeStamp(message.date()) + " " + message.senderName() + ": " + message.text();
//    }

}
