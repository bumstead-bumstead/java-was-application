package application.db;

import application.model.Board;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BoardDatabase {
    private static Map<Integer, Board> boards = new ConcurrentHashMap<>();

    public static void addBoard(Board board) {
        boards.put(boards.size(), board);
    }

    public static Board findBoardByNumber(int boardNumber) {
        return boards.get(boardNumber);
    }

    public static List<Board> findAll() {
        return new ArrayList<>(boards.values());
    }
}
