package ru.vsu.cs.skofenko.logic.api.board;

import lombok.Data;
import ru.vsu.cs.skofenko.logic.api.IChessPiece;

import java.io.Serializable;

@Data
public class BoardCell implements Serializable {
    public enum CellType {
        DEFAULT,
        REACHABLE,
        CAPTURABLE,
        SELECTED
    }

    private IChessPiece piece;

    private CellType cellType = CellType.DEFAULT;
}
