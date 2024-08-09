package com.gabrielflores.myfortune.util;

import java.util.Optional;
import java.util.function.Supplier;
import java.util.regex.Pattern;

public class TextUtils {

    private static final String RAND_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789#@$?&!+=-";
    public static final String UPPERCASES_REGEX = "\\p{Lu}+";
    public static final String LOWERCASES_REGEX = "\\p{Ll}+";
    public static final String LETTERS_REGEX = "\\p{L}+";
    public static final String DIGITS_REGEX = "\\d+";
    public static final String SIMBOLS_REGEX = "[\\p{P}\\p{S}]+";
    public static final String SPACES_REGEX = "\\s+";

    public static boolean isBlankOrNull(final CharSequence val) {
        if (val == null || val.length() == 0) {
            return true;
        }
        return Pattern.compile("\\s*").matcher(val).matches();
    }

    public static boolean isNotBlank(final CharSequence val) {
        return !isBlankOrNull(val);
    }

    public static int length(final CharSequence val) {
        return Optional.ofNullable(val).map(CharSequence::length).orElse(0);
    }

    public static <T extends CharSequence> Optional<T> optionalOf(final T val) {
        return isBlankOrNull(val) ? Optional.empty() : Optional.of(val);
    }

    public static <T extends CharSequence> T notBlankOrElse(final T val, final T def) {
        return optionalOf(val).orElse(def);
    }

    public static <T extends CharSequence> T notBlankOrElse(final T val, final Supplier<T> def) {
        return optionalOf(val).orElseGet(def);
    }

    public static <T extends CharSequence> T notBlankOrThrow(final T val, final Supplier<? extends RuntimeException> def) {
        return optionalOf(val).orElseThrow(def);
    }

    public static boolean hasUppercase(final CharSequence sequence) {
        return isNotBlank(sequence) ? containsPattern(UPPERCASES_REGEX, sequence) : false;
    }

    public static boolean hasLowercase(final CharSequence sequence) {
        return isNotBlank(sequence) ? containsPattern(LOWERCASES_REGEX, sequence) : false;
    }

    public static boolean hasLetter(final CharSequence sequence) {
        return isNotBlank(sequence) ? containsPattern(LETTERS_REGEX, sequence) : false;
    }

    public static boolean hasDigit(final CharSequence sequence) {
        return isNotBlank(sequence) ? containsPattern(DIGITS_REGEX, sequence) : false;
    }

    public static boolean hasSymbol(final CharSequence sequence) {
        return isNotBlank(sequence) ? containsPattern(SIMBOLS_REGEX, sequence) : false;
    }

    public static boolean hasSpace(final CharSequence sequence) {
        return isNotBlank(sequence) ? containsPattern(SPACES_REGEX, sequence) : false;
    }

    public static boolean matches(final CharSequence regex, final CharSequence sequence) {
        return matches(regex, sequence, 0);
    }

    public static boolean matches(final CharSequence regex, final CharSequence sequence, final int flags) {
        final Pattern pattern = Pattern.compile(regex.toString(), flags);
        return pattern.matcher(sequence).matches();
    }

    public static boolean containsPattern(final CharSequence regex, final CharSequence sequence) {
        final Pattern pattern = Pattern.compile(regex.toString());
        return pattern.matcher(sequence).find();
    }

    public static String capitalize(String str) {
        char[] chars = str.toCharArray();
        chars[0] = Character.toUpperCase(chars[0]);
        return String.valueOf(chars);
    }

    public static String decapitalize(String str) {
        char[] chars = str.toCharArray();
        chars[0] = Character.toLowerCase(chars[0]);
        return String.valueOf(chars);
    }

    public static String generateRandomString(final int length) {
        final StringBuilder result = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int position = (int) Math.floor(Math.random() * RAND_CHARS.length());
            result.append(RAND_CHARS.charAt(position));
        }
        return result.toString();
    }

}
