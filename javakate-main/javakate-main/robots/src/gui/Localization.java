package gui;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentHashMap;

public enum Localization {
    RU("ru", "RU"),
    TRANSLIT("translit", "RU");

    static final ConcurrentHashMap<String, MessageFormat> messages = new ConcurrentHashMap<>();
    private static ResourceBundle bundle;
    private final Locale locale;

    Localization(String language, String country) {
        locale = new Locale(language, country);
    }

    // Инициализация языка
    synchronized static void init(Localization localization) {
        bundle = ResourceBundle.getBundle("messages", localization.locale);
        messages.clear(); // Очищаем кеш сообщений
    }

    // Получение переведённой строки
    public static String t(String id, Object... params) {
        return messages.computeIfAbsent(id, k -> new MessageFormat(getString(id)))
                .format(params);
    }

    synchronized static String getString(String id) {
        try {
            return bundle.getString(id);
        } catch (Exception e) {
            return id;
        }
    }
}
