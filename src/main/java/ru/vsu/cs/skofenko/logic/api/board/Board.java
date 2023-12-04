package ru.vsu.cs.skofenko.logic.api.board;

import lombok.Data;
import ru.vsu.cs.skofenko.logic.api.IChessPiece;
import ru.vsu.cs.skofenko.logic.api.geometry.ICoordinate;

import java.io.Serializable;

import static ru.vsu.cs.skofenko.logic.api.IGameLogic.N;

@Data
public class Board implements Serializable {
    private final BoardCell[][] board;
    private ICoordinate enPassantCord;

    public Board() {
        board = new BoardCell[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                board[i][j] = new BoardCell();
            }
        }
    }

    public BoardCell getCell(ICoordinate cord) {
        return board[cord.getI()][cord.getJ()];
    }

    public IChessPiece getPiece(ICoordinate cord) {
        return getCell(cord).getPiece();
    }
}
