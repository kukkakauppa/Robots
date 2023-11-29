package gui;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import log.Logger;

import javax.swing.*;

import static javax.swing.JOptionPane.YES_NO_OPTION;
import static javax.swing.JOptionPane.YES_OPTION;

public class RobotsProgram {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
//        UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
//        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//        UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            // 3. Сделать так, чтобы диалог на основе JOptionPane
            // выдавал текст на кнопках на русском языке
//            Locale locale = new Locale("ru", "");
//            Locale.setDefault(locale);
//            JOptionPane.setDefaultLocale(locale);
            // Нам нужны для интерфейса только Да и Нет
            UIManager.put("OptionPane.yesButtonText", "Да");
            UIManager.put("OptionPane.noButtonText", "Нет");
            //UIManager.put("OptionPane.cancelButtonText", "Отмена");
            //UIManager.put("OptionPane.okButtonText", "Готово");
        } catch (Exception e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(() -> {
            MainApplicationFrame frame = new MainApplicationFrame();
            frame.pack();
            // 2. Добавить обработку события выхода из приложения
            frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
            frame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    // Обработка события закрытия окна
                    Logger.debug("Выходим из программы");
                    //  JOptionPane.showMessageDialog(frame, "Выходим из программы");
                    if (JOptionPane.showConfirmDialog(frame,
                            "Выйти из приложения?",
                            "Подтверждение выхода",
                            YES_NO_OPTION) == YES_OPTION) {
                        frame.saveConfig();
                        e.getWindow().dispose();
                        System.exit(0);
                    }
                }
            });
            frame.setVisible(true);
            frame.setExtendedState(Frame.MAXIMIZED_BOTH);
        });
    }
}
