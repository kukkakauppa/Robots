package log;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LogWindowSourceTest {
    private LogWindowSource logSource;

    @BeforeEach
    void setUp() {
        logSource = new LogWindowSource(5);
    }

    @Test
    void testAppendAndSize() {
        logSource.append(LogLevel.Debug, "Test message 1");
        assertEquals(1, logSource.size(), "Размер лога должен быть 1 после добавления одного сообщения");

        logSource.append(LogLevel.Error, "Test message 2");
        assertEquals(2, logSource.size(), "Размер лога должен быть 2 после добавления двух сообщений");
    }

    @Test
    void testRegisterAndUnregisterListener() {
        TestLogChangeListener listener = new TestLogChangeListener();
        logSource.registerListener(listener);
        assertTrue(logSource.isRegistered(listener), "Слушатель должен быть зарегистрирован");

        logSource.unregisterListener(listener);
        assertFalse(logSource.isRegistered(listener), "Слушатель должен быть удален");
    }

    @Test
    void testLogChangeNotification() {
        TestLogChangeListener listener = new TestLogChangeListener();
        logSource.registerListener(listener);
        logSource.append(LogLevel.Info, "Test message");
        assertTrue(listener.isNotified(), "Слушатель должен получить уведомление об изменении лога");
    }

    @Test
    void testQueueLengthLimit() {
        int queueLength = 5;
        LogWindowSource logSource = new LogWindowSource(queueLength);

        // Добавляем сообщения в лог, превышая лимит
        for (int i = 0; i < queueLength + 2; i++) {
            logSource.append(LogLevel.Info, "Test message " + i);
        }

        // Проверяем, что размер лога не превышает установленный лимит
        assertEquals(queueLength, logSource.size(), "Размер лога должен быть ограничен значением queueLength");

        // Проверяем, что старые сообщения были удалены
        Iterable<LogEntry> messages = logSource.all();
        int messageIndex = 0;
        for (LogEntry message : messages) {
            assertEquals("Test message " + (messageIndex + 2), message.message, "Неверное сообщение в логе");
            messageIndex++;
        }
    }

    private static class TestLogChangeListener implements LogChangeListener {
        private boolean isNotified = false;

        @Override
        public void onLogChanged() {
            isNotified = true;
        }

        public boolean isNotified() {
            return isNotified;
        }
    }
}
