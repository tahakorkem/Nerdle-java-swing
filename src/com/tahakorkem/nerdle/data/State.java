package com.tahakorkem.nerdle.data;

/**
 * Hücre etiketlerini için enum.
 * */
public enum State {
    /**
     * Hücrenin başlangıç etiketi.
     * */
    INITIAL,
    /**
     * Hücrenin doğru tahmin edilmesi durumu.
     * */
    CORRECT,
    /**
     * Hücrenin kısmen doğru tahmin edilmesi durumu.
     * */
    PARTIALLY_CORRECT,
    /**
     * Hücrenin yanlış tahmin edilmesi durumu.
     * */
    INCORRECT
}
