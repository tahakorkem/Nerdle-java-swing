package com.tahakorkem.nerdle.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Dört işlem operatörleri için enum
 */
public enum Operator {
    MULTIPLY('*') {
        @Override
        public double operation(double o1, double o2) {
            return o1 * o2;
        }
    },
    DIVIDE('/') {
        @Override
        public double operation(double o1, double o2) {
            return o1 / o2;
        }
    },
    ADD('+') {
        @Override
        public double operation(double o1, double o2) {
            return o1 + o2;
        }
    },
    SUBTRACT('-') {
        @Override
        public double operation(double o1, double o2) {
            return o1 - o2;
        }
    };
    private final char symbol;

    Operator(char symbol) {
        this.symbol = symbol;
    }

    public char getSymbol() {
        return symbol;
    }

    /**
     * İki sayı arasında yapılacak işlemi yapar ve geriye sonucu döndürür
     *
     * @param o1 ilk sayı
     * @param o2 ikinci sayı
     * @return işlem sonucu
     */
    public abstract double operation(double o1, double o2);

    /**
     * char olarak verilen operatörün
     * nerdle.data.Operator enum'ındaki değerini döndürür
     */
    public static Operator findOperator(char operatorRaw) {
        for (Operator operator : Operator.values()) {
            if (operator.getSymbol() == operatorRaw) {
                return operator;
            }
        }
        return null;
    }

    /**
     * Art arda girilmiş String olarak verilen operatörlerin
     * nerdle.data.Operator enum'ı dizisi değerini döndürür
     */
    public static Operator[] parseOperators(String operators) {
        List<Operator> list = new ArrayList<>();
        for(char operator : operators.toCharArray()) {
            Operator op = findOperator(operator);
            list.add(op);
        }
        return list.toArray(new Operator[0]);
    }
}
