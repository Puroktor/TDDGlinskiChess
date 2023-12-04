package ru.vsu.cs.skofenko.logic.chesspiece;

import ru.vsu.cs.skofenko.logic.api.ChessColor;
import ru.vsu.cs.skofenko.logic.api.board.Board;
import ru.vsu.cs.skofenko.logic.api.geometry.ICoordinate;

import java.util.Collection;

public class Rook extends ChessPiece {
    public Rook(ChessColor color) {
        super(color);
    }

    @Override
    public Collection<ICoordinate> getPossibleCells(Board board, boolean toGo) {
        return getCellsOfStraightPieces(board, this, true, toGo);
    }
}
