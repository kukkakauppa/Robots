package log;

// Запись в лог-файл
public class LogEntry {
    public final LogLevel level; // Тип сообщения
    public final String message; // Текст сообщения

    public LogEntry(LogLevel logLevel, String strMessage) {
        message = strMessage;
        level = logLevel;
    }
}

