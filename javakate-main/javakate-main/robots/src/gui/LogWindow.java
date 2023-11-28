package gui;

import config.State;
import java.awt.*;
import java.beans.PropertyVetoException;
import java.util.HashMap;
import java.util.Map;
import log.LogChangeListener;
import log.LogEntry;
import log.LogWindowSource;

import javax.swing.*;
import static java.lang.Boolean.parseBoolean;
import static java.lang.Integer.parseInt;

import static gui.Localization.t;

public class LogWindow extends JInternalFrame implements LogChangeListener, State {
    private final LogWindowSource m_logSource; // Хранилище (источник) сообщений в логе
    private final TextArea m_logContent; // Окно для вывода сообщений

    public LogWindow(LogWindowSource logSource) {
        super(t("log_window_title"), true, true, true, true);
        // Записываем источник сообщений
        m_logSource = logSource;
        // И подписываемся на изменения в источнике сообщений
        m_logSource.registerListener(this);
        // Создаём новую область для вывода сообщений
        m_logContent = new TextArea("");
        // Задаём ей размеры
        m_logContent.setSize(200, 500);

        // Создаём панель
        JPanel panel = new JPanel(new BorderLayout());
        // Добавляем в центр панель область вывода сообщений
        panel.add(m_logContent, BorderLayout.CENTER);
        // Добавляем саму панель на форму
        getContentPane().add(panel);
        // Упаковываем
        pack();
        // Выводим те сообщения которые уже есть в иточнике сообщений
        updateLogContent();
    }

    private void updateLogContent() {
        // Создаем строку-буфер для всего содержания
        StringBuilder content = new StringBuilder();
        // Пробегаем по всем сообщениям и добавляем каждое новое сообщение в новую строку
        for (LogEntry entry : m_logSource.all()) {
            content.append(entry.message).append("\n");
        }
        // Заменяем содержимое окна на многострочный текст со всеми сообщениями
        m_logContent.setText(content.toString());
        // Перерисовываем окно
        m_logContent.invalidate();
    }

    // Когда в источнике сообщений добавиться новое сообщение
    @Override
    public void onLogChanged() {
        // Мы вызовем перерисовку окна
        // И сделаем это во время перерисовки остального интерфейса
        EventQueue.invokeLater(this::updateLogContent);
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
