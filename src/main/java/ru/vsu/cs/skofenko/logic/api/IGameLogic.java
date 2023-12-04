package ru.vsu.cs.skofenko.logic.api;

import ru.vsu.cs.skofenko.logic.api.board.Board;
import ru.vsu.cs.skofenko.logic.api.geometry.ICoordinate;

public interface IGameLogic{
    int N = 11;

    ChessColor getNowTurn();

    GameState getGameState();

    Board getBoard();

    boolean clickCell(ICoordinate cord);

    boolean promotePawn(IChessPiece piece);
}
