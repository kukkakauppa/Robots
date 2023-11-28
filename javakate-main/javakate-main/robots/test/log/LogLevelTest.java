package log;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LogLevelTest {
    @Test
    void testLogLevelValues() {
        // Проверяем числовые значения для каждого уровня логирования
        assertEquals(0, LogLevel.Trace.level, "Уровень Trace должен иметь значение 0");
        assertEquals(1, LogLevel.Debug.level, "Уровень Debug должен иметь значение 1");
        assertEquals(2, LogLevel.Info.level, "Уровень Info должен иметь значение 2");
        assertEquals(3, LogLevel.Warning.level, "Уровень Warning должен иметь значение 3");
        assertEquals(4, LogLevel.Error.level, "Уровень Error должен иметь значение 4");
        assertEquals(5, LogLevel.Fatal.level, "Уровень Fatal должен иметь значение 5");
    }
}
