package ru.cft.focus.miner.model;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import lombok.extern.log4j.Log4j2;
import ru.cft.focus.miner.data.GameValues;
import ru.cft.focus.miner.view.*;

import java.io.*;
import java.util.*;

import static ru.cft.focus.miner.model.ButtonTypeModel.LEFT_BUTTON;
import static ru.cft.focus.miner.model.ButtonTypeModel.RIGHT_BUTTON;
import static ru.cft.focus.miner.data.GameValues.*;
import static ru.cft.focus.miner.model.CellState.*;

@Log4j2
public class GameModel {
    private boolean start;
    private final List<CellListener> cellListeners;
    private final List<NewGameListener> newGameListeners;
    private final List<WinListener> winListeners;
    private final List<LoseListener> loseListeners;
    private final List<RecordListener> recordListeners;
    private Random rand;
    private JsonObject json;
    private static final int X = 0;
    private static final int Y = 1;
    private static final String JSON_FILE_NAME = "records.json";

    public GameModel() {
        this.cellListeners = new ArrayList<>();
        this.newGameListeners = new ArrayList<>();
        this.winListeners = new ArrayList<>();
        this.loseListeners = new ArrayList<>();
        this.recordListeners = new ArrayList<>();
        start = true;
        rand = new Random();
    }

    public NewGameEvent startNewGame(int bombs, int rows, int cols, GameType gameType) {
        GameValues.initialize(bombs, rows, cols, gameType);
        start = true;
        rand = new Random();

        return new NewGameEvent(bombs, rows, cols);
    }

    public void notifyCellListeners(CellEvent event) {
        for (CellListener listener : cellListeners) {
            listener.onClick(event);
        }
    }

    public void notifyNewGameListeners(NewGameEvent event) {
        for (NewGameListener listener : newGameListeners) {
            listener.onNewGame(event);
        }
    }

    public void notifyWinListeners(WinEvent event) {
        for (WinListener listener : winListeners) {
            listener.onWin(event);
        }
    }

    public void notifyLoseListeners(LoseEvent event) {
        for (LoseListener listener : loseListeners) {
            listener.onLose(event);
        }
    }

    public void notifyRecordListeners(RecordEvent event) {
        for (RecordListener listener : recordListeners) {
            listener.onRecord(event);
        }
    }

    public void setBombs(int currentPosX, int currentPosY) {
        int bombsCount = getBombsCount();
        int rowsCount = getRowsCount();
        int colsCount = getColsCount();
        for (int i = 0; i < bombsCount; i++) {
            int bombPosX;
            int bombPosY;
            do {
                bombPosX = rand.nextInt(colsCount);
                bombPosY = rand.nextInt(rowsCount);
            } while (checkBombOnField(bombPosX, bombPosY) || (bombPosX == currentPosX && bombPosY == currentPosY));
            setBombOnField(bombPosX, bombPosY);
            addBombPosition(bombPosX, bombPosY);
//            mainWindow.setCellImage(bombPosX, bombPosY, GameImage.BOMB); // cheat
        }
    }

    private void setCellStates() {
        for (int x = 0; x < getColsCount(); x++) {
            for (int y = 0; y < getRowsCount(); y++) {
                CellState cellState;
                if (checkBombOnField(x, y)) {
                    cellState = BOMB;
                } else {
                    int bombCount = checkCellsForBombs(getNeighboringCellIndices(x, y));
                    cellState = switch (bombCount) {
                        case 1 -> NUM_1;
                        case 2 -> NUM_2;
                        case 3 -> NUM_3;
                        case 4 -> NUM_4;
                        case 5 -> NUM_5;
                        case 6 -> NUM_6;
                        case 7 -> NUM_7;
                        case 8 -> NUM_8;

                        default -> EMPTY;
                    };
                }
                setCellState(x, y, cellState);
            }
        }
    }

    private int checkCellsForBombs(List<int[]> cells) {
        int bombCount = 0;
        for (var i : cells) {
            if (checkBombOnField(i[X], i[Y])) {
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
        if (!winListeners.contains(listener)) {
            winListeners.add(listener);
        }
    }

    public void addLoseListener(View listener) {
        if (!loseListeners.contains(listener)) {
            loseListeners.add(listener);
        }
    }

    public void addRecordListener(View listener) {
        if (!recordListeners.contains(listener)) {
            recordListeners.add(listener);
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
        noviceObject.addProperty("None", 999);

        JsonObject mediumObject = new JsonObject();
        mediumObject.addProperty("None", 999);

        JsonObject expertObject = new JsonObject();
        expertObject.addProperty("None", 999);

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

    public int getRecord(GameType gameType) {
        int recValue = -1;
        try (JsonReader reader = new JsonReader(new FileReader(JSON_FILE_NAME))) {
            json = JsonParser.parseReader(reader).getAsJsonObject();
            JsonObject jsonRecordHolder = json.getAsJsonObject(gameType.name());
            for (var i : jsonRecordHolder.asMap().entrySet()) {
                recValue = i.getValue().getAsInt();
            }
        } catch (IOException e) {
            log.warn(e.getMessage());
        }

        return recValue;
    }

    public void updateRecord(String name, int recordValue, GameType gameType) {
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

    public List<CellEvent> getOpenedCells(int x, int y) {
        if (isMarked(x, y)) {
            return List.of();
        }

        List<CellEvent> cells = generateCells(x, y);

        for (var cell : cells) {
            setCellOpened(cell.getX(), cell.getY());
        }

        return cells;
    }

    private List<CellEvent> generateCells(int x, int y) {
        if (isMarked(x, y)) {
            return List.of();
        }
        List<CellEvent> cells = new ArrayList<>();
        if (start) {
            setBombs(x, y);
            setCellStates();
            start = false;
        }

        setCellAsCanNotBeMarked(x, y);
        if (isMarked(x, y)) {
            setCellAsMarked(x, y, false);
        }
        CellState cellState = GameValues.getCellState(x, y);
        if (cellState == EMPTY) {
            openEmptyCells(x, y, cells);
        } else {
            cells.add(new CellEvent(x, y, cellState, LEFT_BUTTON));
            incOpenedCellsCountIfNotOpened(x, y);
        }
        log.debug("Number of open cells: " + getOpenedCellsCount());

        return cells;
    }

    private void openEmptyCells(int x, int y, List<CellEvent> cells) {
        setCellVisited(x, y, true);
        cells.add(new CellEvent(x, y, EMPTY, LEFT_BUTTON));

        setCellAsCanNotBeMarked(x, y);
        if (isMarked(x, y)) {
            setCellAsMarked(x, y, false);
        }
        CellState cellState = GameValues.getCellState(x, y);
        if (cellState != EMPTY && cellState != BOMB) {
            cells.add(new CellEvent(x, y, cellState, LEFT_BUTTON));
        }

        incOpenedCellsCountIfNotOpened(x, y);
        log.debug("Number of open cells: " + getOpenedCellsCount());

        if (cellState == EMPTY) {
            for (var coords : getNeighboringCellIndices(x, y)) {
                if (!isCellVisited(coords[X], coords[Y]) && !isMarked(coords[X], coords[Y])) {
                    openEmptyCells(coords[X], coords[Y], cells);
                }
            }
        }
    }

    private List<int[]> getNeighboringCellIndices(int x, int y) {
        List<int[]> neighboringCellIndices = new ArrayList<>();

        if (y > 0) {
            neighboringCellIndices.addAll(getTopOrBottomCellIndices(x, y, true));
        }
        if (y < getRowsCount() - 1) {
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
        int upperBound = getColsCount() - 1;

        if (x > 0) {
            lowerBound = x - 1;
        }
        if (x < getColsCount() - 1) {
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
        if (x < getColsCount() - 1) {
            cellIndices.add(new int[]{x + 1, y});
        }

        return cellIndices;
    }

    public CellEvent getCellForMark(int x, int y) {
        CellEvent cell = null;
        CellState cellState = getCellStateForMark(x, y);
        if (cellState == CellState.CLOSED) {
            decreaseMarkCount();
            cell = new CellEvent(x, y, cellState, RIGHT_BUTTON, getBombsCount() - getMarkCount());
        } else if (cellState == CellState.MARKED) {
            incMarkCount();
            cell = new CellEvent(x, y, cellState, RIGHT_BUTTON, getBombsCount() - getMarkCount());
        }
        return cell;
    }

    private CellState getCellStateForMark(int x, int y) {
        if (checkIfCellCanBeMarked(x, y)) {
            if (isMarked(x, y)) {
                setCellAsMarked(x, y, false);
                return CLOSED;
            } else if (getMarkCount() < getBombsCount()) {
                setCellAsMarked(x, y, true);
                return MARKED;
            }
        }
        return NONE;
    }

    public List<CellEvent> getCellsAround(int x, int y) {
        if (isCellClosed(x, y)) {
            return List.of();
        }
        List<CellEvent> cellEvents = new ArrayList<>();

        var neighboringCellIndices = getNeighboringCellIndices(x, y);
        int bombsAround = checkCellsForBombs(neighboringCellIndices);
        int bombCount = 0;
        for (var coords : neighboringCellIndices) {
            if (isMarked(coords[X], coords[Y])) {
                bombCount++;
            }
        }
        if (bombCount != bombsAround) {
            return List.of();
        }
        for (var coords : neighboringCellIndices) {
            if (isCellClosed(coords[X], coords[Y]) && !isMarked(coords[X], coords[Y])) {
                cellEvents.addAll(getOpenedCells(coords[X], coords[Y]));
            }
        }

        return cellEvents;
    }

}
