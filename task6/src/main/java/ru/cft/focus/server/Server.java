package ru.cft.focus.server;

import lombok.extern.log4j.Log4j2;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.Properties;

@Log4j2
public class Server {
    public static void main(String[] args) {
        int port = 0;
        ClassLoader loader = ClassLoader.getSystemClassLoader();
        try (InputStream stream = loader.getResourceAsStream("server.properties")) {
            Properties properties = new Properties();
            properties.load(stream);
            port = Integer.parseInt(properties.getProperty("server.port"));
        } catch (IOException e) {
            log.error("", e);
        }

        try (ServerSocket socket = new ServerSocket(port)) {
            log.info("The server is running and waiting for connections...");

            while (true) {
                Socket clientSocket = socket.accept();
                log.info("Connection established: " + clientSocket.getInetAddress().getHostAddress());

                ClientHandler clientHandler = new ClientHandler(clientSocket);
                new Thread(clientHandler).start();
            }
        } catch (IOException e) {
            log.warn(e.getMessage());
        }
    }

}
