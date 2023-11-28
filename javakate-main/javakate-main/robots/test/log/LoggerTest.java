package log;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LoggerTest {

    @BeforeEach
    void setUp() {
        // Очистка логов перед каждым тестом
        Logger.getDefaultLogSource().clear();
    }

    @Test
    void testDebug() {
        String testMessage = "Тестовое сообщение для debug";
        Logger.debug(testMessage);

        LogWindowSource logSource = Logger.getDefaultLogSource();
        assertEquals(1, logSource.size(), "Лог должен содержать одно сообщение");

        LogEntry logEntry = logSource.all().iterator().next();
        assertEquals(LogLevel.Debug, logEntry.level, "Уровень логирования должен быть DEBUG");
        assertEquals(testMessage, logEntry.message, "Сообщение в логе не соответствует отправленному");
    }

    @Test
    void testError() {
        String testMessage = "Тестовое сообщение для error";
        Logger.error(testMessage);

        LogWindowSource logSource = Logger.getDefaultLogSource();
        assertEquals(1, logSource.size(), "Лог должен содержать одно сообщение");

        LogEntry logEntry = logSource.all().iterator().next();
        assertEquals(LogLevel.Error, logEntry.level, "Уровень логирования должен быть ERROR");
        assertEquals(testMessage, logEntry.message, "Сообщение в логе не соответствует отправленному");
    }
}
