package com.tahakorkem.nerdle.util;

public class TimeUtils {

    /**
     * Verilen zamanı saat ve dakika gösterimne çevirip geri döndürür.
     *
     * @param timeInSeconds saniye bazında zaman
     * @return saat ve dakika gösterimine sahip String
     */
    public static String format(int timeInSeconds) {
        int sec = timeInSeconds % 60;
        int min = timeInSeconds / 60;
        return String.format("%02d:%02d", min, sec);
    }

}
