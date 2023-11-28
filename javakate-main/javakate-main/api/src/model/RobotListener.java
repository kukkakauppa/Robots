package model;

import java.util.EventListener;

// Когда мы хотим подписаться на события с роботом, мы должны реализовать этот интерфейс
public interface RobotListener extends EventListener {
    // Когда робот будет менять координаты, он будет вызывать метод update()
    void update(IRobot robot);
}
