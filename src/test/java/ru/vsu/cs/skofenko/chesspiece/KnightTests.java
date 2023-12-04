package ru.vsu.cs.skofenko.chesspiece;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.vsu.cs.skofenko.TestBoards;
import ru.vsu.cs.skofenko.logic.api.ChessColor;
import ru.vsu.cs.skofenko.logic.api.geometry.ICoordinate;
import ru.vsu.cs.skofenko.logic.chesspiece.Knight;
import ru.vsu.cs.skofenko.logic.service.Coordinate;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class KnightTests extends ChessPieceTests {

    private Knight whiteKnight;
    private Knight blackKnight;

    @BeforeEach
    public void prepareData() {
        super.prepareData();
        whiteKnight = new Knight(ChessColor.WHITE);
        blackKnight = new Knight(ChessColor.BLACK);
    }

    @Test
    @DisplayName("Check possible go cells for white knight on initial board")
    public void testWhiteKnightInitialGoCells() {
        ICoordinate coordinate = Coordinate.createFromAxial(5, -2);
        Collection<ICoordinate> expectedGoCells = List.of(
                Coordinate.createFromAxial(4, -4),
                Coordinate.createFromAxial(3, 1),
                Coordinate.createFromAxial(3, -3),
                Coordinate.createFromAxial(2, 0)
        );

        testPiecePossibleCells(
                TestBoards.INITIAL_BOARD,
                coordinate,
                true,
                expectedGoCells
        );
    }

    @Test
    @DisplayName("Check possible go cells for black knight on initial board")
    public void testBlackKnightInitialGoCells() {
        ICoordinate coordinate = Coordinate.createFromAxial(3, 2);
        Collection<ICoordinate> expectedGoCells = List.of(
                Coordinate.createFromAxial(0, 3),
                Coordinate.createFromAxial(0, 4),
                Coordinate.createFromAxial(4, -1),
                Coordinate.createFromAxial(2, 0)
        );

        testPiecePossibleCells(
                TestBoards.INITIAL_BOARD,
                coordinate,
                true,
                expectedGoCells
        );
    }

    @Test
    @DisplayName("Check possible capture cells for white knight on initial board")
    public void testWhiteKnightInitialCaptureCells() {
        ICoordinate coordinate = Coordinate.createFromAxial(5, -2);
        Collection<ICoordinate> expectedCaptureCells = Collections.emptyList();

        testPiecePossibleCells(
                TestBoards.INITIAL_BOARD,
                coordinate,
                false,
                expectedCaptureCells
        );
    }

    @Test
    @DisplayName("Check possible capture cells for black knight on initial board")
    public void testBlackKnightInitialCaptureCells() {
        ICoordinate coordinate = Coordinate.createFromAxial(3, 2);
        Collection<ICoordinate> expectedCaptureCells = Collections.emptyList();

        testPiecePossibleCells(
                TestBoards.INITIAL_BOARD,
                coordinate,
                false,
                expectedCaptureCells
        );
    }

    @Test
    @DisplayName("Check knight capture right of another color piece")
    public void testKnightCaptureRight() {
        testPieceCapture(
                whiteKnight, Coordinate.createFromAxial(3, -1)
        );
    }

    @Test
    @DisplayName("Check knight capture left of another color piece")
    public void testQueenCaptureDiagonalLeft() {
        testPieceCapture(
                whiteKnight, Coordinate.createFromAxial(-3, 1)
        );
    }

    @Test
    @DisplayName("Check knight capture of same color piece")
    public void testQueenCaptureSameColor() {
        testPieceCaptureSameColor(
                blackKnight, Coordinate.createFromAxial(3, -1)
        );
    }
}
