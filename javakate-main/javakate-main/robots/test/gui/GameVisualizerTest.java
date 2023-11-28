package gui;

import org.junit.jupiter.api.Test;

import java.util.Random;

import static gui.Geom.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class GameVisualizerTest {

    public static final double ANGLE_DELTA = 0.123;
    // Погрешность вычислений
    private static final double EPS = 0.00001;

    @Test
    void testDistance() {
        assertEquals(5.0, distance(0, 0, 3, 4), 0.001);
    }

    @Test
    void testAngleTo() {
        assertEquals(Math.PI / 4, angleTo(0, 0, 1, 1), 0.001);
    }

    @Test
    void roundPositiveNumbers() {
        assertEquals(5, round(4.5));
        assertEquals(2, round(1.6));
        assertEquals(2, round(2.3));
        assertEquals(3, round(2.6));
    }

    @Test
    void roundNegativeNumbers() {
        assertEquals(-4, round(-4.5));
        assertEquals(-2, round(-1.6));
        assertEquals(-2, round(-2.3));
    }

    @Test
    void roundZero() {
        assertEquals(0, round(0));
    }

    @Test
    void testAngleNormalize() {
        // Проверяем отрицательный угол.
        // Отступаем случайное число окружностей по часовой (в отрицательную сторону)
        Random rand = new Random();
        int circles = rand.nextInt(10) + 2;
        // Одна окружность в радианах
        double oneCircleInRadians = 2 * Math.PI;
        double negativeAngle = -oneCircleInRadians * circles + ANGLE_DELTA;
        assertEquals(ANGLE_DELTA, asNormalizedRadians(negativeAngle), EPS);
        // Отступаем в положительную сторону
        double positiveAngle = oneCircleInRadians * circles + ANGLE_DELTA;
        assertEquals(ANGLE_DELTA, asNormalizedRadians(positiveAngle), EPS);
    }
}
