package log;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LogEntryTest {

    @Test
    void testLogEntry() {
        // Создаем экземпляр LogEntry
        LogEntry logEntry = new LogEntry(LogLevel.Info, "Тестовое сообщение");

        // Проверяем, что уровень логирования был корректно установлен
        assertEquals(LogLevel.Info, logEntry.level, "Уровень логирования не соответствует ожидаемому");

        // Проверяем, что сообщение было корректно установлено
        assertEquals("Тестовое сообщение", logEntry.message, "Сообщение не соответствует ожидаемому");
    }
}
