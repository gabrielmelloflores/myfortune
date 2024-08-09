package com.gabrielflores.myfortune.dto.pagseguro.transacao;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

/**
 *
 * @author Douglas Pasqualin <douglas.pasqualin@gmail.com>
 */
@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class FormaPagamento {

    /**
     * Código identificador do meio de pagamento. Informa um código que
     * identifica o meio de pagamento usado pelo comprador. O meio de pagamento
     * descreve a bandeira de cartão de crédito utilizada ou banco escolhido
     * para um débito online.
     */
    @XmlElement(name = "code")
    private Integer codigo;

    /**
     * Informa o tipo do meio de pagamento usado pelo comprador. Este tipo
     * agrupa diversos meios de pagamento e determina de forma geral o
     * comportamento da transação.
     */
    @XmlElement(name = "type")
    private MeioPagamento meioPagamento;

    @XmlType(name = "status")
    @XmlEnum
    @AllArgsConstructor
    @Getter
    public enum MeioPagamento {
        /**
         * Cartão de crédito: o comprador escolheu pagar a transação com cartão
         * de crédito.
         */
        @XmlEnumValue("1")
        CARTAO_CREDITO(1),
        /**
         * Boleto: o comprador optou por pagar com um boleto bancário.
         */
        @XmlEnumValue("2")
        BOLETO(2),
        /**
         * Débito online (TEF): o comprador optou por pagar a transação com
         * débito online de algum dos bancos conveniados.
         */
        @XmlEnumValue("3")
        DEBITO_ONLINE(3),
        /**
         * Saldo PagSeguro: o comprador optou por pagar a transação utilizando o
         * saldo de sua conta PagSeguro.
         */
        @XmlEnumValue("4")
        SALDO_PAGSEGURO(4),
        /**
         * Oi Paggo *: o comprador escolheu pagar sua transação através de seu
         * celular Oi.
         */
        @XmlEnumValue("5")
        OI_PAGGO(5),
        /**
         * Depósito em conta: o comprador optou por fazer um depósito na conta
         * corrente do PagSeguro. Ele precisará ir até uma agência bancária,
         * fazer o depósito, guardar o comprovante e retornar ao PagSeguro para
         * informar os dados do pagamento. A transação será confirmada somente
         * após a finalização deste processo, que pode levar de 2 a 13 dias
         * úteis.
         */
        @XmlEnumValue("7")
        DEPOSITO(7),
        /**
         * Cartão Emergencial Caixa (Débito): disponível apenas nos checkouts
         * com interface do PagSeguro (redirect e lightbox).
         */
        @XmlEnumValue("8")
        CARTAO_EMERGENCIAL(8),
        /**
         * PIX: o comprador escolheu pagar a transação utilizando o PIX.
         */
        @XmlEnumValue("11")
        PIX(11);

        private final Integer meioPagamentoItem;
    }

}
