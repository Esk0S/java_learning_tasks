package ru.cft.focus.miner.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import ru.cft.focus.miner.model.GameType;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Log4j2
public class Records {
    @JsonIgnore
    private final ObjectMapper objectMapper = new ObjectMapper()
            .enable(SerializationFeature.INDENT_OUTPUT);
    @JsonIgnore
    private final File file = new File("records.json");

    public record Entry(@JsonProperty String name, @JsonProperty int time) {
    }

    @JsonProperty
    @Getter
    private Map<GameType, List<Entry>> recordsMap;

    public Records() {
        recordsMap = new EnumMap<>(GameType.class);
        loadRecords();
        createInitialFileIfNeeded();
    }

    private void loadRecords() {
        try {
            recordsMap = objectMapper.readValue(file, new TypeReference<>() {
            });
        } catch (IOException e) {
            log.error("Failed to get records", e);
        }
    }

    private void saveRecords() {
        try {
            objectMapper.writeValue(file, recordsMap);
        } catch (IOException e) {
            log.error("Failed to save records", e);
        }
    }

    private void createInitialFileIfNeeded() {
        if (!file.exists()) {
            for (GameType gameType : GameType.values()) {
                recordsMap.put(gameType, List.of(new Entry("Unknown", 999)));
            }
            saveRecords();
        }
    }

    public int readRecord(GameType gameType) {
        int recordTime = 0;
        if (!file.exists()) {
            createInitialFileIfNeeded();
        }
        loadRecords();

        List<Entry> typeRecords = recordsMap.getOrDefault(gameType, null);
        if (typeRecords != null && !typeRecords.isEmpty()) {
            Entry entry = typeRecords.get(0);
            recordTime = entry.time();
        }

        return recordTime;
    }

    public void registerScore(GameType gameType, String name, int newScore) {
        List<Entry> typeRecords = recordsMap.computeIfAbsent(gameType, t -> new LinkedList<>());

        if (typeRecords.isEmpty()) {
            typeRecords.add(new Entry(name, newScore));
        } else {
            Entry entry = typeRecords.get(0);
            if (entry.time() <= newScore) {
                throw new IllegalArgumentException("Current score better than supplied");
            }
            typeRecords.remove(0);
            typeRecords.add(new Entry(name, newScore));
        }
        saveRecords();
    }

}
