package org.java;

import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;

@Log4j2
public class Main {
    private static int storageSize;
    private static int producerCount;
    private static int producerTime;
    private static int consumerCount;
    private static int consumerTime;
    private static final String PROPERTIES_FILE = "application.properties";
    private static final String STORAGE_SIZE = "storageSize";
    private static final String PRODUCER_COUNT = "producerCount";
    private static final String PRODUCER_TIME = "producerTime";
    private static final String CONSUMER_COUNT = "consumerCount";
    private static final String CONSUMER_TIME = "consumerTime";
    private static final String ERROR_PROPERTY = "must be greater than 0";

    public static void main(String[] args) {
        parseProperties();
        start();
    }

    public static void start() {
        ArrayList<Producer> producers = new ArrayList<>(producerCount);
        ArrayList<Consumer> consumers = new ArrayList<>(consumerCount);
        Storage storage = new Storage(storageSize);
        for (int i = 0; i < producerCount; i++) {
            producers.add(new Producer(storage, producerTime, i + 1));
        }
        for (int i = 0; i < consumerCount; i++) {
            consumers.add(new Consumer(storage, consumerTime, i + 1));
        }

        producers.forEach(Producer::start);
        consumers.forEach(Consumer::start);
    }

    private static void parseProperties() {
        Properties properties = new Properties();
        ClassLoader loader = Thread.currentThread().getContextClassLoader();

        String key = null;
        try (InputStream stream = loader.getResourceAsStream(PROPERTIES_FILE)) {
            if (stream == null) {
                log.error(() -> "Cannot find the following .properties file: " + PROPERTIES_FILE);
                System.exit(0);
            }
            properties.load(stream);
            key = STORAGE_SIZE;
            storageSize = Integer.parseInt(properties.getProperty(key));
            key = PRODUCER_COUNT;
            producerCount = Integer.parseInt(properties.getProperty(key));
            key = PRODUCER_TIME;
            producerTime = Integer.parseInt(properties.getProperty(key));
            key = CONSUMER_COUNT;
            consumerCount = Integer.parseInt(properties.getProperty(key));
            key = CONSUMER_TIME;
            consumerTime = Integer.parseInt(properties.getProperty(key));

            validateValue(storageSize, STORAGE_SIZE);
            validateValue(producerCount, PRODUCER_COUNT);
            validateValue(consumerCount, CONSUMER_COUNT);
        } catch (IOException e) {
            log.error("", e);
            System.exit(0);
        } catch (NumberFormatException e) {
            String property = key;
            log.error(() -> "Exception occurred while loading property " + property, e);
            System.exit(0);
        }
        log.info(() -> "Storage size: " + storageSize + ", producers: " + producerCount + ", consumers: " + consumerCount);
        log.info(() -> "Producer time: " + producerTime + ", consumer time: " + consumerTime + " seconds");
    }

    private static void validateValue(int value, String errorMessage) {
        if (value < 1) {
            log.error(errorMessage + " " + ERROR_PROPERTY);
            System.exit(0);
        }
    }
}