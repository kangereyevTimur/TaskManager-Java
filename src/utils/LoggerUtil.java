package utils;

import java.io.IOException;
import java.util.logging.*;
/**
 * Вспомогательный класс для логирования событий приложения.
 * Использует стандартный {@link java.util.logging.Logger}.
 *
 * @author Timur
 */
public class LoggerUtil {

    private static final Logger LOGGER = Logger.getLogger("TaskManagerLogger");
    private static boolean initialized = false;
    /**
     * Инициализирует логгер. Вызывается один раз при старте программы.
     */
    public static void init() {
        if (initialized) return;
        try {
            FileHandler fileHandler = new FileHandler("logs/app.log", true);
            fileHandler.setFormatter(new SimpleFormatter());
            LOGGER.addHandler(fileHandler);
            LOGGER.setLevel(Level.ALL);
            initialized = true;
        } catch (IOException e) {
            System.err.println("Ошибка инициализации логирования: " + e.getMessage());
        }
    }
    public static void info(String msg) {
        LOGGER.info(msg);
    }
    public static void warn(String msg) {
        LOGGER.warning(msg);
    }
    public static void error(String msg, Exception e) {
        LOGGER.log(Level.SEVERE, msg, e);
    }
}
