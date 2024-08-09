package com.gabrielflores.myfortune.model.assinatura;

import com.gabrielflores.myfortune.converter.StringEnumConverter;
import com.gabrielflores.myfortune.model.entity.StringEnum;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Situação do pagamento <br>
 * Representa também o 'status' de uma 'charge' no pagbank
 *
 * @author Douglas Pasqualin <douglas.pasqualin@gmail.com>
 */
@Getter
@RequiredArgsConstructor
public enum Situacao implements StringEnum {

    QUITADO("Q", "Quitado / pago"), //PAID
    PENDENTE("P", "Pendente / aguardando"), //Quando criamos a parcela aqui pelo sistema
    NAO_AUTORIZADO("N", "Não autorizado"), //DECLINED
    EM_ANALISE("A", "Em análise/autorizado"), //IN_ANALYSIS ou AUTHORIZED
    CANCELADO("C", "Cancelado pelo meio de pagamento"), //CANCELED
    ENCERRADO("E", "Encerrado, não pago"),
    CANCELADO_User("T", "Cancelado pelo User"); //A principio não vai existir, não vai ter recorrência. Confirmar.

    private final String valor;
    private final String descricao;

    public static class Converter extends StringEnumConverter<Situacao> {
    }

    public boolean permiteNovoCheckout() {
        return this.equals(Situacao.NAO_AUTORIZADO) || this.equals(Situacao.ENCERRADO);
    }

    public boolean isPendente() {
        return this.equals(Situacao.PENDENTE);
    }

    public boolean isQuitado() {
        return this.equals(Situacao.QUITADO);
    }

    public boolean isPago() {
        return isQuitado();
    }
}
