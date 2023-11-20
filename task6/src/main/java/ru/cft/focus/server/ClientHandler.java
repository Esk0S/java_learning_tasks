package ru.cft.focus.server;

import lombok.extern.log4j.Log4j2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

@Log4j2
public class ClientHandler  implements Runnable {
    private final Socket socket;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }
    @Override
    public void run() {
        try (
                BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter output = new PrintWriter(socket.getOutputStream(), true)
        ) {
            String inputLine;
            while ((inputLine = input.readLine()) != null) {
                String finalInputLine = inputLine;
                log.info(() -> "Message from client: " + finalInputLine);
                // Вывод сообщения обратно клиенту
                output.println("Server received your message: " + inputLine);
            }
        } catch (IOException e) {
            log.warn("", e);
        }
    }
}
