package ru.vsu.cs.skofenko.logic.chesspiece;

import ru.vsu.cs.skofenko.logic.api.ChessColor;
import ru.vsu.cs.skofenko.logic.api.board.Board;
import ru.vsu.cs.skofenko.logic.api.geometry.ICoordinate;
import ru.vsu.cs.skofenko.logic.service.Coordinate;
import ru.vsu.cs.skofenko.logic.service.GameLogic;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class Knight extends ChessPiece {
    public Knight(ChessColor color) {
        super(color);
    }

    @Override
    protected Collection<ICoordinate> getPossibleCells(Board board, boolean toGo) {
        Coordinate cord = (Coordinate) coordinate;
        Set<ICoordinate> set = new HashSet<>();
        int r0 = cord.getR(), q0 = cord.getQ();
        int y0 = -r0 - q0;
        for (int i = Math.max(r0 - 3, -GameLogic.N / 2); i <= Math.min(r0 + 3, GameLogic.N / 2); i++) {
            for (int k = Math.max(q0 - 3, -GameLogic.N / 2); k <= Math.min(q0 + 3, GameLogic.N / 2); k++) {
                int j = -k - i;
                Coordinate to = Coordinate.createFromAxial(i, k);
                if (Math.abs(i + k) <= GameLogic.N / 2 && i != r0 && j != y0 && k != q0 && cord.distanceFrom(to) == 3) {
                    if (board.getPiece(to) == null && toGo) {
                        set.add(to);
                    } else if (board.getPiece(to) != null && board.getPiece(to).getColor() != color && !toGo) {
                        set.add(to);
                    }
                }
            }
        }
        return set;
    }
}
