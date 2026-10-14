package cz.vspj.bmi;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.*;

class BmiCalculatorTest {
    @Test void formulaAndCentimetres() { assertEquals(22.857142857142858, BmiCalculator.calculate(175,70), 1e-12); }
    @Test void decimalWeightAndComma() { assertEquals(70.5, BmiCalculator.parseWeight(" 70,5 ")); assertEquals(70.5, BmiCalculator.parseWeight("70.5")); }
    @Test void integerHeight() { assertEquals(175, BmiCalculator.parseHeight(" 175 ")); }
    @ParameterizedTest @ValueSource(strings={"", " ", "0", "-175", "1.75", "175,5", "abc", "NaN", "Infinity", "1e2"})
    void invalidHeight(String value) { assertThrows(NumberFormatException.class, () -> BmiCalculator.parseHeight(value)); }
    @ParameterizedTest @ValueSource(strings={"", " ", "0", "-70", "abc", "NaN", "Infinity", "70,5.2", "1e3"})
    void invalidWeight(String value) { assertThrows(NumberFormatException.class, () -> BmiCalculator.parseWeight(value)); }
    @Test void nullInputs() { assertThrows(NumberFormatException.class, () -> BmiCalculator.parseHeight(null)); assertThrows(NumberFormatException.class, () -> BmiCalculator.parseWeight(null)); }
    @Test void veryLargeValues() { assertThrows(NumberFormatException.class, () -> BmiCalculator.parseHeight("9".repeat(400))); assertThrows(NumberFormatException.class, () -> BmiCalculator.parseWeight("9".repeat(400))); }
    @ParameterizedTest @CsvSource({"18.49,0", "18.5,1", "24.99,1", "25,2", "29.99,2", "30,3", "34.99,3", "35,4", "39.99,4", "40,5", "60,5"})
    void categoryBoundaries(double bmi, int category) { assertEquals(category, BmiCalculator.categoryIndex(bmi)); }
    @Test void immediatelyBeforeEachBoundary() { double[] edges = {18.5,25,30,35,40}; for (int i=0;i<edges.length;i++) assertEquals(i,BmiCalculator.categoryIndex(Math.nextDown(edges[i]))); }
    @Test void rejectsNonPositiveAndNonFiniteValues() {
        for (double value : new double[]{0,-1,Double.NaN,Double.POSITIVE_INFINITY,Double.NEGATIVE_INFINITY}) {
            assertThrows(NumberFormatException.class, () -> BmiCalculator.calculate(value,70));
            assertThrows(NumberFormatException.class, () -> BmiCalculator.calculate(175,value));
            assertThrows(NumberFormatException.class, () -> BmiCalculator.categoryIndex(value));
        }
    }
    @Test void rejectsOverflowAndUnderflow() {
        assertThrows(NumberFormatException.class, () -> BmiCalculator.calculate(Double.MIN_VALUE,70));
        assertThrows(NumberFormatException.class, () -> BmiCalculator.calculate(Double.MAX_VALUE,70));
    }
    @Test void acceptsFractionWithoutLeadingZero() { assertEquals(0.5,BmiCalculator.parseWeight(",5")); }
}
