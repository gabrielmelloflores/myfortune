package com.gabrielflores.myfortune.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.function.Function;

@FunctionalInterface
public interface DateTimeConverter<IN, OUT> {

    OUT apply(IN obj);

    public static DateTimeConverter<Object, LocalDate> toLocalDate() {
        return createConverter(
                (val) -> {
                    if (val == null || (val instanceof LocalDate)) {
                        return (LocalDate) val;
                    } else if (val instanceof java.sql.Date dt) {
                        return dt.toLocalDate();
                    } else if (val instanceof java.util.Date sdt) {
                        return LocalDateTime.ofInstant((sdt).toInstant(), ZoneId.systemDefault()).toLocalDate();
                    } else if (val instanceof Calendar cal) {
                        return LocalDateTime.ofInstant((cal).toInstant(), ZoneId.systemDefault()).toLocalDate();
                    } else if (val instanceof LocalDateTime ldt) {
                        return ldt.toLocalDate();
                    }
                    throw new IllegalArgumentException("Value type <" + val.getClass().getSimpleName() + "> not supportted");
                }
        );
    }

    public static DateTimeConverter<Object, LocalTime> toLocalTime() {
        return createConverter(
                (val) -> {
                    if (val == null || (val instanceof LocalTime)) {
                        return (LocalTime) val;
                    } else if (val instanceof java.sql.Date) {
                        return LocalTime.MIDNIGHT;
                    } else if (val instanceof java.util.Date dt) {
                        return LocalDateTime.ofInstant((dt).toInstant(), ZoneId.systemDefault()).toLocalTime();
                    } else if (val instanceof Calendar cal) {
                        return LocalDateTime.ofInstant((cal).toInstant(), ZoneId.systemDefault()).toLocalTime();
                    } else if (val instanceof LocalDateTime ldt) {
                        return ldt.toLocalTime();
                    }
                    throw new IllegalArgumentException("Value type <" + val.getClass().getSimpleName() + "> not supportted");
                }
        );
    }

    public static DateTimeConverter<Object, LocalDateTime> toLocalDateTime() {
        return createConverter(
                (val) -> {
                    if (val == null || (val instanceof LocalDateTime)) {
                        return (LocalDateTime) val;
                    } else if (val instanceof Date dt) {
                        return LocalDateTime.ofInstant((dt).toInstant(), ZoneId.systemDefault());
                    } else if (val instanceof Calendar cal) {
                        return LocalDateTime.ofInstant((cal).toInstant(), ZoneId.systemDefault());
                    } else if (val instanceof LocalDate ld) {
                        return LocalDateTime.of(ld, LocalTime.MIN);
                    } else if (val instanceof LocalTime lt) {
                        return LocalDateTime.of(LocalDate.of(1970, Month.JANUARY, 1), lt);
                    }
                    throw new IllegalArgumentException("Value type <" + val.getClass().getSimpleName() + "> not supportted");
                }
        );
    }

    public static <IN, OUT> DateTimeConverter<IN, OUT> createConverter(final Function<IN, OUT> func) {
        Objects.requireNonNull(func, "Function cannot be null.");
        return (final IN obj) -> obj != null ? (OUT) func.apply(obj) : null;
    }
}
