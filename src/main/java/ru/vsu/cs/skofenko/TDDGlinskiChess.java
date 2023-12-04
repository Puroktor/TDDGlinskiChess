package ru.vsu.cs.skofenko;

import ru.vsu.cs.skofenko.ui.MainForm;

import java.util.Locale;

public class TDDGlinskiChess {

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> new MainForm(Locale.ENGLISH).setVisible(true));
    }
}
