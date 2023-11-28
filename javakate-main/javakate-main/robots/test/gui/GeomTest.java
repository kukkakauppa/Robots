package gui;

import org.junit.jupiter.api.Test;

import static gui.Geom.*;
import static java.lang.Math.PI;
import static java.lang.Math.cos;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class GeomTest {

    @Test
    void testDistanceAtOrigin() {
        assertEquals(0, distance(0, 0, 0, 0));
    }

    @Test
    void testDistancePositiveNumbers() {
        assertEquals(5, distance(0, 0, 3, 4));
    }

    @Test
    void testDistanceNegativeNumbers() {
        assertEquals(5, distance(-3, -4, 0, 0));
    }

    @Test
    void testDistanceWithDifferentPoints() {
        assertEquals(Math.sqrt(2), distance(1, 1, 2, 2));
    }

    @Test
    void testAngleToRightQuadrant() {
        assertEquals(0, angleTo(1, 0, 2, 0));
    }

    @Test
    void testAngleToTopQuadrant() {
        assertEquals(PI / 2, angleTo(0, 1, 0, 2));
    }

    @Test
    void testAngleToWithNegativeNumbers() {
        assertEquals(PI, angleTo(-1, 0, -2, 0));
    }

    @Test
    void testAngleToFullCircle() {
        assertEquals(0, angleTo(1, 0, 1 + cos(2 * PI), 0 + Math.sin(2 * PI)));
    }

    @Test
    void testApplyLimitsBelowRange() {
        assertEquals(1, applyLimits(0, 1, 10));
    }

    @Test
    void testApplyLimitsAboveRange() {
        assertEquals(10, applyLimits(11, 1, 10));
    }

    @Test
    void testApplyLimitsWithinRange() {
        assertEquals(5, applyLimits(5, 1, 10));
    }

    @Test
    void testAsNormalizedRadiansNegative() {
        assertEquals(PI, asNormalizedRadians(-PI));
    }

    @Test
    void testAsNormalizedRadiansMoreThanTwoPI() {
        assertEquals(0, asNormalizedRadians(2 * PI));
    }

    @Test
    void testAsNormalizedRadiansJustLessThanTwoPI() {
        assertEquals(PI, asNormalizedRadians(3 * PI));
    }

    @Test
    void testAsNormalizedRadiansStandard() {
        assertEquals(PI / 2, asNormalizedRadians(PI / 2));
    }

    @Test
    void testRoundPositive() {
        assertEquals(2, round(1.5));
    }

    @Test
    void testRoundNegative() {
        assertEquals(-2, round(-1.6));
    }

    @Test
    void testRoundUp() {
        assertEquals(3, round(2.5));
    }

    @Test
    void testRoundDown() {
        assertEquals(-3, round(-2.6));
    }
}
