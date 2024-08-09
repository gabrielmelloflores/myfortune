package com.gabrielflores.myfortune.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.FormatStyle;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

@FunctionalInterface
public interface DateTimeFormatter<IN> {

    String apply(IN obj);

    default Optional<String> applyOptional(final IN obj) {
        final String str = apply(obj);
        return (str == null || str.trim().isEmpty()) ? Optional.empty() : Optional.of(str);
    }

    default boolean supports() {
        return false;
    }

    public static String format(final Object val) {
        return getDefault().apply(val);
    }

    public static Optional<String> formatOptional(final Object val) {
        return Optional.ofNullable(format(val));
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static DateTimeFormatter<Object> nullsafe(final DateTimeFormatter f) {
        return f != null ? f : getDefault();
    }

    public static DateTimeFormatter<Object> getDefault() {
        return createFormatter(
                (val) -> {
                    if ((val instanceof Date) || (val instanceof Calendar) || (val instanceof LocalDateTime)) {
                        return DateTimeFormatter.ofDateTime().apply(val);
                    } else if (val instanceof LocalDate) {
                        return DateTimeFormatter.ofDate().apply(val);
                    } else if (val instanceof LocalTime) {
                        return DateTimeFormatter.ofTime().apply(val);
                    }
                    return Objects.toString(val, null);
                }
        );
    }

    public static DateTimeFormatter<Object> ofDate() {
        return ofDate(null);
    }

    public static DateTimeFormatter<Object> ofDate(final Locale locale) {
        return createFormatter(val -> java.time.format.DateTimeFormatter
                .ofLocalizedDate(FormatStyle.SHORT)
                .withLocale(locale != null ? locale : Locale.getDefault(Locale.Category.FORMAT))
                .format(DateTimeConverter.toLocalDate().apply(val))
        );
    }

    public static DateTimeFormatter<Object> ofTime() {
        return ofTime(null);
    }

    public static DateTimeFormatter<Object> ofTime(final Locale locale) {
        return createFormatter(val -> java.time.format.DateTimeFormatter
                .ofLocalizedTime(FormatStyle.SHORT)
                .withLocale(locale != null ? locale : Locale.getDefault(Locale.Category.FORMAT))
                .format(DateTimeConverter.toLocalTime().apply(val))
        );
    }

    public static DateTimeFormatter<Object> ofDateTime() {
        return ofDateTime(null, null);
    }

    public static DateTimeFormatter<Object> ofDateTime(final String pattern) {
        return ofDateTime(pattern, null);
    }

    public static DateTimeFormatter<Object> ofDateTime(final Locale locale) {
        return ofDateTime(null, locale);
    }

    public static DateTimeFormatter<Object> ofDateTime(final String pattern, final Locale locale) {
        return createFormatter(val -> Optional
                .ofNullable(pattern)
                .filter(p -> !p.isEmpty())
                .map(java.time.format.DateTimeFormatter::ofPattern)
                .orElse(java.time.format.DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT))
                .withLocale(locale != null ? locale : Locale.getDefault(Locale.Category.FORMAT))
                .format(DateTimeConverter.toLocalDateTime().apply(val))
        );
    }

    public static <IN> DateTimeFormatter<IN> createFormatter(final Function<IN, String> func) {
        Objects.requireNonNull(func, "Function cannot be null.");
        return (final IN obj) -> obj != null ? (String) func.apply(obj) : null;
    }
}
