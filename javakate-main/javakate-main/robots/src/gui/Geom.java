package gui;

import static java.lang.Math.*;

public class Geom {
    // Декартового расстояние между двумя точками на плоскости:
    // (x1, y1) и (x2, y2)
    public static double distance(double x1, double y1, double x2, double y2) {
        double diffX = x1 - x2; // Разница координат по X
        double diffY = y1 - y2; // Разница координат по Y
        return sqrt(diffX * diffX + diffY * diffY); // Корень из суммы квадратов разницы координат
    }

    // Угол между двумя векторами заданными точками на плоскости:
    // (fromX, fromY) и (toX, toY)
    public static double angleTo(double fromX, double fromY, double toX, double toY) {
        double diffX = toX - fromX;
        double diffY = toY - fromY;
        return asNormalizedRadians(atan2(diffY, diffX));
    }

    // Перевод угла в радианах в диапазон от 0 до 2-х ПИ
    public static double asNormalizedRadians(double angle) {
        while (angle < 0) angle += 2 * PI;
        while (angle >= 2 * PI) angle -= 2 * PI;
        return angle;
    }

    // Перевод угла в радианах в диапазон от -ПИ до ПИ
    // Эта функция переводит угол в радианах в диапазон от -PI до +PI
    // Чтобы правильно работало сравнение направления робота и на цель
    public static double PIRadians(double angle) {
        while (angle < -PI) angle += 2 * PI;
        while (angle >= PI) angle -= 2 * PI;
        return angle;
    }

    // Если какая-то величина v меньше минимального значения, то возвращаем минимальное значение
    // Если она больше максимального, то возвращаем максимальное значение
    // Если она между минимальным и максимальным значениями, то возвращаем само значение
    public static double applyLimits(double v, double minV, double maxV) {
        return v < minV ? minV : min(v, maxV);
    }

    // Округляем к ближайшему целому
    static int round(double value) {
        return (int) Math.round(value);
    }
}
