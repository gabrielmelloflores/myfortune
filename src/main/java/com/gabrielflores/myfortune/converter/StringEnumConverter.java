package com.gabrielflores.myfortune.converter;

import com.gabrielflores.myfortune.model.entity.StringEnum;
import jakarta.persistence.AttributeConverter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.stream.Stream;

/**
 *
 * @Author: Gabriel Flores - gabrielmelloflores@gmail.com  
 * @param <E> enum type
 */
public class StringEnumConverter<E extends Enum<E> & StringEnum> implements AttributeConverter<E, String> {

    private final Class<E> enumClass;

    @SuppressWarnings("unchecked")
    public StringEnumConverter() {
        final Type genericSuperclass = getClass().getGenericSuperclass();
        if (genericSuperclass instanceof ParameterizedType parameterizedType) {
            final Type[] types = parameterizedType.getActualTypeArguments();
            this.enumClass = types[0] instanceof ParameterizedType type ? (Class<E>) type.getRawType() : (Class<E>) types[0];
        } else {
            throw new IllegalStateException("Erro na parametrização do bean");
        }
    }

    @Override
    public String convertToDatabaseColumn(E e) {
        return e != null ? e.getValor() : null;
    }

    @Override
    public E convertToEntityAttribute(String valor) {
        if (valor == null) {
            return null;
        }
        return Stream.of(this.enumClass.getEnumConstants())
                .filter(e -> e.getValor().equals(valor.trim()))
                .findFirst().orElseThrow(() -> {
                    return new IllegalArgumentException(String.format("Valor '%s' inválido para enum '%s'", valor, this.enumClass.getSimpleName()));
                });
    }
}
