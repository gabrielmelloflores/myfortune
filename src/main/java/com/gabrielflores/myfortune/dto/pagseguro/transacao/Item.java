package com.gabrielflores.myfortune.dto.pagseguro.transacao;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import java.math.BigDecimal;
import lombok.Data;

/**
 *
 * @author Douglas Pasqualin <douglas.pasqualin@gmail.com>
 */
@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class Item {

    /**
     * Identificador do item. Identifica o item da transação. Este identificador
     * deve ser único por transação e foi informado por você no fluxo de
     * pagamento. <br>
     * Presença: Obrigatória.<br>
     * Tipo: Texto.<br>
     * Formato: Livre.
     */
    @XmlElement(name = "id", required = true)
    private String id;

    /**
     * Descrição do item. Descreve o item da transação. A descrição é um texto
     * explicativo do item que você especificou no fluxo de pagamento.<br>
     * Presença: Obrigatória.<br>
     * Tipo: Texto.<br>
     * Formato: Livre.
     */
    @XmlElement(name = "description", required = true)
    private String descricao;

    /**
     * Item's unit value. Informa o preço unitário do item da transação. Este é
     * o valor que foi especificado no fluxo de pagamento.<br>
     * Presença: Obrigatória.<br>
     * Tipo: Número.<br>
     * Formato: Decimal, com duas casas decimais separadas por ponto (p.e.,
     * 1234.56).
     */
    @XmlElement(name = "amount", required = true)
    private BigDecimal valor;

    /**
     * Quantidade do item. Informa a quantidade do item da transação. Está é a
     * quantidade que foi especificada no fluxo de pagamento.<br>
     * Presença: Obrigatória.<br>
     * Tipo: Número.<br>
     * Formato: Um número inteiro maior ou igual a 1 e menor ou igual a 999.
     */
    @XmlElement(name = "quantity", required = true)
    private Integer quantidade;
}
