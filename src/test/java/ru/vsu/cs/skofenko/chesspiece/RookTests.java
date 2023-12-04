package ru.vsu.cs.skofenko.chesspiece;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.vsu.cs.skofenko.TestBoards;
import ru.vsu.cs.skofenko.logic.api.ChessColor;
import ru.vsu.cs.skofenko.logic.api.geometry.ICoordinate;
import ru.vsu.cs.skofenko.logic.chesspiece.Rook;
import ru.vsu.cs.skofenko.logic.service.Coordinate;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class RookTests extends ChessPieceTests {

    private Rook whiteRook;
    private Rook blackRook;

    @BeforeEach
    public void prepareData() {
        super.prepareData();
        whiteRook = new Rook(ChessColor.WHITE);
        blackRook = new Rook(ChessColor.BLACK);
    }

    @Test
    @DisplayName("Check possible go cells for white rook on initial board")
    public void testWhiteRookInitialGoCells() {
        ICoordinate coordinate = Coordinate.createFromAxial(2, 3);
        Collection<ICoordinate> expectedGoCells = List.of(
                Coordinate.createFromAxial(2, 2),
                Coordinate.createFromAxial(2, 1),
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
    @DisplayName("Check possible go cells for black rook on initial board")
    public void testBlackRookInitialGoCells() {
        ICoordinate coordinate = Coordinate.createFromAxial(-2, -3);
        Collection<ICoordinate> expectedCaptureCells = List.of(
                Coordinate.createFromAxial(-2, 0),
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
    @DisplayName("Check possible capture cells for white rook on initial board")
    public void testWhiteRookInitialCaptureCells() {
        ICoordinate coordinate = Coordinate.createFromAxial(2, 3);
        Collection<ICoordinate> expectedCaptureCells = Collections.emptyList();

        testPiecePossibleCells(
                TestBoards.INITIAL_BOARD,
                coordinate,
                false,
                expectedCaptureCells
        );
    }

    @Test
    @DisplayName("Check possible capture cells for black rook on initial board")
    public void testBlackRookInitialCaptureCells() {
        ICoordinate coordinate = Coordinate.createFromAxial(-2, -3);
        Collection<ICoordinate> expectedCaptureCells = Collections.emptyList();

        testPiecePossibleCells(
                TestBoards.INITIAL_BOARD,
                coordinate,
                false,
                expectedCaptureCells
        );
    }

    @Test
    @DisplayName("Check rook capture up-right of another color piece")
    public void testRookCaptureUpRight() {
        testPieceCapture(
                whiteRook, Coordinate.createFromAxial(0, -3)
        );
    }

    @Test
    @DisplayName("Check rook capture down right of another color piece")
    public void testRookCaptureDown() {
        testPieceCapture(
                whiteRook, Coordinate.createFromAxial(2, 0)
        );
    }

    @Test
    @DisplayName("Check rook capture of same color piece")
    public void testRookCaptureSameColor() {
        testPieceCaptureSameColor(
                blackRook, Coordinate.createFromAxial(2, 0)
        );
    }
}
