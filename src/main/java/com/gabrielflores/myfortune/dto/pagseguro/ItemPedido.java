package com.gabrielflores.myfortune.dto.pagseguro;

import com.gabrielflores.myfortune.model.assinatura.Assinatura;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 *
 * @author Douglas Pasqualin <douglas.pasqualin@gmail.com>
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ItemPedido extends PagSeguroObject {

    /**
     * Nome dado ao item.
     */
    @NotNull
    @JsonProperty("name")
    private String descricao;

    /**
     * Quantidade referente ao item.
     */
    @NotNull
    @JsonProperty("quantity")
    private Integer quantidade;

    /**
     * Valor unitário do item.
     */
    @NotNull
    @JsonProperty("unit_amount")
    private Integer valor;

    public ItemPedido(final Assinatura assinatura) {
        super();
        this.idInterno = String.valueOf(assinatura.getPlano().getId());
        this.descricao = assinatura.getPlano().getDescricao();
        this.quantidade = assinatura.getColeiras().size();
        this.valor = assinatura.getValor().multiply(new BigDecimal(100)).intValueExact();
    }
}
