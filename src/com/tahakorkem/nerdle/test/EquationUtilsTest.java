package com.tahakorkem.nerdle.test;

import com.tahakorkem.nerdle.util.EquationUtils;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * nerdle.util.EquationUtils class'ı için test sınıfı.
 * */
public class EquationUtilsTest {

    // 25.05.2022 Bu metot 7 ila 9 karakter uzunluğunda
    // içerisinde +,-,* ve / sembollerinden en az birisi olan
    // muhakkak = sembolü barındıran bir matematiksel ifade üretmektedir.

    private static String equation;

    @BeforeClass
    public static void generateEquation_setup() {
        equation = EquationUtils.generateEquation();
        System.out.println(equation);
    }

    @Test
    public void generateEquation_hasProperLength() {
        int minLength = 7;
        int maxLength = 9;
        assertTrue(equation.length() >= minLength && equation.length() <= maxLength);
    }

    @Test
    public void generateEquation_hasEqualsSign() {
        int firstIndex = equation.indexOf("=");
        assertTrue(firstIndex > 0);
        int lastIndex = equation.lastIndexOf("=");
        assertEquals(lastIndex, firstIndex);
    }

    @Test
    public void generateEquation_consistsOfValidCharacters() {
        assertTrue(equation.matches("[0-9=+*/-]+"));
    }

    @Test
    public void generateEquation_hasValidOperators() {
        assertTrue(equation.contains("+") || equation.contains("-") || equation.contains("*") || equation.contains("/"));
    }

    @Test
    public void generateEquation_correctlyGeneratesEquation() {
        assertTrue(EquationUtils.validateEquation(equation));
    }

}