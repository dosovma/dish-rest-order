package ru.dosov.restvoting.util;

import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {
    public static final java.time.format.DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

    private DateTimeUtil () {}

    public static LocalDate checkFillDateForMenu(LocalDate date) {
        return date == null ? LocalDate.now() : date;
    }

    public static @Nullable LocalDate parseToLocalDate(@Nullable String str) {
        return StringUtils.hasText(str)
                ? LocalDate.parse(str, DATE_TIME_FORMATTER)
                : null;
    }
}