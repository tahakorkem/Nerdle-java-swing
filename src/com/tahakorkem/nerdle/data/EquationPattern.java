package com.tahakorkem.nerdle.data;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Oluşturulacak denklem için kullanılacak olan kuralları tutan yardımcı sınıf.
 */
public class EquationPattern {

    private final int num1;
    private final Operator[] operator1;
    private final int num2;
    private final Operator[] operator2;
    private final int num3;
    private final int result;
    private final int operandCount;

    /**
     * İki operand ve bir operatör içeren denklemler için constructor.
     */
    public EquationPattern(int num1, Operator[] operator1, int num2, int result) {
        this.num1 = num1;
        this.operator1 = operator1;
        this.num2 = num2;
        this.operator2 = null;
        this.num3 = 0;
        this.result = result;
        this.operandCount = 2;
    }

    /**
     * Üç operand ve iki operatör içeren denklemler için constructor.
     */
    public EquationPattern(int num1, Operator[] operator1, int num2, Operator[] operator2, int num3, int result) {
        this.num1 = num1;
        this.operator1 = operator1;
        this.num2 = num2;
        this.operator2 = operator2;
        this.num3 = num3;
        this.result = result;
        this.operandCount = 3;
    }

    public int getNum1() {
        return num1;
    }

    public Operator[] getOperator1() {
        return operator1;
    }

    public int getNum2() {
        return num2;
    }

    public Operator[] getOperator2() {
        return operator2;
    }

    public int getNum3() {
        return num3;
    }

    public int getResult() {
        return result;
    }

    public int getOperandCount() {
        return operandCount;
    }

    /**
     * Verilen kalıba göre nerdle.data.EquationPattern objesi oluşturur ve döndürür.
     */
    public static EquationPattern fromPattern(String pattern) {
        String regex = "(?<num1>[?]+)\\[(?<op1>[+*/-]+)\\](?<num2>[?]+)(?:\\[(?<op2>[+*/-]+)\\](?<num3>[?]+))?=(?<result>[?]+)";
        Matcher m = Pattern.compile(regex).matcher(pattern);
        if (!m.find()) {
            return null;
        }

        // regexi kullanarak kuralları ayrıştır
        String num1 = m.group("num1");
        String operator1 = m.group("op1");
        String num2 = m.group("num2");
        String operator2 = m.group("op2");
        String num3 = m.group("num3");
        String result = m.group("result");

        // iki operand için nerdle.data.EquationPattern objesi oluştur ve döndür
        if (operator2 == null || num3 == null) {
            return new EquationPattern(num1.length(), Operator.parseOperators(operator1), num2.length(), result.length());
        }

        // üç operand için nerdle.data.EquationPattern objesi oluştur ve döndür
        return new EquationPattern(num1.length(), Operator.parseOperators(operator1), num2.length(), Operator.parseOperators(operator2), num3.length(), result.length());
    }
}
