package model;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

import static gui.Geom.*;

public class Robot implements IRobot { // Модель робота
    public static final double maxVelocity = 9.0; // Максимальная скорость робота
    public static final double maxAngularVelocity = 0.1; // Максимальная угловая скорость робота в радианах
    static Random random = new Random(); // Генератор случайных чисел чтобы вносить случайность в движения робота

    // Подписчики на события с роботом (их робот будет оповещать при всех изменениях)
    ArrayList<RobotListener> subscribers = new ArrayList<>();

    // Внутреннее состояние робота
    // Координата x робота
    private volatile double x = 100;
    // Координата y робота
    private volatile double y = 100;
    // Направление движения робота в радианах
    private volatile double angle = 0;
    // Координата x цели
    private volatile int targetX = 150;
    // Координата y цели
    private volatile int targetY = 100;

    // Направление на цель в радианах
    private volatile double angleToTarget = 0;

    // Задать координаты цели (если пользователь поставил новую цель)
    public void setTarget(Point target) {
        targetX = target.x;
        targetY = target.y;
    }

    // Получить координату x цели
    public int getTargetX() {
        return targetX;
    }

    // Получить координату y цели
    public int getTargetY() {
        return targetY;
    }

    // Получить координату x робота
    public double getX() {
        return x;
    }

    // Получить координату y робота
    public double getY() {
        return y;
    }

    // Получить направление движения робота в радианах
    public double getAngle() {
        return angle;
    }

    // Получить направление на цель в радианах
    public double getAngleToTarget() {
        return angleToTarget;
    }

    // Выполнить ход робота
    public void go() {
        // Расстояние от робота до цели
        double distance = distance(targetX, targetY, x, y);
        // Если мы там где цель => никуда не двигаемся
        if (distance < 0.5) return;
        // Вычисляем направление на цель
        angleToTarget = angleTo(x, y, targetX, targetY);
        // Разница между направлением движения робота и направлением на цель
        double diff = PIRadians(angleToTarget - angle);
        // Сколько временных отрезков мы моделируем
        int duration = 2;
        // Угловая скорость
        double angularVelocity = diff / duration + (random.nextDouble() - 0.5) * maxAngularVelocity * 0.5;
        // Выбираем скорость робота
        // Расстояние делим на время и ещё на какое-то число и случайно замедляемся
        double v = distance / duration / 5 - random.nextDouble();
        move(v, angularVelocity, duration);
    }

    public void move(double velocity, double angularVelocity, double duration) {
        // Скорость должна быть в заданном диапазоне
        velocity = applyLimits(velocity, 0, maxVelocity);
        // Угловая скорость должна быть в заданном диапазоне
        angularVelocity = applyLimits(angularVelocity, -maxAngularVelocity, maxAngularVelocity);
        // Новый угол поворота робота
        double newAngle = asNormalizedRadians(angle + angularVelocity * duration);
        // Пересчитываем координаты x и y
        double newX = x + velocity / angularVelocity * (sin(newAngle) - sin(angle));
        //double newX = x + velocity * duration * (sin(newAngle) - sin(angle));
        if (!Double.isFinite(newX)) {
            newX = x + velocity * duration * cos(angle);
        }
        double newY = y - velocity / angularVelocity * (cos(newAngle) - cos(angle));
        //double newY = y - velocity * duration * (cos(newAngle) - cos(angle));
        if (!Double.isFinite(newY)) {
            newY = y + velocity * duration * sin(angle);
        }
        // Сохраняем новые координаты и угол
        this.x = newX;
        this.y = newY;
        this.angle = newAngle;
        // Оповещаем подписчиков, что данные изменились
        notifySubscribers();
    }

    // Оповещаем всех подписчиков, что с роботом что-то произошло
    private void notifySubscribers() {
        // Пробегаем по массиву подписчиков и каждому из них оправляем своё состояние
        for (RobotListener subscriber : subscribers)
            subscriber.update(this); // Отправляем каждому подписчику текущее состояние робота
    }

    // Добавляем подписчика на события
    public void subscribe(RobotListener subscriber) {
        subscribers.add(subscriber);
    }
}
