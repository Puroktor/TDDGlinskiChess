package ru.vsu.cs.skofenko.ui;

import ru.vsu.cs.skofenko.logic.api.ChessColor;
import ru.vsu.cs.skofenko.logic.api.IChessPiece;
import ru.vsu.cs.skofenko.logic.chesspiece.*;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

class ChessPieceIcons {
    private static final Map<Class<? extends ChessPiece>, Map<ChessColor, Image>> map = new HashMap<>();

    static {
        @SuppressWarnings("unchecked")
        Class<? extends ChessPiece>[] pieces = new Class[]{King.class, Pawn.class, Queen.class, Knight.class, Bishop.class, Rook.class};
        String[] paths = new String[]{"/icon/WhiteKing.png", "/icon/BlackKing.png", "/icon/WhitePawn.png", "/icon/BlackPawn.png",
                "/icon/WhiteQueen.png", "/icon/BlackQueen.png", "/icon/WhiteKnight.png", "/icon/BlackKnight.png",
                "/icon/WhiteBishop.png", "/icon/BlackBishop.png", "/icon/WhiteRook.png", "/icon/BlackRook.png"};
        int i = 0;
        for (Class<? extends ChessPiece> type : pieces) {
            Map<ChessColor, Image> innerMap = new HashMap<>();
            for (ChessColor color : ChessColor.values()) {
                innerMap.put(color, new ImageIcon(ChessPieceIcons.class.getResource(paths[i++])).getImage());
            }
            map.put(type, innerMap);
        }
    }

    public static Image getIcon(IChessPiece piece) {
        return map.get(piece.getClass()).get(piece.getColor());
    }
}
