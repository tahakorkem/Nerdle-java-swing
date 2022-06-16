package com.tahakorkem.nerdle.manager;

import com.tahakorkem.nerdle.data.Statistics;

import java.io.*;

/**
 * Dosya kaydetme ve okuma işlemlerini yapan sınıf.
 * */
public class StorageManager {

    private Statistics stats;
    private GameManager savedGame;

    public StorageManager() {
        try {
            // kayıtlı oyun bilgisi alınır
            readSavedGame();
        } catch (IOException | ClassNotFoundException e) {
            // kayıtlı oyun yok ya da başka bir hata oluştu
            e.printStackTrace();
        }
        try {
            // kayıtlı istatistik verisi alınır
            readSavedStats();
        } catch (IOException | ClassNotFoundException e) {
            // henüz kayıtlı istatistik verisi yok ya da başka bir hata oluştu
            // bu durumda başlangıç değerli bir nerdle.data.Statistics objesi oluşturulur
            stats = new Statistics();
            e.printStackTrace();
        }
    }

    public Statistics getStats() {
        return stats;
    }

    public boolean hasSavedGame() {
        return savedGame != null;
    }

    public GameManager getSavedGame() {
        return savedGame;
    }

    /**
     * Kayıtlı oyun bilgisi okunur
     * */
    private void readSavedGame() throws IOException, ClassNotFoundException {
        FileInputStream fileInputStream = new FileInputStream(GameManager.FILE_NAME);
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
        savedGame = (GameManager) objectInputStream.readObject();
        objectInputStream.close();
    }

    /**
     * Kayıtlı istatistik bilgisi okunur
     * */
    private void readSavedStats() throws IOException, ClassNotFoundException {
        FileInputStream fileInputStream = new FileInputStream(Statistics.FILE_NAME);
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
        stats = (Statistics) objectInputStream.readObject();
        objectInputStream.close();
    }

    /**
     * Oyun kaydedilir
     * */
    public void saveGame(GameManager game) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(GameManager.FILE_NAME);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
        objectOutputStream.writeObject(game);
        objectOutputStream.flush();
        objectOutputStream.close();
    }

    /**
     * İstatistik bilgileri kaydedilir
     * */
    public void saveStats() throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(Statistics.FILE_NAME);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
        objectOutputStream.writeObject(stats);
        objectOutputStream.flush();
        objectOutputStream.close();
    }

}
