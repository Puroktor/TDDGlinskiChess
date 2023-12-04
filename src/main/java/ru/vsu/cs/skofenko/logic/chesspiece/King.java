package ru.vsu.cs.skofenko.logic.chesspiece;

import ru.vsu.cs.skofenko.logic.api.ChessColor;
import ru.vsu.cs.skofenko.logic.api.board.Board;
import ru.vsu.cs.skofenko.logic.api.geometry.ICoordinate;
import ru.vsu.cs.skofenko.logic.service.Coordinate;
import ru.vsu.cs.skofenko.logic.api.geometry.Directions;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class King extends ChessPiece {
    public King(ChessColor color) {
        super(color);
    }

    @Override
    protected Collection<ICoordinate> getPossibleCells(Board board, boolean toGo) {
        Coordinate coordinateImpl = (Coordinate) coordinate;
        Set<ICoordinate> set = new HashSet<>();
        for (Directions d : Directions.values()) {
            Coordinate cord = coordinateImpl.copy();
            cord.plus(d);
            if (cord.isInBounds()) {
                if (board.getPiece(cord) == null && toGo) {
                    set.add(cord);
                } else if (board.getPiece(cord) != null && board.getPiece(cord).getColor() != color && !toGo) {
                    set.add(cord);
                }
            }
        }
        return set;
    }
}
