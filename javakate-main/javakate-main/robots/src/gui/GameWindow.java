package gui;

import config.State;
import java.awt.*;
import java.beans.PropertyVetoException;
import java.util.HashMap;
import java.util.Map;
import model.IRobot;

import javax.swing.*;
import static java.lang.Boolean.parseBoolean;
import static java.lang.Integer.parseInt;

import static gui.Localization.t;

// Игровое поле, поддерживает сохранение состояния
// Для этого реализует интерфейс State
public class GameWindow extends JInternalFrame implements State {
    // Конструктор
    public GameWindow(IRobot robot) {
        // Название окна
        super(t("Игровое поле"), true, true, true, true);
        // Создаём панель окна с выравниванием Вверх, вниз, влево, вправо и по центру
        JPanel p = new JPanel(new BorderLayout());
        // Создаём компонент который рисует робота и цель
        GameVisualizer visualizer = new GameVisualizer(robot);
        // И добавляем его в центр панели (по факту он занимает всю панель)
        p.add(visualizer, BorderLayout.CENTER);
        // Добавляем панель в форму
        getContentPane().add(p);
        // Упаковываем форму (выравниваем все вложенные компоненты)
        // Пересчитываются отступы, рамочки и т.д.
        pack();
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
}
