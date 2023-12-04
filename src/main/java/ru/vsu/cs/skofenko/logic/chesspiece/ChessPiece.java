package ru.vsu.cs.skofenko.logic.chesspiece;

import lombok.Getter;
import lombok.Setter;
import ru.vsu.cs.skofenko.logic.api.ChessColor;
import ru.vsu.cs.skofenko.logic.api.IChessPiece;
import ru.vsu.cs.skofenko.logic.api.board.Board;
import ru.vsu.cs.skofenko.logic.api.geometry.Directions;
import ru.vsu.cs.skofenko.logic.api.geometry.ICoordinate;
import ru.vsu.cs.skofenko.logic.service.Coordinate;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;


@Getter
@Setter
public abstract class ChessPiece implements IChessPiece, Serializable {

    protected ChessColor color;
    protected ICoordinate coordinate;
    protected Collection<ICoordinate> possibleGoCells;
    protected Collection<ICoordinate> possibleCaptureCells;

    public void setPossibleCells(Board board, boolean toGo) {
        if (toGo) {
            possibleGoCells = getPossibleCells(board, true);
        } else {
            possibleCaptureCells = getPossibleCells(board, false);
        }
    }

    protected abstract Collection<ICoordinate> getPossibleCells(Board board, boolean toGo);

    protected static Collection<ICoordinate> getCellsOfStraightPieces(Board board, ChessPiece piece, boolean isOrthogonal, boolean toGo) {
        Set<ICoordinate> set = new HashSet<>();
        for (Directions d : (isOrthogonal) ? Directions.getOrthogonal() : Directions.getDiagonal()) {
            Coordinate coordinateImpl = (Coordinate) piece.coordinate;
            Coordinate cord = coordinateImpl.copy();
            cord.plus(d);
            while (cord.isInBounds()) {
                if (board.getPiece(cord) != null) {
                    if (board.getPiece(cord).getColor() != piece.getColor() && !toGo) {
                        set.add(cord.copy());
                    }
                    break;
                } else if (toGo) {
                    set.add(cord.copy());
                }
                cord.plus(d);
            }
        }
        return set;
    }

    // except King and Pawn
    public static ChessPiece getChessPieceFromStr(String str, ChessColor color) {
        if (str == null) {
            throw new IllegalArgumentException("String is invalid");
        }

        return switch (str) {
            case "Rook" -> new Rook(color);
            case "Bishop" -> new Bishop(color);
            case "Knight" -> new Knight(color);
            case "Queen" -> new Queen(color);
            default -> throw new IllegalArgumentException("String is invalid");
        };
    }

    public ChessPiece(ChessColor color) {
        this.color = color;
    }
}
