package gui;

import config.Config;
import config.ConfigFile;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Paths;
import log.Logger;
import model.IRobot;
import model.Robot;

import javax.swing.*;
import static java.awt.event.KeyEvent.*;

import static gui.Localization.t;
import static javax.swing.JOptionPane.*;

/**
 * Что требуется сделать:
 * 1. Метод создания меню перегружен функционалом и трудно читается.
 * Следует разделить его на серию более простых методов (или вообще выделить отдельный класс).
 */
public class MainApplicationFrame extends JFrame {
    private final JDesktopPane desktopPane = new JDesktopPane();
    private final ConfigFile fileConfig;
    private final Config config;

    public MainApplicationFrame() {
        Localization.init(Localization.RU); // Инициализируем язык
        // Задаём заголовок окна
        setTitle(t("title"));
        // Конфигурационный файл будет расположен в каталоге пользователя и будет называться
        // robots.txt
        // Например: C:\Users\Ekaterina\robots.txt
        fileConfig = new ConfigFile(Paths.get(System.getProperty("user.home")).resolve("robots.txt"));
        config = fileConfig.tryLoad();

        // Большое окно должно иметь отступы по 50 пикселей с каждой стороны экрана
        int inset = 50;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(inset, inset, screenSize.width - inset * 2, screenSize.height - inset * 2);

        setContentPane(desktopPane);

        // Создаём модель робота, которую потом отправим в 2 окна
        IRobot robot = new Robot();
        // Добавляем окно с журналом сообщений
        addWindow(createLogWindow());
        // Добавляем окно, в котором будет рисоваться робот
        addWindow(createGameWindow(robot));
        // Добавляем окно, в которое будем выводить параметры робота
        addWindow(createRobotCoordWindow(robot));

        // Добавляем меню которое будет создано в методе genMenu()
        setJMenuBar(genMenu());
        // Отключаем действие при закрытии приложения, настроенное по умолчанию.
        // Делается это вызовом:
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        config.loadAllWindows();
    }


    void addWindow(JInternalFrame frame) {
        desktopPane.add(frame);
        frame.setVisible(true);
    }

    LogWindow createLogWindow() { // Создание окна для вывода журнала сообщений
        // Создаём окно и передаём ему общее хранилище сообщений
        LogWindow w = new LogWindow(Logger.getDefaultLogSource());
        // Указываем координаты верхнего левого угла окна
        w.setLocation(10, 10);
        // Указываем начальный размер окна
        w.setSize(300, 800);
        // Указываем что нельзя его уменьшать меньше начального размера
        setMinimumSize(w.getSize());
        // Упаковываем все вложенные компоненты (они при этом выравниваются внутри как мы задали)
        w.pack();
        // Регистрируем в конфигурации приложения
        config.register(w, "log");
        // Отпавляем первое сообщение в журнал работы чтобы проверить что он работает
        Logger.debug("Протокол работает");
        return w;
    }

    private GameWindow createGameWindow(IRobot robot) {
        GameWindow w = new GameWindow(robot);
        w.setSize(400, 400);
        config.register(w, "model");
        return w;
    }

    private RobotCoordWindow createRobotCoordWindow(IRobot robot) {
        RobotCoordWindow w = new RobotCoordWindow(robot);
        w.setSize(200, 100);
        config.register(w, "coordinates");
        return w;
    }

    // Создание главного меню приложения
    private JMenuBar genMenu() {
        JMenuBar m = new JMenuBar(); // Создаём меню для нашей программы
        // Создаём выпадающее меню с внешним видом программы
        m.add(subMenu("Режим отображения", VK_V, "Управление режимом отображения приложения",
                menu("Системная схема", VK_S, () -> {
                    setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                    this.invalidate();
                }),
                menu("Универсальная схема", VK_U, () -> {
                    setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
                    this.invalidate();
                })
        ));
        // Создаём выпадающее меню для тестирования
        m.add(subMenu("Тесты", VK_T, "Тестовые команды",
                menu("Сообщение в лог", VK_S, () -> Logger.debug("Новая строка")),
                menu("Привет!", VK_K, () -> Logger.debug("Привет всем!"))
        ));
        m.add(subMenu("Язык", VK_L, "Выбор языка интерфейса",
                menu("Русский", VK_R, () -> lang(Localization.RU)),
                menu("Translit", VK_T, () -> lang(Localization.TRANSLIT))
        ));
        // 1. Добавить пункт меню, позволяющий закрыть приложение и сделать так,
        // чтобы в методе выдавался запрос на подтверждение выхода (класс JOptionPane)
        // Добавляем пункт меню "Выход" в общее меню
        m.add(menu("Выход из программы", VK_ESCAPE, () -> {
            if (JOptionPane.showConfirmDialog(this, // Родительское окно
                    "Выйти из приложения?", // Сообщение в окне
                    "Подтверждение выхода", // Заголовок окна
                    YES_NO_OPTION // Какие кнопки
            ) == YES_OPTION) {
                saveConfig();
                this.dispose(); // Закрываем основное окно
                System.exit(0); // Закрываем программу
            } else {
                Logger.debug("При подтверждении выхода вы нажали НЕТ или закрыли окно");
            }
        }));
        return m;
    }

    // Создание подменю в основном меню программы
    private JMenu subMenu(String s, int hotKey, String description, JMenuItem... items) {
        JMenu m = new JMenu(t(s)); // Создаём подменю
        m.setMnemonic(hotKey); // Задаём "горячую" кнопку для запуска
        m.getAccessibleContext().setAccessibleDescription(t(description)); // Задаём описание
        for (JMenuItem x : items) m.add(x); // Добавляем пункты меню
        return m; // Возвращаем подменю
    }

    private JMenuItem menu(String s, int hotKey, Runnable action) {
        JMenuItem mi = new JMenuItem(t(s), hotKey); // Создаём пункт меню с текстом и "горячей" кнопкой
        //mi.setAccelerator(KeyStroke.getKeyStroke(hotKey, ALT_MASK));
        //mi.setActionCommand("new");
        // Добавляем обработчик события (событие - нажатие на пункт меню)
        mi.addActionListener(event -> action.run());
        return mi;
    }

    private void setLookAndFeel(String className) {
        try {
            UIManager.setLookAndFeel(className);
            SwingUtilities.updateComponentTreeUI(this);
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            // just ignore
        }
    }

    private void lang(Localization local) {
        Logger.debug("Выбран язык: " + local.toString());
        Localization.init(local);
        this.invalidate();
    }

    void saveConfig() {
        config.saveAllWindows();
        try {
            fileConfig.save(config);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
