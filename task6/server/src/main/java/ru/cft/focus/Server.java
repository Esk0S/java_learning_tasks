package ru.cft.focus;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;

public class Server {
    private static final Logger log = LogManager.getLogger(Server.class);

    public static void main(String[] args) {
        int port = parsePortFromFile();

        try (ServerSocket socket = new ServerSocket(port)) {
            log.info(() -> "The server is running on port " + port + " and waiting for connections...");

            while (true) {
                Socket clientSocket = socket.accept();
                log.info(() -> "Connection established: " + clientSocket.getInetAddress().getHostAddress());

                ClientHandler clientHandler = new ClientHandler(clientSocket);
                new Thread(clientHandler).start();
            }
        } catch (IOException e) {
            log.warn(e.getMessage());
        }
    }

    private static int parsePortFromFile() {
        int port = 0;
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

        return port;
    }

}
