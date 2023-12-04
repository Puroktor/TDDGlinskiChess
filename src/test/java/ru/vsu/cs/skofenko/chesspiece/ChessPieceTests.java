package ru.vsu.cs.skofenko.chesspiece;

import org.junit.jupiter.api.Assertions;
import ru.vsu.cs.skofenko.logic.api.ChessColor;
import ru.vsu.cs.skofenko.logic.api.IChessPiece;
import ru.vsu.cs.skofenko.logic.api.board.Board;
import ru.vsu.cs.skofenko.logic.api.geometry.ICoordinate;
import ru.vsu.cs.skofenko.logic.chesspiece.Pawn;
import ru.vsu.cs.skofenko.logic.service.Coordinate;

import java.util.Collection;

public abstract class ChessPieceTests {

    private Board testBoard;
    private Pawn testPawn;

    public void prepareData() {
        testBoard = new Board();
        testPawn = new Pawn(ChessColor.BLACK);
    }

    protected void testPiecePossibleCells(Board board, ICoordinate pieceCoord, boolean toGo, Collection<ICoordinate> expectedCells) {
        IChessPiece piece = board.getPiece(pieceCoord);
        piece.setCoordinate(pieceCoord);
        piece.setPossibleCells(board, toGo);

        Collection<ICoordinate> actualCells = toGo ? piece.getPossibleGoCells() : piece.getPossibleCaptureCells();

        Assertions.assertEquals(expectedCells.size(), actualCells.size());
        Assertions.assertTrue(actualCells.containsAll(expectedCells));
    }

    protected void testPieceCapture(IChessPiece piece, ICoordinate pawnCoord) {
        ICoordinate pieceCoord = Coordinate.createFromAxial(0, 0);

        piece.setCoordinate(pieceCoord);
        testPawn.setCoordinate(pawnCoord);

        testBoard.getCell(pieceCoord).setPiece(piece);
        testBoard.getCell(pawnCoord).setPiece(testPawn);

        piece.setPossibleCells(testBoard, true);
        piece.setPossibleCells(testBoard, false);
        Collection<ICoordinate> goCellsCells = piece.getPossibleGoCells();
        Collection<ICoordinate> captureCells = piece.getPossibleCaptureCells();

        Assertions.assertEquals(1, captureCells.size());
        Assertions.assertTrue(captureCells.contains(pawnCoord));
        Assertions.assertFalse(goCellsCells.contains(pawnCoord));
    }

    protected void testPieceCaptureSameColor(IChessPiece piece, ICoordinate pawnCoord) {
        ICoordinate pieceCoord = Coordinate.createFromAxial(0, 0);

        piece.setCoordinate(pieceCoord);
        testPawn.setCoordinate(pawnCoord);

        testBoard.getCell(pieceCoord).setPiece(piece);
        testBoard.getCell(pawnCoord).setPiece(testPawn);

        piece.setPossibleCells(testBoard, true);
        piece.setPossibleCells(testBoard, false);
        Collection<ICoordinate> goCellsCells = piece.getPossibleGoCells();
        Collection<ICoordinate> captureCells = piece.getPossibleCaptureCells();

        Assertions.assertTrue(captureCells.isEmpty());
        Assertions.assertFalse(goCellsCells.contains(pawnCoord));
    }
}
