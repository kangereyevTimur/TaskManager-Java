package utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Вспомогательный класс с утилитами для работы с датами и другими функциями.
 */
public class Helper {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    /**
     * Форматирует LocalDate в строку для отображения.
     *
     * @param date дата для форматирования
     * @return строковое представление даты в формате dd.MM.yyyy
     */
    public static String formatDate(LocalDate date) {
        if (date == null) {
            return "";
        }
        return date.format(FORMATTER);
    }
}

