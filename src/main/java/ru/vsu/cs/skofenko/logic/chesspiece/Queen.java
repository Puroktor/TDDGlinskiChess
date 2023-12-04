package ru.vsu.cs.skofenko.logic.chesspiece;

import ru.vsu.cs.skofenko.logic.api.ChessColor;
import ru.vsu.cs.skofenko.logic.api.board.Board;
import ru.vsu.cs.skofenko.logic.api.geometry.ICoordinate;

import java.util.Collection;

public class Queen extends ChessPiece {
    public Queen(ChessColor color) {
        super(color);
    }

    @Override
    protected Collection<ICoordinate> getPossibleCells(Board board, boolean toGo) {
        Collection<ICoordinate> collection = getCellsOfStraightPieces(board, this, false, toGo);
        collection.addAll(getCellsOfStraightPieces(board, this, true, toGo));
        return collection;
    }
}
