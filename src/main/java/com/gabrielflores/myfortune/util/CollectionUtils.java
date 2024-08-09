package com.gabrielflores.myfortune.util;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

public class CollectionUtils {

    public static int size(final Collection<?> val) {
        return Optional.ofNullable(val).map(Collection::size).orElse(0);
    }

    public static boolean isEmptyOrNull(final Collection<?> val) {
        return Optional.ofNullable(val).map(Collection::isEmpty).orElse(true);
    }

    public static boolean isEmpty(final Map<?, ?> val) {
        return Optional.ofNullable(val).map(Map::isEmpty).orElse(false);
    }

    public static boolean isEmptyOrNull(final Map<?, ?> val) {
        return Optional.ofNullable(val).map(Map::isEmpty).orElse(true);
    }

    public static boolean isNotEmpty(final Map<?, ?> val) {
        return Optional.ofNullable(val).map(v -> !v.isEmpty()).orElse(false);
    }

    public static <T> Stream<T> streamOf(final Collection<T> val) {
        return Optional.ofNullable(val).map(Collection::stream).orElseGet(Stream::empty);
    }

    public static <K, V> Stream<Entry<K, V>> streamOf(final Map<K, V> val) {
        return Optional.ofNullable(val).map((v) -> v.entrySet().stream()).orElseGet(Stream::empty);
    }

    public static <K, V> Stream<K> keyStreamOf(final Map<K, V> val) {
        return Optional.ofNullable(val).map((v) -> v.keySet().stream()).orElseGet(Stream::empty);
    }

    public static <K, V> Stream<V> valueStreamOf(final Map<K, V> val) {
        return Optional.ofNullable(val).map((v) -> v.values().stream()).orElseGet(Stream::empty);
    }

    public static <T> Stream<T> streamOf(final T[] val) {
        return Optional.ofNullable(val).map(Stream::of).orElseGet(Stream::empty);
    }

    public static <T> Set<T> nullsafe(final Set<T> val) {
        return Optional.ofNullable(val).orElseGet(LinkedHashSet<T>::new);
    }

    public static <T> List<T> nullsafe(final List<T> val) {
        return Optional.ofNullable(val).orElseGet(LinkedList<T>::new);
    }

    public static <T> Collection<T> nullsafe(final Collection<T> val) {
        return Optional.ofNullable(val).orElseGet(LinkedList<T>::new);
    }

    public static <T> T getFirst(final Collection<T> val) {
        return !isEmptyOrNull(val) ? val.iterator().next() : null;
    }

    public static <K, V> Map<K, V> nullsafe(final Map<K, V> val) {
        return Optional.ofNullable(val).orElseGet(LinkedHashMap<K, V>::new);
    }

    public static <T> Stream<T> nullsafe(final Stream<T> val) {
        return Optional.ofNullable(val).orElseGet(Stream::empty);
    }

}
