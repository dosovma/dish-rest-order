package ru.dosov.restvoting.util;

import org.springframework.format.Formatter;

import java.time.LocalDate;
import java.util.Locale;

public class DateTimeFormatter implements Formatter<LocalDate> {

    @Override
    public LocalDate parse(String text, Locale locale) {
        return DateTimeUtil.parseToLocalDate(text);
    }

    @Override
    public String print(LocalDate lt, Locale locale) {
        return lt.format(DateTimeUtil.DATE_TIME_FORMATTER);
    }
}