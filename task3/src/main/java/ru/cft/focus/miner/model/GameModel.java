package ru.cft.focus.miner.model;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import lombok.extern.log4j.Log4j2;
import ru.cft.focus.miner.data.GameField;
import ru.cft.focus.miner.view.*;

import java.io.*;
import java.util.*;

import static ru.cft.focus.miner.model.ActionType.MARK_UNMARK;
import static ru.cft.focus.miner.model.ActionType.OPEN_CELLS;

@Log4j2
public class GameModel {
    private boolean start;
    private Random rand;
    private JsonObject json;
    private final GameField gameField;
    private final List<CellListener> cellListeners;
    private final List<NewGameListener> newGameListeners;
    private final List<GameWonListener> gameWonListeners;
    private final List<GameLostListener> gameLostListeners;
    private final List<RecordListener> recordListeners;
    private final List<HighScoresWindowListener> highScoresWindowListeners;
    private static final int X = 0;
    private static final int Y = 1;
    private static final String JSON_FILE_NAME = "records.json";

    public GameModel(GameField gameFiled) {
        this.gameField = gameFiled;
        this.cellListeners = new ArrayList<>();
        this.newGameListeners = new ArrayList<>();
        this.gameWonListeners = new ArrayList<>();
        this.gameLostListeners = new ArrayList<>();
        this.recordListeners = new ArrayList<>();
        this.highScoresWindowListeners = new ArrayList<>();
        start = true;
        rand = new Random();
    }

    public void startNewGame(int bombs, int rows, int cols, GameType gameType) {
        gameField.initialize(bombs, rows, cols, gameType);
        start = true;
        rand = new Random();
    }

    public void notifyCellListeners(CellEvent event) {
        for (CellListener listener : cellListeners) {
            listener.onCellAction(event);
        }
    }

    public void notifyNewGameListeners(NewGameEvent event) {
        for (NewGameListener listener : newGameListeners) {
            listener.onNewGame(event);
        }
    }

    public void notifyGameWonListeners() {
        for (GameWonListener listener : gameWonListeners) {
            listener.onGameWon();
        }
    }

    public void notifyGameLostListeners() {
        for (GameLostListener listener : gameLostListeners) {
            listener.onGameLost();
        }
    }

    public void notifyRecordListeners() {
        for (RecordListener listener : recordListeners) {
            listener.onRecord();
        }
    }

    public void notifyHighScoresWindowListeners() {
        for (HighScoresWindowListener listener : highScoresWindowListeners) {
            listener.updateHighScoresWindow();
        }
    }

    public void setBombs(int currentPosX, int currentPosY) {
        int bombsCount = gameField.getBombsCount();
        int rowsCount = gameField.getRowsCount();
        int colsCount = gameField.getColsCount();
        for (int i = 0; i < bombsCount; i++) {
            int bombPosX;
            int bombPosY;
            do {
                bombPosX = rand.nextInt(colsCount);
                bombPosY = rand.nextInt(rowsCount);
            } while (gameField.checkBombOnField(bombPosX, bombPosY) || (bombPosX == currentPosX && bombPosY == currentPosY));
            gameField.setBombOnField(bombPosX, bombPosY);
            log.debug("Bomb on " + bombPosX + " " + bombPosY);
//            mainWindow.setCellImage(bombPosX, bombPosY, GameImage.BOMB); // cheat
        }
    }

    private int checkCellsForBombs(List<int[]> cells) {
        int bombCount = 0;
        for (var i : cells) {
            if (gameField.checkBombOnField(i[X], i[Y])) {
                bombCount++;
            }
        }
        return bombCount;
    }

    public void addNewGameListener(View listener) {
        if (!newGameListeners.contains(listener)) {
            newGameListeners.add(listener);
        }
    }

    public void addCellListener(View listener) {
        if (!cellListeners.contains(listener)) {
            cellListeners.add(listener);
        }
    }

    public void addWinListener(View listener) {
        if (!gameWonListeners.contains(listener)) {
            gameWonListeners.add(listener);
        }
    }

    public void addLoseListener(View listener) {
        if (!gameLostListeners.contains(listener)) {
            gameLostListeners.add(listener);
        }
    }

    public void addRecordListener(View listener) {
        if (!recordListeners.contains(listener)) {
            recordListeners.add(listener);
        }
    }

    public void addHighRecordListener(View listener) {
        if (!highScoresWindowListeners.contains(listener)) {
            highScoresWindowListeners.add(listener);
        }
    }

    public int readRecord(GameType gameType) {
        File records = new File(JSON_FILE_NAME);
        if (!records.isFile()) {
            createRecords();
        }

        return getRecord(gameType);
    }

    private void createRecords() {
        JsonObject noviceObject = new JsonObject();
        noviceObject.addProperty("Unknown", 999);

        JsonObject mediumObject = new JsonObject();
        mediumObject.addProperty("Unknown", 999);

        JsonObject expertObject = new JsonObject();
        expertObject.addProperty("Unknown", 999);

        JsonObject mainObject = new JsonObject();
        mainObject.add("NOVICE", noviceObject);
        mainObject.add("MEDIUM", mediumObject);
        mainObject.add("EXPERT", expertObject);
        try (JsonWriter writer = new JsonWriter(new FileWriter(JSON_FILE_NAME))) {
            Gson gson = new Gson();
            gson.toJson(mainObject, writer);
        } catch (IOException e) {
            log.warn(e.getMessage());
        }
    }

    private int getRecord(GameType gameType) {
        Map<String, Map<String, Integer>> records = parseRecordsFromFile();

        return records.get(gameType.name()).values().iterator().next();
    }

    public void updateHighScoresFile(String name, int recordValue, GameType gameType) {
        String oldName = null;
        JsonObject jsonRecordHolder = json.getAsJsonObject(gameType.name());
        for (var i : jsonRecordHolder.asMap().entrySet()) {
            oldName = i.getKey();
        }
        jsonRecordHolder.remove(oldName);
        jsonRecordHolder.addProperty(name, recordValue);

        try (JsonWriter writer = new JsonWriter(new FileWriter(JSON_FILE_NAME))) {
            Gson gson = new Gson();
            gson.toJson(json, writer);
        } catch (IOException e) {
            log.warn(e.getMessage());
        }
    }

    public Map<String, Map<String, Integer>> parseRecordsFromFile() {
        Map<String, Map<String, Integer>> records = new HashMap<>();
        try (JsonReader reader = new JsonReader(new FileReader(JSON_FILE_NAME))) {
            json = JsonParser.parseReader(reader).getAsJsonObject();

            for (var gameTypeJson : json.entrySet()) {
                String gameType = gameTypeJson.getKey();
                Map<String, Integer> innerMap = new HashMap<>();
                for (var nameJson : gameTypeJson.getValue().getAsJsonObject().entrySet()) {
                    String name = nameJson.getKey();
                    int result = nameJson.getValue().getAsInt();
                    innerMap.put(name, result);
                }
                records.put(gameType, innerMap);
            }
        } catch (IOException e) {
            log.warn(e.getMessage());
        }

        return records;
    }

    private void checkForWonLost(List<CellEvent> cells) {
        boolean bomb = false;
        for (var cellEvent : cells) {
            if (cellEvent.getCell().hasBomb()) {
                bomb = true;
            }
            notifyCellListeners(cellEvent);
        }
        if (bomb) {
            notifyGameLostListeners();
        }
        if (gameField.getOpenedCellsCount() == gameField.getNeedToOpenCellsToWin()) {
            notifyGameWonListeners();
        }
    }

    public void openCells(int x, int y) {
        List<CellEvent> cells = generateOpenedCells(x, y);
        checkForWonLost(cells);
    }

    public void openCellsAround(int x, int y) {
        List<CellEvent> cells = generateCellsAround(x, y);
        checkForWonLost(cells);
    }

    private List<CellEvent> generateOpenedCells(int x, int y) {
        Cell cell = gameField.getCell(x, y);
        if (cell.isMarked()) {
            return List.of();
        }
        List<CellEvent> cells = new ArrayList<>();
        if (start) {
            setBombs(x, y);
            generateNumberOfBombsAroundCells();
            start = false;
        }

        if (cell.isMarked()) {
            cell.setMarked(false);
        }
        if (cell.isEmpty()) {
            openEmptyCells(x, y, cells);
        } else {
            gameField.incOpenedCellsCountIfNotOpened(x, y);
            cell.setOpened();
            cells.add(new CellEvent(x, y, cell, OPEN_CELLS));
        }
        log.debug("Number of open cells: " + gameField.getOpenedCellsCount());

        return cells;
    }

    private void openEmptyCells(int x, int y, List<CellEvent> cells) {
        Cell cell = gameField.getCell(x, y);
        gameField.incOpenedCellsCountIfNotOpened(x, y);
        log.debug("Number of open cells: " + gameField.getOpenedCellsCount());
        cell.setOpened();
        if (cell.isMarked()) {
            cell.setMarked(false);
        }

        cells.add(new CellEvent(x, y, cell, OPEN_CELLS));

        if (cell.getBombsAround() > 0) {
            cells.add(new CellEvent(x, y, cell, OPEN_CELLS));
        }

        if (cell.isEmpty()) {
            for (var coords : getNeighboringCellIndices(x, y)) {
                var neighbor = gameField.getCell(coords[X], coords[Y]);
                if (!neighbor.isOpened() && !neighbor.isMarked()) {
                    openEmptyCells(coords[X], coords[Y], cells);
                }
            }
        }
    }

    private void generateNumberOfBombsAroundCells() {
        for (int x = 0; x < gameField.getColsCount(); x++) {
            for (int y = 0; y < gameField.getRowsCount(); y++) {
                int bombsAround = 0;
                for (var neighbor : getNeighboringCellIndices(x, y)) {
                    int neighborX = neighbor[X];
                    int neighborY = neighbor[Y];
                    Cell cell = gameField.getCell(neighborX, neighborY);
                    if (cell.hasBomb()) {
                        bombsAround++;
                    }
                }
                Cell currentCell = gameField.getCell(x, y);
                currentCell.setBombsAround(bombsAround);
            }
        }
    }

    private List<int[]> getNeighboringCellIndices(int x, int y) {
        List<int[]> neighboringCellIndices = new ArrayList<>();

        if (y > 0) {
            neighboringCellIndices.addAll(getTopOrBottomCellIndices(x, y, true));
        }
        if (y < gameField.getRowsCount() - 1) {
            neighboringCellIndices.addAll(getTopOrBottomCellIndices(x, y, false));
        }
        neighboringCellIndices.addAll(getLeftAndRightCellIndices(x, y));

        return neighboringCellIndices;
    }

    private List<int[]> getTopOrBottomCellIndices(int x, int y, boolean topCells) {
        List<int[]> cellIndices = new ArrayList<>();

        if (topCells) {
            y--;
        } else {
            y++;
        }

        int lowerBound = 0;
        int upperBound = gameField.getColsCount() - 1;

        if (x > 0) {
            lowerBound = x - 1;
        }
        if (x < gameField.getColsCount() - 1) {
            upperBound = x + 1;
        }

        for (int i = lowerBound; i <= upperBound; i++) {
            cellIndices.add(new int[]{i, y});
        }

        return cellIndices;
    }

    private List<int[]> getLeftAndRightCellIndices(int x, int y) {
        List<int[]> cellIndices = new ArrayList<>();

        if (x > 0) {
            cellIndices.add(new int[]{x - 1, y});
        }
        if (x < gameField.getColsCount() - 1) {
            cellIndices.add(new int[]{x + 1, y});
        }

        return cellIndices;
    }

    public void markCell(int x, int y) {
        Cell cell = gameField.getCell(x, y);

        if (cell.isOpened()) {
            return;
        }

        if (cell.isMarked()) {
            cell.setMarked(false);
            gameField.decreaseMarkCount();
        } else if (gameField.getMarkCount() < gameField.getBombsCount()) {
            cell.setMarked(true);
            gameField.incMarkCount();
        }

        notifyCellListeners(new CellEvent(x, y, cell, MARK_UNMARK));
    }

    public List<CellEvent> generateCellsAround(int x, int y) {
        Cell cell = gameField.getCell(x, y);
        if (!cell.isOpened()) {
            return List.of();
        }
        List<CellEvent> cellEvents = new ArrayList<>();

        var neighboringCellIndices = getNeighboringCellIndices(x, y);
        int bombsAround = checkCellsForBombs(neighboringCellIndices);
        int bombCount = 0;
        for (var coords : neighboringCellIndices) {
            var neighbor = gameField.getCell(coords[X], coords[Y]);
            if (neighbor.isMarked()) {
                bombCount++;
            }
        }
        if (bombCount != bombsAround) {
            return List.of();
        }
        for (var coords : neighboringCellIndices) {
            var neighbor = gameField.getCell(coords[X], coords[Y]);
            if (!neighbor.isOpened() && !neighbor.isMarked()) {
                cellEvents.addAll(generateOpenedCells(coords[X], coords[Y]));
            }
        }

        return cellEvents;
    }

}
