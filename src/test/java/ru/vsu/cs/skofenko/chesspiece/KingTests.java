package ru.vsu.cs.skofenko.chesspiece;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.vsu.cs.skofenko.TestBoards;
import ru.vsu.cs.skofenko.logic.api.ChessColor;
import ru.vsu.cs.skofenko.logic.api.geometry.ICoordinate;
import ru.vsu.cs.skofenko.logic.chesspiece.King;
import ru.vsu.cs.skofenko.logic.service.Coordinate;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class KingTests extends ChessPieceTests {

    private King whiteKing;
    private King blackKing;

    @BeforeEach
    public void prepareData() {
        super.prepareData();
        whiteKing = new King(ChessColor.WHITE);
        blackKing = new King(ChessColor.BLACK);
    }

    @Test
    @DisplayName("Check possible go cells for white king on initial board")
    public void testWhiteKingInitialGoCells() {
        ICoordinate coordinate = Coordinate.createFromAxial(4, 1);
        Collection<ICoordinate> expectedGoCells = List.of(
                Coordinate.createFromAxial(2, 2),
                Coordinate.createFromAxial(3, 1)
        );

        testPiecePossibleCells(
                TestBoards.INITIAL_BOARD,
                coordinate,
                true,
                expectedGoCells
        );
    }

    @Test
    @DisplayName("Check possible go cells for black king on initial board")
    public void testBlackKingInitialGoCells() {
        ICoordinate coordinate = Coordinate.createFromAxial(-5, 1);
        Collection<ICoordinate> expectedCaptureCells = List.of(
                Coordinate.createFromAxial(-4, 1),
                Coordinate.createFromAxial(-4, 2)
        );

        testPiecePossibleCells(
                TestBoards.INITIAL_BOARD,
                coordinate,
                true,
                expectedCaptureCells
        );
    }

    @Test
    @DisplayName("Check possible capture cells for white king on initial board")
    public void testWhiteKingInitialCaptureCells() {
        ICoordinate coordinate = Coordinate.createFromAxial(4, 1);
        Collection<ICoordinate> expectedCaptureCells = Collections.emptyList();

        testPiecePossibleCells(
                TestBoards.INITIAL_BOARD,
                coordinate,
                false,
                expectedCaptureCells
        );
    }

    @Test
    @DisplayName("Check possible capture cells for black king on initial board")
    public void testBlackKingInitialCaptureCells() {
        ICoordinate coordinate = Coordinate.createFromAxial(-5, 1);
        Collection<ICoordinate> expectedCaptureCells = Collections.emptyList();

        testPiecePossibleCells(
                TestBoards.INITIAL_BOARD,
                coordinate,
                false,
                expectedCaptureCells
        );
    }

    @Test
    @DisplayName("Check king capture down of another color piece")
    public void testKingCaptureDown() {
        testPieceCapture(
                whiteKing, Coordinate.createFromAxial(1, 0)
        );
    }

    @Test
    @DisplayName("Check king capture diagonal up left of another color piece")
    public void testKingCaptureDiagonalUpLeft() {
        testPieceCapture(
                whiteKing, Coordinate.createFromAxial(-2, 1)
        );
    }

    @Test
    @DisplayName("Check king capture of same color piece")
    public void testKingCaptureSameColor() {
        testPieceCaptureSameColor(
                blackKing, Coordinate.createFromAxial(1, 0)
        );
    }
}
