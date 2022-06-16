package com.tahakorkem.nerdle.util;

import com.tahakorkem.nerdle.data.Operator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Calculator {

    /**
     * Verilen materiksel ifadenin sonucunu hesaplar
     * ve tam sayı sonucunu döndürür.
     * Yapılan işlem geçerli olmalı,
     * sonucu sonlu bir sayı olmalı,
     * tam sayı olmalı
     * ve 0'dan büyük olmalıdır.
     *
     * @param input matematiksel sonucu hesaplanacak ifade
     * @return ifade geçerli ise verilen ifadenin sonucu,
     * geçersiz ise -1
     */
    public static int calculate(String input) {
        try {
            String operatorRegex = "[+*/-]";

            String[] operatorsRaw = findAllMatches(operatorRegex, input);
            String[] operandsRaw = input.split(operatorRegex);

            // operatörlerin listelerini oluştur
            List<Operator> operators = new ArrayList<>();
            for (String operator : operatorsRaw) {
                Operator o = Operator.findOperator(operator.charAt(0));
                operators.add(o);
            }

            // operandların listelerini oluştur
            List<Double> operands = new ArrayList<>();
            for (String operand : operandsRaw) {
                operands.add((double) Integer.parseInt(operand));
            }

            // işlem önceliğine göre opertatör kullan
            // önce çarpma ve bölme, sonra toplama ve çıkarma işlemleri yap
            evaluateOnlyAllowedOperators(operands, operators, Operator.MULTIPLY, Operator.DIVIDE);
            evaluateOnlyAllowedOperators(operands, operators, Operator.ADD, Operator.SUBTRACT);

            // işlemler yapıldıktan sonra tek bir sayı kalmış mı kontrol et
            if (operands.size() != 1) {
                throw new IllegalStateException("İfadenin sonucu tek bir sayı olmalıdır");
            }

            double result = operands.get(0);

            // sonunucun sonlu olup olmadığını kontrol et
            if (Double.isInfinite(result)) {
                throw new IllegalStateException("Sonuç çok büyük");
            }

            // sonuç bir tamsayı mı kontrol et
            if (result != (int) result) {
                throw new IllegalStateException("Sonuç bir tamsayı olmalıdır");
            }

            // sonuç 0'dan büyük mü kontrol et
            if (result < 0) {
                throw new IllegalStateException("Sonuç 0'dan büyük olmalıdır");
            }

            return (int) result;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * Verilen regex ile ifadenin tüm eşleşenlerini döndürür
     * */
    private static String[] findAllMatches(String regex, String input) {
        List<String> allMatches = new ArrayList<>();
        Matcher m = Pattern.compile(regex).matcher(input);
        while (m.find()) {
            allMatches.add(m.group());
        }
        return allMatches.toArray(new String[0]);
    }

    /**
     * Verilen operantlar ve operatörleri üzerinde
     * sadece izin verilen operatörleri kullanarak işlem yapar
     * ve operant ve operatör listelerini günceller.
     *
     * @param operands         matematiksel ifadenin operandları
     * @param operators        matematiksel ifadenin operatörleri
     * @param allowedOperators matematiksel ifadenin çözümleyebileceği operatörler
     */
    private static void evaluateOnlyAllowedOperators(List<Double> operands, List<Operator> operators, Operator... allowedOperators) {
        int i = 0;
        while (i < operators.size()) {
            Operator operator = operators.get(i);

            if (Arrays.asList(allowedOperators).contains(operator)) {
                // izin verilen operatörlerden biri bulunursa
                // şu anki sayı ikilisi için gerekli işlemi yap
                double operand1 = operands.get(i);
                double operand2 = operands.get(i + 1);

                // işlem yapılır
                // operatör ve operandlar listeden çıkarılır
                // işlem sonucu operandlar listesine eklenir
                double r = operator.operation(operand1, operand2);
                operators.remove(i);
                operands.remove(i);
                operands.remove(i);
                operands.add(i, r);
            } else {
                // izin verilen operatörlerden biri bulunmamışsa
                // diğer sayı ikilisine geç
                i++;
            }
        }
    }

}