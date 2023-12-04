package ru.vsu.cs.skofenko.logic.chesspiece;

import ru.vsu.cs.skofenko.logic.api.ChessColor;
import ru.vsu.cs.skofenko.logic.api.board.Board;
import ru.vsu.cs.skofenko.logic.api.geometry.ICoordinate;

import java.util.Collection;

public class Bishop extends ChessPiece {
    public Bishop(ChessColor color) {
        super(color);
    }

    @Override
    public Collection<ICoordinate> getPossibleCells(Board board, boolean toGo) {
        return getCellsOfStraightPieces(board, this, false, toGo);
    }
}
