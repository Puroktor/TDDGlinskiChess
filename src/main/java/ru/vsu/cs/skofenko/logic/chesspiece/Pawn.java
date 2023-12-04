package ru.vsu.cs.skofenko.logic.chesspiece;

import ru.vsu.cs.skofenko.logic.api.ChessColor;
import ru.vsu.cs.skofenko.logic.api.board.Board;
import ru.vsu.cs.skofenko.logic.api.geometry.ICoordinate;
import ru.vsu.cs.skofenko.logic.service.Coordinate;
import ru.vsu.cs.skofenko.logic.api.geometry.Directions;

import java.util.Collection;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

public class Pawn extends ChessPiece {
    private boolean notMoved = true;

    public boolean hasNotMoved() {
        return notMoved;
    }

    public void moved() {
        this.notMoved = false;
    }

    @Override
    protected Collection<ICoordinate> getPossibleCells(Board board, boolean toGo) {
        Coordinate coordinateImpl = (Coordinate) coordinate;
        Set<ICoordinate> set = new HashSet<>();
        if (toGo) {
            Directions d = (color == ChessColor.WHITE) ? Directions.UP : Directions.DOWN;
            Coordinate cord = coordinateImpl.copy();
            for (int k = 0; (k < 1) || (k < 2 && notMoved); k++) {
                cord.plus(d);
                if (cord.isInBounds() && board.getPiece(cord) == null) {
                    set.add(cord.copy());
                } else {
                    break;
                }
            }
        } else {
            EnumSet<Directions> enumSet;
            if (color == ChessColor.WHITE) {
                enumSet = EnumSet.of(Directions.UP_LEFT, Directions.UP_RIGHT);
            } else {
                enumSet = EnumSet.of(Directions.DOWN_LEFT, Directions.DOWN_RIGHT);
            }
            for (Directions d : enumSet) {
                Coordinate cord = coordinateImpl.copy();
                cord.plus(d);
                if (cord.isInBounds() && (board.getPiece(cord) != null && board.getPiece(cord).getColor() != color
                        || cord.equals(board.getEnPassantCord())))
                    set.add(cord);
            }
        }
        return set;
    }

    public Pawn(ChessColor color) {
        super(color);
    }
}
