package com.tahakorkem.nerdle.test;

import com.tahakorkem.nerdle.util.Calculator;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * nerdle.Calculator class'ı için test sınıfı.
 * */
public class CalculatorTest {

    @Test
    public void testAdd() {
        String input = "1+2";
        Assert.assertEquals(3, Calculator.calculate(input));
    }

    @Test
    public void testSubtract() {
        String input = "2-1";
        assertEquals(1, Calculator.calculate(input));
    }

    @Test
    public void testMultiply() {
        String input = "2*3";
        assertEquals(6, Calculator.calculate(input));
    }

    @Test
    public void testDivide() {
        String input = "6/2";
        assertEquals(3, Calculator.calculate(input));
    }

    @Test
    public void testDivideByZero() {
        String input = "6/0";
        assertEquals(-1, Calculator.calculate(input));
    }

    @Test
    public void testComplex1() {
        String input = "1+2*3";
        assertEquals(7, Calculator.calculate(input));
    }

    @Test
    public void testComplex2() {
        String input = "1+2*3-4";
        assertEquals(3, Calculator.calculate(input));
    }

    @Test
    public void testComplex3() {
        String input = "1+2*3-4/2";
        assertEquals(5, Calculator.calculate(input));
    }

    @Test
    public void testDoubleOperandsIntegerResult() {
        String input = "7/2*6";
        assertEquals(21, Calculator.calculate(input));
    }

    @Test
    public void testDoubleOperandsNonIntegerResult() {
        String input = "7/2+9";
        assertEquals(-1, Calculator.calculate(input));
    }

    @Test
    public void testNegativeResult() {
        String input = "1-5";
        assertEquals(-1, Calculator.calculate(input));
    }

}