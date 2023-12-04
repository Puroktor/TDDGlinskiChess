package ru.vsu.cs.skofenko.logic.service;

import lombok.Getter;
import ru.vsu.cs.skofenko.logic.api.*;
import ru.vsu.cs.skofenko.logic.api.board.Board;
import ru.vsu.cs.skofenko.logic.api.board.BoardCell;
import ru.vsu.cs.skofenko.logic.api.geometry.ICoordinate;
import ru.vsu.cs.skofenko.logic.chesspiece.*;

import java.io.Serializable;
import java.util.*;

@Getter
public class GameLogic implements IGameLogic, Serializable {
    private final Board board;
    private final Collection<IChessPiece> chessPieces = new HashSet<>();

    private ChessColor nowTurn = ChessColor.WHITE;
    private IChessPiece selectedPiece;
    private GameState gameState;
    private King whiteKing, blackKing;
    private Pawn enPassant;
    private ICoordinate promotingCord;
    private boolean locked = false;

    public GameLogic() {
        board = new Board();
        placeChess();
        fillPiecesCollections();
        fillPossibleCells();
        gameState = GameState.PLAYING;
    }

    private void fillPiecesCollections() {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                Coordinate cord = Coordinate.createFromInner(i, j);
                IChessPiece piece = board.getPiece(cord);
                if (piece != null) {
                    chessPieces.add(piece);
                    piece.setCoordinate(cord);
                }
            }
        }
    }

    private void fillPossibleCells() {
        fillPossibleCells(true);
        fillPossibleCells(false);
        checkMateAndRemoveCells();
    }

    private void fillPossibleCells(boolean toGo) {
        for (IChessPiece piece : chessPieces) {
            piece.setPossibleCells(board, toGo);
        }
    }

    @Override
    public boolean clickCell(ICoordinate cord) {
        if (locked || gameState == GameState.CHECKMATE || gameState == GameState.STALEMATE) {
            return false;
        }
        IChessPiece now = board.getPiece(cord);
        if (now != null && now.getColor() == nowTurn) {
            removeSelectedPiece(getSelectedCord());
            setSelectedPiece(cord);
        } else if (selectedPiece != null && (selectedPiece.getPossibleGoCells().contains(cord) ||
                selectedPiece.getPossibleCaptureCells().contains(cord))) {
            return goTo(cord);
        }
        return false;
    }

    private void setSelectedPiece(ICoordinate cord) {
        selectedPiece = board.getPiece(cord);
        setCellsType(true, getSelectedCord());
    }

    private void setCellsType(boolean select, ICoordinate cord) {
        BoardCell.CellType type = select ? BoardCell.CellType.REACHABLE : BoardCell.CellType.DEFAULT;
        for (ICoordinate c : selectedPiece.getPossibleGoCells()) {
            board.getCell(c).setCellType(type);
        }
        if (select) {
            type = BoardCell.CellType.CAPTURABLE;
            board.getCell(cord).setCellType(BoardCell.CellType.SELECTED);
        } else {
            board.getCell(cord).setCellType(BoardCell.CellType.DEFAULT);
        }
        for (ICoordinate c : selectedPiece.getPossibleCaptureCells()) {
            board.getCell(c).setCellType(type);
        }
    }

    private void removeSelectedPiece(ICoordinate previousCord) {
        if (selectedPiece == null)
            return;
        setCellsType(false, previousCord);
        selectedPiece = null;
    }

    private void movePiece(ICoordinate from, ICoordinate to) {
        enPassant = null;
        board.setEnPassantCord(null);
        BoardCell toCell = board.getCell(to);
        if (toCell.getPiece() != null) {
            chessPieces.remove(toCell.getPiece());
        }
        BoardCell fromCell = board.getCell(from);
        toCell.setPiece(fromCell.getPiece());
        fromCell.getPiece().setCoordinate(to);
        fromCell.setPiece(null);
    }

    private boolean goTo(ICoordinate to) {
        if (to.equals(board.getEnPassantCord())) {
            board.getCell(enPassant.getCoordinate()).setPiece(null);
            chessPieces.remove(enPassant);
        }
        ICoordinate from = selectedPiece.getCoordinate();
        movePiece(from, to);
        if (selectedPiece instanceof Pawn) {
            if (pawnMoved((Pawn) selectedPiece, from))
                return true;
        }
        removeSelectedPiece(from);
        startNewMove();
        return false;
    }

    private void startNewMove() {
        nowTurn = (nowTurn == ChessColor.WHITE) ? ChessColor.BLACK : ChessColor.WHITE;
        fillPossibleCells();
        checkState();
    }

    private void checkState() {
        gameState = GameState.PLAYING;
        if (isInCheck()) {
            gameState = checkMateAndRemoveCells() ? GameState.CHECKMATE : GameState.CHECK;
        } else if (isInStaleMate()) {
            gameState = GameState.STALEMATE;
        }
    }

    private boolean isInStaleMate() {
        for (IChessPiece piece : chessPieces) {
            if (piece.getColor() != nowTurn)
                continue;
            if (piece.getPossibleCaptureCells().size() > 0 || piece.getPossibleGoCells().size() > 0)
                return false;
        }
        return true;
    }

    private boolean isInCheck() {
        for (IChessPiece piece : chessPieces) {
            if (piece.getColor() != nowTurn && piece.getPossibleCaptureCells().contains(getNowKing().getCoordinate())) {
                return true;
            }
        }
        return false;
    }

    private boolean checkMateAndRemoveCells() {
        boolean result = true;
        Map<IChessPiece, Set<ICoordinate>> pieceToCaptureCellsMap = new HashMap<>();
        for (IChessPiece piece : chessPieces.toArray(new IChessPiece[0])) {
            if (piece.getColor() != nowTurn)
                continue;
            ICoordinate from = piece.getCoordinate();
            Iterator<ICoordinate> iterator = piece.getPossibleGoCells().iterator();
            while (iterator.hasNext()) {
                if (tryToMakeMove(from, iterator.next())) {
                    iterator.remove();
                } else {
                    result = false;
                }
            }
            HashSet<ICoordinate> captureSet = new HashSet<>();
            pieceToCaptureCellsMap.put(piece, captureSet);
            for (ICoordinate cord : piece.getPossibleCaptureCells()) {
                if (!tryToMakeMove(from, cord)) {
                    result = false;
                    captureSet.add(cord);
                }
            }

        }
        for (IChessPiece piece : chessPieces) {
            if (piece.getColor() == nowTurn) {
                ((ChessPiece) piece).setPossibleCaptureCells(pieceToCaptureCellsMap.get(piece));
            }
        }
        return result;
    }

    private boolean tryToMakeMove(ICoordinate from, ICoordinate to) {
        IChessPiece toPiece = board.getCell(to).getPiece();
        Pawn enPassant = this.enPassant;
        ICoordinate enPasCord = this.board.getEnPassantCord();
        movePiece(from, to);
        fillPossibleCells(false);
        boolean result = isInCheck();
        movePiece(to, from);
        if (toPiece != null) {
            board.getCell(to).setPiece(toPiece);
            chessPieces.add(toPiece);
        }
        this.enPassant = enPassant;
        this.board.setEnPassantCord(enPasCord);
        fillPossibleCells(false);
        return result;
    }

    private boolean pawnMoved(Pawn pawn, ICoordinate fromCord) {
        Coordinate from = (Coordinate) fromCord;
        Coordinate cord = (Coordinate) pawn.getCoordinate();
        if (pawn.hasNotMoved()) {
            if (cord.distanceFrom(from) == 2) {
                enPassant = pawn;
                board.setEnPassantCord(from.middleWith(cord));
            }
            if (Math.abs(from.getQ()) <= Math.abs(cord.getQ())) {
                pawn.moved();
            }
        }
        int y = -cord.getR() - cord.getQ();
        if (((cord.getR() + cord.getQ() == -5 || cord.getQ() + y == 5) && pawn.getColor() == ChessColor.WHITE) ||
                ((cord.getR() + cord.getQ() == 5 || cord.getQ() + y == -5) && pawn.getColor() == ChessColor.BLACK)) {
            locked = true;
            promotingCord = cord;
            removeSelectedPiece(from);
            return true;
        }
        return false;
    }

    @Override
    public boolean promotePawn(IChessPiece piece) {
        if (!locked || piece instanceof Pawn || piece instanceof King) {
            return false;
        }
        piece.setCoordinate(promotingCord);
        board.getCell(promotingCord).setPiece(piece);
        locked = false;
        startNewMove();
        return true;
    }

    private ICoordinate getSelectedCord() {
        return (selectedPiece == null) ? Coordinate.EMPTY_CORD : selectedPiece.getCoordinate();
    }

    private King getNowKing() {
        return nowTurn == ChessColor.WHITE ? whiteKing : blackKing;
    }

    private void placeChess() {
        board.getCell(Coordinate.createFromInner(0, 4)).setPiece(new Pawn(ChessColor.BLACK));
        board.getCell(Coordinate.createFromInner(6, 9)).setPiece(new Pawn(ChessColor.WHITE));

        board.getCell(Coordinate.createFromInner(0, 3)).setPiece(new Rook(ChessColor.BLACK));
        board.getCell(Coordinate.createFromInner(1, 4)).setPiece(new Pawn(ChessColor.BLACK));
        board.getCell(Coordinate.createFromInner(6, 8)).setPiece(new Pawn(ChessColor.WHITE));
        board.getCell(Coordinate.createFromInner(7, 8)).setPiece(new Rook(ChessColor.WHITE));

        board.getCell(Coordinate.createFromInner(0, 2)).setPiece(new Knight(ChessColor.BLACK));
        board.getCell(Coordinate.createFromInner(2, 4)).setPiece(new Pawn(ChessColor.BLACK));
        board.getCell(Coordinate.createFromInner(6, 7)).setPiece(new Pawn(ChessColor.WHITE));
        board.getCell(Coordinate.createFromInner(8, 7)).setPiece(new Knight(ChessColor.WHITE));

        blackKing = new King(ChessColor.BLACK);
        board.getCell(Coordinate.createFromInner(0, 1)).setPiece(blackKing);

        board.getCell(Coordinate.createFromInner(3, 4)).setPiece(new Pawn(ChessColor.BLACK));
        board.getCell(Coordinate.createFromInner(6, 6)).setPiece(new Pawn(ChessColor.WHITE));

        whiteKing = new King(ChessColor.WHITE);
        board.getCell(Coordinate.createFromInner(9, 6)).setPiece(whiteKing);

        board.getCell(Coordinate.createFromInner(0, 0)).setPiece(new Bishop(ChessColor.BLACK));
        board.getCell(Coordinate.createFromInner(1, 1)).setPiece(new Bishop(ChessColor.BLACK));
        board.getCell(Coordinate.createFromInner(2, 2)).setPiece(new Bishop(ChessColor.BLACK));
        board.getCell(Coordinate.createFromInner(4, 4)).setPiece(new Pawn(ChessColor.BLACK));
        board.getCell(Coordinate.createFromInner(6, 5)).setPiece(new Pawn(ChessColor.WHITE));
        board.getCell(Coordinate.createFromInner(8, 5)).setPiece(new Bishop(ChessColor.WHITE));
        board.getCell(Coordinate.createFromInner(9, 5)).setPiece(new Bishop(ChessColor.WHITE));
        board.getCell(Coordinate.createFromInner(10, 5)).setPiece(new Bishop(ChessColor.WHITE));

        board.getCell(Coordinate.createFromInner(1, 0)).setPiece(new Queen(ChessColor.BLACK));
        board.getCell(Coordinate.createFromInner(4, 3)).setPiece(new Pawn(ChessColor.BLACK));
        board.getCell(Coordinate.createFromInner(7, 4)).setPiece(new Pawn(ChessColor.WHITE));
        board.getCell(Coordinate.createFromInner(10, 4)).setPiece(new Queen(ChessColor.WHITE));

        board.getCell(Coordinate.createFromInner(2, 0)).setPiece(new Knight(ChessColor.BLACK));
        board.getCell(Coordinate.createFromInner(4, 2)).setPiece(new Pawn(ChessColor.BLACK));
        board.getCell(Coordinate.createFromInner(8, 3)).setPiece(new Pawn(ChessColor.WHITE));
        board.getCell(Coordinate.createFromInner(10, 3)).setPiece(new Knight(ChessColor.WHITE));

        board.getCell(Coordinate.createFromInner(3, 0)).setPiece(new Rook(ChessColor.BLACK));
        board.getCell(Coordinate.createFromInner(4, 1)).setPiece(new Pawn(ChessColor.BLACK));
        board.getCell(Coordinate.createFromInner(9, 2)).setPiece(new Pawn(ChessColor.WHITE));
        board.getCell(Coordinate.createFromInner(10, 2)).setPiece(new Rook(ChessColor.WHITE));

        board.getCell(Coordinate.createFromInner(4, 0)).setPiece(new Pawn(ChessColor.BLACK));
        board.getCell(Coordinate.createFromInner(10, 1)).setPiece(new Pawn(ChessColor.WHITE));
    }
}