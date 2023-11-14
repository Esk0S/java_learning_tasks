package org.java;

import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Log4j2
public class Main {
    private static int storageSize;
    private static int producerCount;
    private static int producerTime;
    private static int consumerCount;
    private static int consumerTime;

    public static void main(String[] args) {
        parseProperties();

        Storage storage = new Storage(storageSize);
        for (int i = 0; i < producerCount; i++) {
            new Producer(storage, producerTime, i + 1).start();
        }
        for (int i = 0; i < consumerCount; i++) {
            new Consumer(storage, consumerTime, i + 1).start();
        }
    }

    private static void parseProperties() {
        Properties properties = new Properties();
        ClassLoader loader = Thread.currentThread().getContextClassLoader();

        String key = null;
        try (InputStream stream = loader.getResourceAsStream("application.properties")) {
            properties.load(stream);
            key = "storageSize";
            storageSize = Integer.parseInt(properties.getProperty(key));
            key = "producerCount";
            producerCount = Integer.parseInt(properties.getProperty(key));
            key = "producerTime";
            producerTime = Integer.parseInt(properties.getProperty(key));
            key = "consumerCount";
            consumerCount = Integer.parseInt(properties.getProperty(key));
            key = "consumerTime";
            consumerTime = Integer.parseInt(properties.getProperty(key));
        } catch (NullPointerException | IOException e) {
            log.error(e.getMessage());
            System.exit(0);
        } catch (NumberFormatException e) {
            String property = key;
            log.error(() -> "Exception occurred while loading property " + property, e);
            System.exit(0);
        }
        log.info("Storage size: " + storageSize + ", producers: " + producerCount + ", consumers: " + consumerCount);
        log.info("Producer time: " + producerTime + ", consumer time: " + consumerTime + " seconds");
    }
}