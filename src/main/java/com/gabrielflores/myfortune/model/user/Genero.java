package com.gabrielflores.myfortune.model.user;

import com.gabrielflores.myfortune.converter.StringEnumConverter;
import com.gabrielflores.myfortune.model.entity.StringEnum;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/** 
 * @Author: Gabriel Flores - gabrielmelloflores@gmail.com  
 * @Date: 2024-08-07 09:52:41  
*/

@Getter
@RequiredArgsConstructor
public enum Genero implements StringEnum {
    FEMININO("F", "Feminino"),
    MASCULINO("M", "Masculino"),
    OUTRO("O", "Outro");

    private final String valor;
    private final String descricao;

    public static class Converter extends StringEnumConverter<Genero> {
    }
}
