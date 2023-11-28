package model;

import java.awt.*;

public interface IRobot {
    // Задать цель движения робота
    void setTarget(Point p);

    // Получить x-координату цели робота
    int getTargetX();

    // Получить y-координату цели робота
    int getTargetY();

    // Получить x-координату робота
    double getX();

    // Получить y-координату робота
    double getY();

    // Получить направление движения робота
    double getAngle();

    // Получить направление от робота к цели
    double getAngleToTarget();

    // Движение робота
    void go();

    // Подписываемся на изменения состояния робота
    void subscribe(RobotListener subscriber);
}
