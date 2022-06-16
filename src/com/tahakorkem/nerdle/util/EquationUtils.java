package com.tahakorkem.nerdle.util;

import com.tahakorkem.nerdle.data.Operator;
import com.tahakorkem.nerdle.data.EquationPattern;

import java.util.HashMap;
import java.util.Map;

public class EquationUtils {

    /**
     * Verilen denklemin geçerli olup olmadığını kontrol eder.
     *
     * @param equation geçerliliği kontrol edilecek denklem
     * @return geçerli ise true, değilse false
     */
    public static boolean validateEquation(String equation) {
        // denklemde sadece bir tane '=' karakteri olmalı
        int firstIndex = equation.indexOf("=");
        if (firstIndex < 0) {
            return false;
        }
        int lastIndex = equation.lastIndexOf("=");
        if (lastIndex != firstIndex) {
            return false;
        }
        // denklemi '=' karakterinden ikiye böl
        String[] equationParts = equation.split("=");
        if (equationParts.length != 2) {
            return false;
        }
        String leftPart = equationParts[0];
        String rightPart = equationParts[1];

        // denklemin sol kısmı için hesaplama yapılır
        int result = Calculator.calculate(leftPart);
        if (result == -1) {
            return false;
        }

        // denklemin sağ kısmı sadece sayısal karakterler içermeli
        if (!rightPart.matches("\\d+")) {
            return false;
        }

        // denklemin sağ kısmının sayısal değeri ile denklemin sol kısmının hesaplanan değeri eşit olmalı
        int rightPartResult = Integer.parseInt(rightPart);
        if (rightPartResult != result) {
            return false;
        }

        return true;
    }

    /**
     * 7 ila 9 karakter arasında rastgele bir denklem üretir.
     *
     * @return üretilen denklem
     */
    public static String generateEquation() {
        // 7 ila 9 arasında rastgele bir sayı üret
        int length = generateRandomNumber(7, 9);
        switch (length) {
            case 7:
                return generateEquation7();
            case 8:
                return generateEquation8();
            case 9:
                return generateEquation9();
        }
        return null;
    }

    /**
     * 7 haneli denklem üretir.
     *
     * @return üretilen denklem
     */
    private static String generateEquation7() {
        // 1 ila 4 arasında rastgele bir sayı üret
        int randomNumber = generateRandomNumber(1, 4);
        switch (randomNumber) {
            case 1:
                return generateEquation7_1_1();
            case 2:
                return generateEquation7_1_2();
            case 3:
                return generateEquation7_2_1();
            case 4:
                return generateEquation7_2_2();
        }
        return null;
    }

    private static String generateEquation7_1_1() {
        return generateEquationCorrespondingPattern("?[+-*/]?[+-*/]?=?");
    }

    private static String generateEquation7_1_2() {
        return generateEquationCorrespondingPattern("??[/-]??=?");
    }

    private static String generateEquation7_2_1() {
        return generateEquationCorrespondingPattern("?[+*]??=??");
    }

    private static String generateEquation7_2_2() {
        return generateEquationCorrespondingPattern("??[+*/-]?=??");
    }

    /**
     * 8 karakterden oluşan denklemleri üretir.
     *
     * @return üretilen denklem
     */
    private static String generateEquation8() {
        // 1 ila 4 arasında rastgele bir sayı üret
        int randomNumber = generateRandomNumber(1, 4);
        switch (randomNumber) {
            case 1:
                return generateEquation8_1_1();
            case 2:
                return generateEquation8_1_2();
            case 3:
                return generateEquation8_2_1();
            case 4:
                return generateEquation8_2_2();
        }
        return null;
    }

    private static String generateEquation8_1_1() {
        return generateEquationCorrespondingPattern("?[+*]??[+/-]?=?");
    }

    private static String generateEquation8_1_2() {
        return generateEquationCorrespondingPattern("??[+/-]?[+*/-]?=?");
    }

    private static String generateEquation8_2_1() {
        return generateEquationCorrespondingPattern("?[+*/-]?[+*/-]?=??");
    }

    private static String generateEquation8_2_2() {
        return generateEquationCorrespondingPattern("??[+-]??=??");
    }

    /**
     * 9 karakterden oluşan denklemleri üretir.
     *
     * @return üretilen denklem
     */
    private static String generateEquation9() {
        // 1 ila 6 arasında rastgele bir sayı üret
        int randomNumber = generateRandomNumber(1, 6);
        switch (randomNumber) {
            case 1:
                return generateEquation9_1_1();
            case 2:
                return generateEquation9_1_2();
            case 3:
                return generateEquation9_1_3();
            case 4:
                return generateEquation9_2_1();
            case 5:
                return generateEquation9_2_2();
            case 6:
                return generateEquation9_2_3();
        }
        return null;
    }

    private static String generateEquation9_1_1() {
        return generateEquationCorrespondingPattern("?[+*]??[+/-]??=?");
    }

    private static String generateEquation9_1_2() {
        return generateEquationCorrespondingPattern("??[+/-]?[+*-]??=?");
    }

    private static String generateEquation9_1_3() {
        return generateEquationCorrespondingPattern("??[+/-]??[+*/-]?=?");
    }

    private static String generateEquation9_2_1() {
        return generateEquationCorrespondingPattern("?[+*/-]?[+*-]??=??");
    }

    private static String generateEquation9_2_2() {
        return generateEquationCorrespondingPattern("?[+*]??[+*/-]?=??");
    }

    private static String generateEquation9_2_3() {
        return generateEquationCorrespondingPattern("??[+*/-]?[+*/-]?=??");
    }

    /**
     * generateEquationCorrespondingPattern isimli metot overload edilmiştir.
     */
    private static String generateEquationCorrespondingPattern(String pattern) {
        EquationPattern equationPattern = EquationPattern.fromPattern(pattern);
        return generateEquationCorrespondingPattern(equationPattern);
    }

    /**
     * Verilen kalıba uygun bir denklem üretir.
     * Eğer kalıbın uygunluğu sağlanmazsa,
     * aynı kalıpta rekürsif olarak yeni bir denklem üretir.
     */
    private static String generateEquationCorrespondingPattern(EquationPattern pattern) {
        String input;

        // birinci sayı üretilir
        int num1DigitCount = pattern.getNum1();
        int num1 = generateNDigitRandomNumber(num1DigitCount);

        // birinci operatör üretilir
        Operator operator1 = chooseRandomly(pattern.getOperator1());

        // ikinci sayı üretilir
        int num2DigitCount = pattern.getNum2();
        int num2 = generateNDigitRandomNumber(num2DigitCount);

        // num1 ve num2 sayıları için optimizasyon yapılır
        NumberTuple numberTuple = new NumberTuple(num1, num1DigitCount, num2, num2DigitCount, operator1);
        optimize(numberTuple);
        num1 = numberTuple.getNum1();
        num2 = numberTuple.getNum2();

        if (pattern.getOperandCount() == 3) {
            // ikinci operatör üretilir
            Operator operator2 = chooseRandomly(pattern.getOperator2());

            // üçüncü sayı üretilir
            int num3DigitCount = pattern.getNum3();
            int num3 = generateNDigitRandomNumber(num3DigitCount);

            // num2 ve num3 sayıları için optimizasyon yapılır
            numberTuple.setTuple(num2, num2DigitCount, num3, num3DigitCount, operator2);
            optimize(numberTuple);
            num2 = numberTuple.getNum1();
            num3 = numberTuple.getNum2();

            input = String.format("%d%c%d%c%d", num1, operator1.getSymbol(), num2, operator2.getSymbol(), num3);
        } else {
            input = String.format("%d%c%d", num1, operator1.getSymbol(), num2);
        }
        System.out.println(input);
        int result = Calculator.calculate(input);
        if (result == -1) {
            return generateEquationCorrespondingPattern(pattern);
        }
        if (!checkIfNDigit(result, pattern.getResult())) {
            return generateEquationCorrespondingPattern(pattern);
        }
        return String.format("%s=%d", input, result);
    }

    /**
     * Verilen sayı ikilisi için optimizasyon yapılır.
     * Optimizasyon için bölme ve çıkarma işlemleri
     * dikkate alınır.
     */
    private static void optimize(NumberTuple tuple) {
        if (tuple.operator == Operator.DIVIDE) {
            // sayı ikilisi arasında bölme işlemi yapılacaksa

            // num1 num2'den küçükse yerlerini değiştir
            if (tuple.num1 < tuple.num2) {
                tuple.swap();
            }

            // num1'in num2'ye tam bölünüp bölünmediğini kontrol et
            if (tuple.num1 % tuple.num2 != 0) {
                // ya birinci sayı ikinciye bölünecek şekilde değiştirilir
                // ya da ikinci sayı birinciyi bölecek şekilde değiştirilir

                int dividend1 = (tuple.num1 / tuple.num2) * tuple.num2;
                int dividend2 = dividend1 + tuple.num2;
                int divisor = tuple.num2 - (tuple.num1 % tuple.num2);

                if (checkIfNDigit(dividend2, tuple.num1DigitCount)) {
                    tuple.num1 = dividend2;
                } else if (checkIfNDigit(dividend1, tuple.num1DigitCount)) {
                    tuple.num1 = dividend1;
                } else if (checkIfNDigit(divisor, tuple.num2DigitCount)) {
                    tuple.num2 = divisor;
                }
            }
            System.out.println("after optimize: " + tuple.num1 + " " + tuple.num2);
        } else if (tuple.operator == Operator.SUBTRACT) {
            // sayı ikilisi arasında çıkarma işlemi yapılacaksa
            // num1 num2'den küçükse yerlerini değiştir
            if (tuple.num1 < tuple.num2) {
                tuple.swap();
            }
        }
    }

    /**
     * Verilen sayının belirtilen basamakta olup olmadığını kontrol eder.
     *
     * @param num kontrol edilecek sayı
     * @param n   basamak sayısı
     * @return sayı belirtilen basamakta ise true, değilse false
     */
    private static boolean checkIfNDigit(int num, int n) {
        int max = (int) Math.pow(10, n) - 1;
        int min = (int) Math.pow(10, n - 1);
        return num >= min && num <= max;
    }

    /**
     * Verilen değerde basamağa sahip rastgele bir sayı üretir.
     *
     * @param n basamak değeri
     * @return rastgele sayı
     */
    private static int generateNDigitRandomNumber(int n) {
        int max = (int) Math.pow(10, n) - 1;
        int min = (int) Math.pow(10, n - 1);
        return generateRandomNumber(min, max);
    }

    /**
     * Verilen sayı aralığında rastgele bir sayı üretir ve geriye döndürür.
     *
     * @param min aralığın başlangıç değeri
     * @param max aralığın bitiş değeri
     * @return aralığın içinde olan rastgele bir sayı
     */
    private static int generateRandomNumber(int min, int max) {
        return (int) (Math.random() * (max - min) + min);
    }

    /**
     * Verilen öğelerden rastgele bir öğe seçip geri döndürür.
     *
     * @param items rastgele seçim yapılacak öğeler
     * @return rastgele seçilen öğe
     */
    @SafeVarargs
    private static <T> T chooseRandomly(T... items) {
        int randomIndex = (int) (Math.random() * items.length);
        return items[randomIndex];
    }

    /**
     * Verilen denklemdeki tüm karakterlerin sıklıklarını hesaplar ve döndürür.
     *
     * @param equation denklem
     * @return karakterlerin sıklıklarılarını içeren bir map
     */
    public static Map<Character, Integer> generateFrequencyTable(String equation) {
        Map<Character, Integer> table = new HashMap<>();
        for (char c : equation.toCharArray()) {
            table.put(c, table.getOrDefault(c, 0) + 1);
        }
        return table;
    }

    /**
     * Sayı ikilileri oluşturmak için kullanılan yardımcı sınıf.
     */
    private static class NumberTuple {
        private int num1;
        private int num1DigitCount;
        private int num2;
        private int num2DigitCount;
        private Operator operator;

        NumberTuple(int num1, int num1DigitCount, int num2, int num2DigitCount, Operator operator) {
            this.num1 = num1;
            this.num1DigitCount = num1DigitCount;
            this.num2 = num2;
            this.num2DigitCount = num2DigitCount;
            this.operator = operator;
        }

        public void setTuple(int num1, int num1DigitCount, int num2, int num2DigitCount, Operator operator) {
            this.num1 = num1;
            this.num1DigitCount = num1DigitCount;
            this.num2 = num2;
            this.num2DigitCount = num2DigitCount;
            this.operator = operator;
        }

        public int getNum1() {
            return num1;
        }

        public int getNum2() {
            return num2;
        }

        /**
         * İki sayıyı yer değiştirir.
         */
        public void swap() {
            // sayıları değiştir
            int temp = num1;
            num1 = num2;
            num2 = temp;
            // basamak değerlerini değiştirir
            temp = num1DigitCount;
            num1DigitCount = num2DigitCount;
            num2DigitCount = temp;
        }
    }
}
