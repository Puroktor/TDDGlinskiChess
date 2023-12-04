package ru.vsu.cs.skofenko.chesspiece;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.vsu.cs.skofenko.TestBoards;
import ru.vsu.cs.skofenko.logic.api.ChessColor;
import ru.vsu.cs.skofenko.logic.api.board.Board;
import ru.vsu.cs.skofenko.logic.api.geometry.ICoordinate;
import ru.vsu.cs.skofenko.logic.chesspiece.Pawn;
import ru.vsu.cs.skofenko.logic.service.Coordinate;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class PawnTests extends ChessPieceTests {

    private Pawn whitePawn;
    private Pawn blackPawn;

    @BeforeEach
    public void prepareData() {
        super.prepareData();
        whitePawn = new Pawn(ChessColor.WHITE);
        blackPawn = new Pawn(ChessColor.BLACK);
    }

    @Test
    @DisplayName("Check possible go cells for pawn pawn on initial board")
    public void testWhitePawnInitialGoCells() {
        ICoordinate coordinate = Coordinate.createFromAxial(-5, 4);
        Collection<ICoordinate> expectedGoCells = List.of(
                Coordinate.createFromAxial(-4, 4),
                Coordinate.createFromAxial(-3, 4)
        );

        testPiecePossibleCells(
                TestBoards.INITIAL_BOARD,
                coordinate,
                true,
                expectedGoCells
        );
    }

    @Test
    @DisplayName("Check possible go cells for black pawn on initial board")
    public void testBlackPawnInitialGoCells() {
        ICoordinate coordinate = Coordinate.createFromAxial(-1, 0);
        Collection<ICoordinate> expectedCaptureCells = List.of(
                Coordinate.createFromAxial(0, 0)
        );

        testPiecePossibleCells(
                TestBoards.INITIAL_BOARD,
                coordinate,
                true,
                expectedCaptureCells
        );
    }

    @Test
    @DisplayName("Check possible capture cells for white pawn on initial board")
    public void testWhitePawnInitialCaptureCells() {
        ICoordinate coordinate = Coordinate.createFromAxial(-5, 4);
        Collection<ICoordinate> expectedCaptureCells = Collections.emptyList();

        testPiecePossibleCells(
                TestBoards.INITIAL_BOARD,
                coordinate,
                false,
                expectedCaptureCells
        );
    }

    @Test
    @DisplayName("Check possible capture cells for black pawn on initial board")
    public void testBlackPawnInitialCaptureCells() {
        ICoordinate coordinate = Coordinate.createFromAxial(-1, 0);
        Collection<ICoordinate> expectedCaptureCells = Collections.emptyList();

        testPiecePossibleCells(
                TestBoards.INITIAL_BOARD,
                coordinate,
                false,
                expectedCaptureCells
        );
    }

    @Test
    @DisplayName("Check pawn capture up-right of another color piece")
    public void testPawnCaptureUpRight() {
        testPieceCapture(
                whitePawn, Coordinate.createFromAxial(0, -1)
        );
    }

    @Test
    @DisplayName("Check pawn capture down left of another color piece")
    public void testPawnCaptureDownLeft() {
        testPieceCaptureSameColor(
                whitePawn, Coordinate.createFromAxial(0, 1)
        );
    }

    @Test
    @DisplayName("Check pawn capture of same color piece")
    public void testPawnCaptureSameColor() {
        testPieceCaptureSameColor(
                blackPawn, Coordinate.createFromAxial(-1, 1)
        );
    }

    @Test
    @DisplayName("Check pawn capture en passant of another color piece")
    public void testPawnCaptureEnPassant() {
        Board board = new Board();
        Pawn piece = whitePawn;
        Pawn pawn = blackPawn;
        ICoordinate pieceCoord = Coordinate.createFromAxial(0, 0);
        ICoordinate enPassantCord = Coordinate.createFromAxial(-1, 1);
        ICoordinate pawnCoord = Coordinate.createFromAxial(2, 0);

        piece.setCoordinate(pieceCoord);
        pawn.setCoordinate(pawnCoord);

        board.setEnPassantCord(enPassantCord);
        board.getCell(pieceCoord).setPiece(piece);
        board.getCell(pawnCoord).setPiece(pawn);

        piece.setPossibleCells(board, true);
        piece.setPossibleCells(board, false);
        Collection<ICoordinate> goCellsCells = piece.getPossibleGoCells();
        Collection<ICoordinate> captureCells = piece.getPossibleCaptureCells();

        Assertions.assertEquals(captureCells.size(), 1);
        Assertions.assertTrue(captureCells.contains(enPassantCord));
        Assertions.assertFalse(goCellsCells.contains(enPassantCord));
    }
}
