package com.tahakorkem.nerdle.ui;

import com.tahakorkem.nerdle.data.State;

/**
 * Oyun oynandığında tetiklenen olayların tutulduğu callback interface'i.
 */
public interface GameListener {
    /**
     * Belirli bir hücrenin etitet durumu değiştiğinde tetiklenir.
     *
     * @param row      hücrenin satır numarası
     * @param column   hücrenin sütun numarası
     * @param newState hücrenin yeni etitet durumu
     */
    void onStateChange(int row, int column, State newState);

    /**
     * Belirli bir hücrenin seçili olup olmadığı durumu değiştiğinde tetiklenir.
     *
     * @param row        hücrenin satır numarası
     * @param column     hücrenin sütun numarası
     * @param isSelected hücrenin seçili olup olmadığı bilgisi
     */
    void onCellSelectionChange(int row, int column, boolean isSelected);

    /**
     * Belirli bir hücrenin içindeki değer değiştiğinde tetiklenir.
     *
     * @param row     hücrenin satır numarası
     * @param column  hücrenin sütun numarası
     * @param newText hücrenin yeni değeri
     */
    void onTextChange(int row, int column, char newText);

    /**
     * Oyun zamanında bir değişiklik olduğunda tetiklenir.
     *
     * @param newTimeInSeconds oyunun saniye bazında yeni zamanı
     */
    void onTimeChange(int newTimeInSeconds);

    /**
     * Kullanıcıya bir uyarı mesajı gösterilmek istendiğinde tetiklenir.
     *
     * @param message uyarı mesajı
     */
    void onAlert(String message);

    /**
     * Kulanıcı oyunu kazandığında tetiklenir.
     *
     * @param totalTryCount    yapılan toplam deneme sayısı
     * @param totalElapsedTime oyunun saniye bazında toplam zamanı
     */
    void onWin(int totalTryCount, int totalElapsedTime);

    /**
     * Kullanıcı oyunu kaybedince tetiklenir.
     *
     * @param actualEquation kullanıcın tahmin edemediği asıl denklem
     */
    void onLose(String actualEquation);
}
