package ru.cft.focus.client;

import lombok.extern.log4j.Log4j2;

import java.io.*;
import java.net.Socket;
import java.util.Properties;

@Log4j2
public class Client {
    public static void main(String[] args) {
        int port = 0;
        String address = null;
        ClassLoader loader = ClassLoader.getSystemClassLoader();
        try (InputStream stream = loader.getResourceAsStream("server.properties")) {

            Properties properties = new Properties();
            properties.load(stream);
            port = Integer.parseInt(properties.getProperty("server.port"));
            address = properties.getProperty("server");

        } catch (IOException e) {
            log.error("", e);
        }

        try (Socket socket = new Socket(address, port)) {
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);

            output.println("Hello Server!");

            String response = input.readLine();
            log.info("Server response: " + response);
        } catch (IOException e) {
            log.warn(e.getMessage());
        }
    }

}
