package log;

import static log.LogLevel.Debug;
import static log.LogLevel.Error;

public final class Logger {
    private static final LogWindowSource defaultLogSource;

    static {
        defaultLogSource = new LogWindowSource(100);
    }

    private Logger() {
    }

    public static void debug(String s) {
        defaultLogSource.append(Debug, s);
    }

    public static void error(String s) {
        defaultLogSource.append(Error, s);
    }

    public static LogWindowSource getDefaultLogSource() {
        return defaultLogSource;
    }
}
