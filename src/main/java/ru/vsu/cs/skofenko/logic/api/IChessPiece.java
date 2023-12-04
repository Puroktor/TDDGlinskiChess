package ru.vsu.cs.skofenko.logic.api;

import ru.vsu.cs.skofenko.logic.api.board.Board;
import ru.vsu.cs.skofenko.logic.api.geometry.ICoordinate;

import java.util.Collection;

public interface IChessPiece {

    ICoordinate getCoordinate();

    void setCoordinate(ICoordinate coordinate);

    ChessColor getColor();

    void setPossibleCells(Board board, boolean toGo);

    Collection<ICoordinate> getPossibleGoCells();

    Collection<ICoordinate> getPossibleCaptureCells();
}
