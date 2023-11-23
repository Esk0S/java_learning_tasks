package ru.cft.focus.server;

import lombok.extern.log4j.Log4j2;
import ru.cft.focus.common.Message;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Properties;

@Log4j2
public class Server {
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
