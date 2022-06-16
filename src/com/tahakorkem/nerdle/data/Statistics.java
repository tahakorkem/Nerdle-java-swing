package com.tahakorkem.nerdle.data;

import com.tahakorkem.nerdle.util.TimeUtils;

import java.io.Serializable;

/**
 * Oyun istatistiklerini tutan sınıf.
 * */
public class Statistics implements Serializable {
    public static final String FILE_NAME = "stats";
    private static final long serialVersionUID = 2L;

    /**
     * Kaybedilen oyun sayısı.
     * */
    private int lostGameCount = 0;
    /**
     * Yarıda bırakılan oyun sayısı.
     * */
    private int interruptedGameCount = 0;
    /**
     * Kazanılan oyun sayısı.
     * */
    private int wonGameCount = 0;
    /**
     * Kazanılan oyunların ortalama süresi.
     * */
    private float wonGameTimeAverage = 0;
    /**
     * Kazanılan oyunların ortalama kaç satırda kazanıldığı.
     * */
    private float wonGameTryAverage = 0;

    public Statistics() {

    }

    public String getLostGameCount() {
        return String.valueOf(lostGameCount);
    }

    public String getInterruptedGameCount() {
        return String.valueOf(interruptedGameCount);
    }

    public String getWonGameCount() {
        return String.valueOf(wonGameCount);
    }

    public String getWonGameTimeAverage() {
        if (wonGameTimeAverage == 0) {
            return "??";
        }
        int avg = Math.round(wonGameTimeAverage);
        return TimeUtils.format(avg);
    }

    public String getWonGameTryAverage() {
        return wonGameTryAverage == 0 ? "??" : String.format("%.2f", wonGameTryAverage);
    }

    /**
     * İstatistiklere kaybedilen yeni bir oyun eklenir.
     * */
    public void insertLostGame() {
        this.lostGameCount++;
    }

    /**
     * İstatistiklere yarıda bırakılan yeni bir oyun eklenir.
     * */
    public void insertInterruptedGame() {
        this.interruptedGameCount++;
    }

    /**
     * İstatistiklere kazanılan yeni bir oyun eklenir.
     *
     * @param totalTryCount Oyunun toplam kaç satırda kazanıldığı.
     * @param totalElapsedTime Oyunda geçen toplam süre.
     *
     * */
    public void insertWonGame(int totalTryCount, int totalElapsedTime) {
        wonGameTryAverage = ((wonGameTryAverage * wonGameCount) + totalTryCount) / (wonGameCount + 1);
        wonGameTimeAverage = ((wonGameTimeAverage * wonGameCount) + totalElapsedTime) / (wonGameCount + 1);
        wonGameCount++;
    }

}
