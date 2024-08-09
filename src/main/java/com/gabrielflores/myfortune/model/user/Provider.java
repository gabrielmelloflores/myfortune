package com.gabrielflores.myfortune.model.user;

import com.gabrielflores.myfortune.converter.StringEnumConverter;
import com.gabrielflores.myfortune.model.entity.StringEnum;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/** 
 * @Author: Gabriel Flores - gabrielmelloflores@gmail.com  
 * @Date: 2024-08-07 09:59:41  
*/
@Getter
@RequiredArgsConstructor
public enum Provider implements StringEnum {

    LOCAL("local", "Autenticação via Pet Connect"),
    GOOGLE("google", "Autenticação via Google"),
    APPLE("apple", "Autenticação via Apple");

    private final String valor;
    private final String descricao;

    public static Provider getByValor(final String valor) {
        for (Provider provider : values()) {
            if (provider.getValor().equals(valor)) {
                return provider;
            }
        }
        return null;
    }

    public static class Converter extends StringEnumConverter<Provider> {
    }
}
