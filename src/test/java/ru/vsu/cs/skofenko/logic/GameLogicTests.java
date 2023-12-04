package ru.vsu.cs.skofenko.logic;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.vsu.cs.skofenko.logic.api.ChessColor;
import ru.vsu.cs.skofenko.logic.api.GameState;
import ru.vsu.cs.skofenko.logic.api.IGameLogic;
import ru.vsu.cs.skofenko.logic.api.board.Board;
import ru.vsu.cs.skofenko.logic.api.board.BoardCell;
import ru.vsu.cs.skofenko.logic.api.geometry.ICoordinate;
import ru.vsu.cs.skofenko.logic.chesspiece.King;
import ru.vsu.cs.skofenko.logic.chesspiece.Queen;
import ru.vsu.cs.skofenko.logic.service.Coordinate;
import ru.vsu.cs.skofenko.logic.service.GameLogic;

public class GameLogicTests {

    private IGameLogic initialGameLogic;
    private Queen whiteQueen;
    private King whiteKing;

    @BeforeEach
    public void prepareData() {
        initialGameLogic = new GameLogic();
        whiteQueen = new Queen(ChessColor.WHITE);
        whiteKing = new King(ChessColor.WHITE);
    }

    @Test
    @DisplayName("Check if cell types change after piece selected")
    public void testCellTypeChangesAfterClick() {
        ICoordinate coordinate = Coordinate.createFromAxial(1, 0);
        ICoordinate reachableCord = Coordinate.createFromAxial(0, 0);
        Board board = initialGameLogic.getBoard();
        initialGameLogic.clickCell(coordinate);

        Assertions.assertEquals(ChessColor.WHITE, initialGameLogic.getNowTurn());
        Assertions.assertEquals(BoardCell.CellType.SELECTED, board.getCell(coordinate).getCellType());
        Assertions.assertEquals(BoardCell.CellType.REACHABLE, board.getCell(reachableCord).getCellType());
    }

    @Test
    @DisplayName("Check whether turn changes after piece moved")
    public void testNowTurnChanges() {
        Assertions.assertEquals(ChessColor.WHITE, initialGameLogic.getNowTurn());

        ICoordinate fromCord = Coordinate.createFromAxial(1, 0);
        ICoordinate toCord = Coordinate.createFromAxial(0, 0);
        initialGameLogic.clickCell(fromCord);
        initialGameLogic.clickCell(toCord);

        Assertions.assertEquals(ChessColor.BLACK, initialGameLogic.getNowTurn());
    }

    @Test
    @DisplayName("Check if player can move enemy pieces")
    public void testCantMoveEnemyPieces() {
        Assertions.assertEquals(ChessColor.WHITE, initialGameLogic.getNowTurn());

        ICoordinate fromCord = Coordinate.createFromAxial(-5, 4);
        ICoordinate toCord = Coordinate.createFromAxial(-4, 4);

        Assertions.assertFalse(initialGameLogic.clickCell(fromCord));
        Assertions.assertFalse(initialGameLogic.clickCell(toCord));
        Assertions.assertEquals(ChessColor.WHITE, initialGameLogic.getNowTurn());
        Assertions.assertNotNull(initialGameLogic.getBoard().getPiece(fromCord));
    }

    @Test
    @DisplayName("Try to promote pawn when impossible")
    public void testImpossiblePawnPromotion() {
        ICoordinate coordinate = Coordinate.createFromAxial(-5, 0);
        whiteQueen.setCoordinate(coordinate);
        boolean result = initialGameLogic.promotePawn(whiteQueen);

        Assertions.assertFalse(result);
        Assertions.assertNotEquals(initialGameLogic.getBoard().getPiece(coordinate), whiteQueen);
    }

    @Test
    @DisplayName("Test game state changing to Check")
    public void testCheckState() {
        // White Queen up-left
        initialGameLogic.clickCell(Coordinate.createFromAxial(5, -1));
        initialGameLogic.clickCell(Coordinate.createFromAxial(3, -3));

        // Black pawn down
        initialGameLogic.clickCell(Coordinate.createFromAxial(-1, -4));
        initialGameLogic.clickCell(Coordinate.createFromAxial(0, -4));

        // White Queen up-right - Check
        initialGameLogic.clickCell(Coordinate.createFromAxial(3, -3));
        initialGameLogic.clickCell(Coordinate.createFromAxial(-3, 3));

        Assertions.assertEquals(ChessColor.BLACK, initialGameLogic.getNowTurn());
        Assertions.assertEquals(GameState.CHECK, initialGameLogic.getGameState());
    }

    @Test
    @DisplayName("Try to successfully promote pawn")
    public void testPromotion() {
        setUpPawnPromotion();
        // White Pawn - Up = Promotion
        ICoordinate promotionCord = Coordinate.createFromAxial(-1, -4);
        initialGameLogic.clickCell(Coordinate.createFromAxial(0, -4));
        boolean needPromotion = initialGameLogic.clickCell(promotionCord);
        boolean promotionResult = initialGameLogic.promotePawn(whiteQueen);

        Assertions.assertTrue(needPromotion);
        Assertions.assertTrue(promotionResult);
        Assertions.assertEquals(initialGameLogic.getBoard().getPiece(promotionCord), whiteQueen);
    }

    @Test
    @DisplayName("Try to promote pawn with invalid piece")
    public void testPromotionWithInvalidPiece() {
        setUpPawnPromotion();
        // White Pawn - Up = Promotion
        ICoordinate promotionCord = Coordinate.createFromAxial(-1, -4);
        initialGameLogic.clickCell(Coordinate.createFromAxial(0, -4));
        boolean needPromotion = initialGameLogic.clickCell(promotionCord);
        boolean promotionResult = initialGameLogic.promotePawn(whiteKing);

        Assertions.assertTrue(needPromotion);
        Assertions.assertFalse(promotionResult);
        Assertions.assertNotEquals(initialGameLogic.getBoard().getPiece(promotionCord), whiteKing);
    }

    private void setUpPawnPromotion() {
        // White Pawn - Up
        initialGameLogic.clickCell(Coordinate.createFromAxial(4, -3));
        initialGameLogic.clickCell(Coordinate.createFromAxial(2, -3));

        // Left Black Pawn - Down
        initialGameLogic.clickCell(Coordinate.createFromAxial(-1, -4));
        initialGameLogic.clickCell(Coordinate.createFromAxial(0, -4));

        // White Pawn - Up
        initialGameLogic.clickCell(Coordinate.createFromAxial(2, -3));
        initialGameLogic.clickCell(Coordinate.createFromAxial(1, -3));

        // Right Black Pawn - Down
        initialGameLogic.clickCell(Coordinate.createFromAxial(-5, 4));
        initialGameLogic.clickCell(Coordinate.createFromAxial(-4, 4));

        // White Pawn - Up
        initialGameLogic.clickCell(Coordinate.createFromAxial(1, -3));
        initialGameLogic.clickCell(Coordinate.createFromAxial(0, -3));

        // Right Black Pawn - Down
        initialGameLogic.clickCell(Coordinate.createFromAxial(-4, 4));
        initialGameLogic.clickCell(Coordinate.createFromAxial(-3, 4));

        // White Pawn - Capture
        initialGameLogic.clickCell(Coordinate.createFromAxial(0, -3));
        initialGameLogic.clickCell(Coordinate.createFromAxial(0, -4));

        // Right Black Pawn - Down
        initialGameLogic.clickCell(Coordinate.createFromAxial(-3, 4));
        initialGameLogic.clickCell(Coordinate.createFromAxial(-2, 4));
    }
}
