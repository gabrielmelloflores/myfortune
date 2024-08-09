package com.gabrielflores.myfortune.model.assinatura;

import com.gabrielflores.myfortune.converter.StringEnumConverter;
import com.gabrielflores.myfortune.model.entity.StringEnum;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 *
 * @author Douglas Pasqualin <douglas.pasqualin@gmail.com>
 */
@Getter
@RequiredArgsConstructor
public enum TipoDesconto implements StringEnum {

    PERCENTUAL("P", "Percentual"),
    VALOR("V", "Valor");

    private final String valor;
    private final String descricao;

    public static class Converter extends StringEnumConverter<TipoDesconto> {
    }
}
