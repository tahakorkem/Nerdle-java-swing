package com.tahakorkem.nerdle.ui;

import com.tahakorkem.nerdle.manager.GameManager;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Oyunun zamanını, oyunun devam ettiği her saniye artıran sınıf.
 * */
public class Chronometer extends TimerTask {

    private final GameManager gameManager;
    private final Timer timer;

    public Chronometer(GameManager gameManager, Timer timer) {
        this.gameManager = gameManager;
        this.timer = timer;
    }

    @Override
    public void run() {
        // oyun bitince kronometreyi durdur
        if(!gameManager.isInGame()) {
            timer.cancel();
            return;
        }
        // oyun zamanını bir saniye artır
        gameManager.setTimeInSeconds(gameManager.getTimeInSeconds() + 1);
    }

}
