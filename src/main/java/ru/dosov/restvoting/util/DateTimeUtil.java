package ru.dosov.restvoting.util;

import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {
    public static final java.time.format.DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

    private static final LocalDateTime MIN_DATE = LocalDateTime.of(1, 1, 1, 0, 0);
    private static final LocalDateTime MAX_DATE = LocalDateTime.of(3000, 1, 1, 0, 0);

    private DateTimeUtil() {
    }

    public static LocalDate getDateOrNow(@Nullable LocalDate date) {
        return date == null ? LocalDate.now() : date;
    }

    public static @Nullable
    LocalDate parseToLocalDate(@Nullable String str) {
        return StringUtils.hasText(str)
                ? LocalDate.parse(str, DATE_TIME_FORMATTER)
                : null;
    }

    public static @Nullable
    LocalDateTime getOrMinDateTime(@Nullable LocalDate localDate) {
        return localDate == null ? MIN_DATE : localDate.atTime(0, 0, 0);
    }

    public static @Nullable
    LocalDateTime getOrMaxDateTime(@Nullable LocalDate localDate) {
        return localDate == null ? MAX_DATE : localDate.atTime(23, 59, 59);
    }

    public static @Nullable
    LocalDate getOrMinDate(@Nullable LocalDate localDate) {
        return localDate == null ? MIN_DATE.toLocalDate() : localDate;
    }

    public static @Nullable
    LocalDate getOrMaxDate(@Nullable LocalDate localDate) {
        return localDate == null ? MAX_DATE.toLocalDate() : localDate;
    }
}