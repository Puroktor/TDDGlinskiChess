package ru.vsu.cs.skofenko.chesspiece;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.vsu.cs.skofenko.TestBoards;
import ru.vsu.cs.skofenko.logic.api.ChessColor;
import ru.vsu.cs.skofenko.logic.api.geometry.ICoordinate;
import ru.vsu.cs.skofenko.logic.chesspiece.Queen;
import ru.vsu.cs.skofenko.logic.service.Coordinate;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class QueenTests extends ChessPieceTests {

    private Queen whiteQueen;
    private Queen blackQueen;

    @BeforeEach
    public void prepareData() {
        super.prepareData();
        whiteQueen = new Queen(ChessColor.WHITE);
        blackQueen = new Queen(ChessColor.BLACK);
    }

    @Test
    @DisplayName("Check possible go cells for white queen on initial board")
    public void testWhiteQueenInitialGoCells() {
        ICoordinate coordinate = Coordinate.createFromAxial(5, -1);
        Collection<ICoordinate> expectedGoCells = List.of(
                Coordinate.createFromAxial(3, -1),
                Coordinate.createFromAxial(4, -1),
                Coordinate.createFromAxial(4, -2),
                Coordinate.createFromAxial(3, -3),
                Coordinate.createFromAxial(2, -4),
                Coordinate.createFromAxial(1, -5)
        );

        testPiecePossibleCells(
                TestBoards.INITIAL_BOARD,
                coordinate,
                true,
                expectedGoCells
        );
    }

    @Test
    @DisplayName("Check possible go cells for black queen on initial board")
    public void testBlackQueenInitialGoCells() {
        ICoordinate coordinate = Coordinate.createFromAxial(-4, -1);
        Collection<ICoordinate> expectedCaptureCells = List.of(
                Coordinate.createFromAxial(0, -3),
                Coordinate.createFromAxial(2, -4),
                Coordinate.createFromAxial(4, -5),
                Coordinate.createFromAxial(-3, -1),
                Coordinate.createFromAxial(-2, -1),
                Coordinate.createFromAxial(-2, -2)
        );

        testPiecePossibleCells(
                TestBoards.INITIAL_BOARD,
                coordinate,
                true,
                expectedCaptureCells
        );
    }

    @Test
    @DisplayName("Check possible capture cells for white queen on initial board")
    public void testWhiteQueenInitialCaptureCells() {
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
    @DisplayName("Check possible capture cells for black queen on initial board")
    public void testBlackQueenInitialCaptureCells() {
        ICoordinate coordinate = Coordinate.createFromAxial(-4, -1);
        Collection<ICoordinate> expectedCaptureCells = Collections.emptyList();

        testPiecePossibleCells(
                TestBoards.INITIAL_BOARD,
                coordinate,
                false,
                expectedCaptureCells
        );
    }

    @Test
    @DisplayName("Check queen capture down-right of another color piece")
    public void testQueenCaptureDownRight() {
        testPieceCapture(
                whiteQueen, Coordinate.createFromAxial(2, -2)
        );
    }

    @Test
    @DisplayName("Check rook capture diagonal left of another color piece")
    public void testQueenCaptureDiagonalLeft() {
        testPieceCapture(
                whiteQueen, Coordinate.createFromAxial(-2, 1)
        );
    }

    @Test
    @DisplayName("Check rook capture of same color piece")
    public void testQueenCaptureSameColor() {
        testPieceCaptureSameColor(
                blackQueen, Coordinate.createFromAxial(-2, 1)
        );
    }
}
