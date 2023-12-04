package ru.vsu.cs.skofenko.logic.service;

import ru.vsu.cs.skofenko.logic.api.IGameLogic;

import java.io.*;

public class GameLogicMapper {

    public static final String FILE_EXTENSION = ".chess";

    public static void saveGame(IGameLogic gameLogic, File file) throws Exception {
        try (FileOutputStream outputStream = new FileOutputStream(file);
             ObjectOutputStream objectWriter = new ObjectOutputStream(outputStream)) {
            objectWriter.writeObject(gameLogic);
        }
    }

    public static IGameLogic openGame(File file) throws Exception {
        try (FileInputStream inputStream = new FileInputStream(file);
             ObjectInputStream objectReader = new ObjectInputStream(inputStream)) {
            return (IGameLogic) objectReader.readObject();
        }
    }
}
