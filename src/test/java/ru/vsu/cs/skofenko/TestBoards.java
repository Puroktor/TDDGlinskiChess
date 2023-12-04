package ru.vsu.cs.skofenko;

import ru.vsu.cs.skofenko.logic.api.ChessColor;
import ru.vsu.cs.skofenko.logic.api.board.Board;
import ru.vsu.cs.skofenko.logic.chesspiece.*;
import ru.vsu.cs.skofenko.logic.service.Coordinate;

public class TestBoards {

    public final static Board INITIAL_BOARD = new Board();

    static {
        placeInitialChess(INITIAL_BOARD);
    }

    private static void placeInitialChess(Board board) {
        board.getCell(Coordinate.createFromInner(0, 4)).setPiece(new Pawn(ChessColor.BLACK));
        board.getCell(Coordinate.createFromInner(6, 9)).setPiece(new Pawn(ChessColor.WHITE));

        board.getCell(Coordinate.createFromInner(0, 3)).setPiece(new Rook(ChessColor.BLACK));
        board.getCell(Coordinate.createFromInner(1, 4)).setPiece(new Pawn(ChessColor.BLACK));
        board.getCell(Coordinate.createFromInner(6, 8)).setPiece(new Pawn(ChessColor.WHITE));
        board.getCell(Coordinate.createFromInner(7, 8)).setPiece(new Rook(ChessColor.WHITE));

        board.getCell(Coordinate.createFromInner(0, 2)).setPiece(new Knight(ChessColor.BLACK));
        board.getCell(Coordinate.createFromInner(2, 4)).setPiece(new Pawn(ChessColor.BLACK));
        board.getCell(Coordinate.createFromInner(6, 7)).setPiece(new Pawn(ChessColor.WHITE));
        board.getCell(Coordinate.createFromInner(8, 7)).setPiece(new Knight(ChessColor.WHITE));


        board.getCell(Coordinate.createFromInner(3, 4)).setPiece(new Pawn(ChessColor.BLACK));
        board.getCell(Coordinate.createFromInner(6, 6)).setPiece(new Pawn(ChessColor.WHITE));

        board.getCell(Coordinate.createFromInner(9, 6)).setPiece(new King(ChessColor.WHITE));
        board.getCell(Coordinate.createFromInner(0, 1)).setPiece(new King(ChessColor.BLACK));

        board.getCell(Coordinate.createFromInner(0, 0)).setPiece(new Bishop(ChessColor.BLACK));
        board.getCell(Coordinate.createFromInner(1, 1)).setPiece(new Bishop(ChessColor.BLACK));
        board.getCell(Coordinate.createFromInner(2, 2)).setPiece(new Bishop(ChessColor.BLACK));
        board.getCell(Coordinate.createFromInner(4, 4)).setPiece(new Pawn(ChessColor.BLACK));
        board.getCell(Coordinate.createFromInner(6, 5)).setPiece(new Pawn(ChessColor.WHITE));
        board.getCell(Coordinate.createFromInner(8, 5)).setPiece(new Bishop(ChessColor.WHITE));
        board.getCell(Coordinate.createFromInner(9, 5)).setPiece(new Bishop(ChessColor.WHITE));
        board.getCell(Coordinate.createFromInner(10, 5)).setPiece(new Bishop(ChessColor.WHITE));

        board.getCell(Coordinate.createFromInner(1, 0)).setPiece(new Queen(ChessColor.BLACK));
        board.getCell(Coordinate.createFromInner(4, 3)).setPiece(new Pawn(ChessColor.BLACK));
        board.getCell(Coordinate.createFromInner(7, 4)).setPiece(new Pawn(ChessColor.WHITE));
        board.getCell(Coordinate.createFromInner(10, 4)).setPiece(new Queen(ChessColor.WHITE));

        board.getCell(Coordinate.createFromInner(2, 0)).setPiece(new Knight(ChessColor.BLACK));
        board.getCell(Coordinate.createFromInner(4, 2)).setPiece(new Pawn(ChessColor.BLACK));
        board.getCell(Coordinate.createFromInner(8, 3)).setPiece(new Pawn(ChessColor.WHITE));
        board.getCell(Coordinate.createFromInner(10, 3)).setPiece(new Knight(ChessColor.WHITE));

        board.getCell(Coordinate.createFromInner(3, 0)).setPiece(new Rook(ChessColor.BLACK));
        board.getCell(Coordinate.createFromInner(4, 1)).setPiece(new Pawn(ChessColor.BLACK));
        board.getCell(Coordinate.createFromInner(9, 2)).setPiece(new Pawn(ChessColor.WHITE));
        board.getCell(Coordinate.createFromInner(10, 2)).setPiece(new Rook(ChessColor.WHITE));

        board.getCell(Coordinate.createFromInner(4, 0)).setPiece(new Pawn(ChessColor.BLACK));
        board.getCell(Coordinate.createFromInner(10, 1)).setPiece(new Pawn(ChessColor.WHITE));
    }
}
