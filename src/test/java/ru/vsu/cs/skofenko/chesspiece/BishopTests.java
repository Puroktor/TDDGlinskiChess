package ru.vsu.cs.skofenko.chesspiece;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.vsu.cs.skofenko.TestBoards;
import ru.vsu.cs.skofenko.logic.api.ChessColor;
import ru.vsu.cs.skofenko.logic.api.geometry.ICoordinate;
import ru.vsu.cs.skofenko.logic.chesspiece.Bishop;
import ru.vsu.cs.skofenko.logic.service.Coordinate;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class BishopTests extends ChessPieceTests {

    private Bishop whiteBishop;
    private Bishop blackBishop;

    @BeforeEach
    public void prepareData() {
        super.prepareData();
        whiteBishop = new Bishop(ChessColor.WHITE);
        blackBishop = new Bishop(ChessColor.BLACK);
    }

    @Test
    @DisplayName("Check possible go cells for white bishop on initial board")
    public void testWhiteBishopInitialGoCells() {
        ICoordinate coordinate = Coordinate.createFromAxial(-5, 0);
        Collection<ICoordinate> expectedGoCells = List.of(
                Coordinate.createFromAxial(-3, -1),
                Coordinate.createFromAxial(-4, 1)
        );

        testPiecePossibleCells(
                TestBoards.INITIAL_BOARD,
                coordinate,
                true,
                expectedGoCells
        );
    }

    @Test
    @DisplayName("Check possible go cells for black bishop on initial board")
    public void testBlackBishopInitialGoCells() {
        ICoordinate coordinate = Coordinate.createFromAxial(3, 0);
        Collection<ICoordinate> expectedGoCells = List.of(
                Coordinate.createFromAxial(2, 2),
                Coordinate.createFromAxial(4, -2)
        );

        testPiecePossibleCells(
                TestBoards.INITIAL_BOARD,
                coordinate,
                true,
                expectedGoCells
        );
    }

    @Test
    @DisplayName("Check possible capture cells for white bishop on initial board")
    public void testWhiteBishopInitialCaptureCells() {
        ICoordinate coordinate = Coordinate.createFromAxial(-5, 0);
        Collection<ICoordinate> expectedCaptureCells = Collections.emptyList();

        testPiecePossibleCells(
                TestBoards.INITIAL_BOARD,
                coordinate,
                false,
                expectedCaptureCells
        );
    }

    @Test
    @DisplayName("Check possible capture cells for black bishop on initial board")
    public void testBlackBishopInitialCaptureCells() {
        ICoordinate coordinate = Coordinate.createFromAxial(3, 0);
        Collection<ICoordinate> expectedCaptureCells = Collections.emptyList();

        testPiecePossibleCells(
                TestBoards.INITIAL_BOARD,
                coordinate,
                false,
                expectedCaptureCells
        );
    }

    @Test
    @DisplayName("Check bishop capture diagonal left of another color piece")
    public void testBishopCaptureDiagonalLeft() {
        testPieceCapture(
                whiteBishop, Coordinate.createFromAxial(-2, 1)
        );
    }

    @Test
    @DisplayName("Check bishop capture diagonal down right of another color piece")
    public void testBishopCaptureDiagonalDownRight() {
        testPieceCapture(
                whiteBishop, Coordinate.createFromAxial(2, 2)
        );
    }

    @Test
    @DisplayName("Check bishop capture of same color piece")
    public void testBishopCaptureSameColor() {
        testPieceCaptureSameColor(
                blackBishop, Coordinate.createFromAxial(-2, 1)
        );
    }
}
