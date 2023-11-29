package gui;

import config.State;
import java.awt.*;
import java.beans.PropertyVetoException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import model.IRobot;
import model.RobotListener;

import javax.swing.*;
import static java.lang.Boolean.parseBoolean;
import static java.lang.Integer.parseInt;

import static gui.Localization.t;

// Окно для отображения координат и других параметров робота
public class RobotCoordWindow extends JInternalFrame implements State, RobotListener {

    // X-координата робота
    private final JLabel robotX;
    // Y-координата робота
    private final JLabel robotY;
    // Направление движения робота
    private final JLabel angle;
    // Направление на цель
    private final JLabel angleToTarget;

    // Формат для округления данных о роботе
    DecimalFormat format = new DecimalFormat("#.0");

    public RobotCoordWindow(IRobot robot) {
        super(t("Координаты робота"), true, true, true, true);
        // Создаём панель
        JPanel p = new JPanel(new GridLayout(2, 2));
        // Добавлеям подпись к каждому параметру и сам параметр
        p.add(new JLabel("Координата X"));
        robotX = new JLabel(robot.getX() + "");
        p.add(robotX);
        p.add(new JLabel("Координата Y"));
        robotY = new JLabel(robot.getY() + "");
        p.add(robotY);
        p.add(new JLabel("Угол робота"));
        angle = new JLabel(robot.getAngle() + "");
        p.add(angle);
        p.add(new JLabel("Угол на цель"));
        angleToTarget = new JLabel(robot.getAngleToTarget() + "");
        p.add(angleToTarget);
        // Саму панель добавляем на форму
        getContentPane().add(p);
        // Перепакуем для отрисовки
        pack();
        robot.subscribe(this); // Подписываемся на изменения робота
    }


    // Получаем текущее состояние окна
    @Override
    public Map<String, String> getState() {
        HashMap<String, String> s = new HashMap<>();
        // Получаем x-координату верхнего левого угла окна
        s.put("x", getLocation().x + "");
        // Получаем y-координату верхнего левого угла окна
        s.put("y", getLocation().y + "");
        // Получаем ширину окна
        s.put("width", getWidth() + "");
        // Получаем высоту окна
        s.put("height", getHeight() + "");
        // Получаем максимизировано ли окно?
        s.put("isMaximum", isMaximum() + "");
        return s;
    }

    // Восстанавливаем состояние окна из словаря
    @Override
    public void setState(Map<String, String> s) throws PropertyVetoException {
        // Если ширина и высота окна пропущены => это какой-то кривой конфиг
        // Пропускаем
        if (s.get("width") == null || s.get("height") == null) return;
        // Задаём размеры окна из загруженных
        setSize(parseInt(s.get("width")), parseInt(s.get("height")));
        // Если координаты окна пропущены => выходим
        if (s.get("x") == null || s.get("y") == null) return;
        // Если координаты есть => перемещаем окно по этим координатам
        setLocation(parseInt(s.get("x")), parseInt(s.get("y")));
        // Максимизируем окно если сказано что оно было максимизировано
        setMaximum(parseBoolean(s.get("isMaximum")));
    }

    @Override
    public void update(IRobot robot) {
        robotX.setText(format.format(robot.getX()));
        robotY.setText(format.format(robot.getY()));
        angle.setText(format.format(robot.getAngle()));
        angleToTarget.setText(format.format(robot.getAngleToTarget()));
    }
}
