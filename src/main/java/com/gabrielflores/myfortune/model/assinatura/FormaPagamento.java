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
public enum FormaPagamento implements StringEnum {

    CREDITO("C", "Cartão de Crédito"),
    PIX("P", "Pix"),
    BOLETO("B", "Boleto"),
    DEBITO("D", "Cartão de Débito");

    private final String valor;
    private final String descricao;

    public static class Converter extends StringEnumConverter<FormaPagamento> {
    }
}
