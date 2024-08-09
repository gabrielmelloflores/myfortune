package com.gabrielflores.myfortune.dto.pagseguro.dadospessoais;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

/**
 *
 * @author Douglas Pasqualin <douglas.pasqualin@gmail.com>
 */
@Data
@NoArgsConstructor
public class TelefonePagSeguro {

    /**
     * Código de area do país. Exemplo 55 (Brasil)
     */
    @NotNull
    @JsonProperty("country")
    private Integer codigoPais;

    /**
     * Código do país no telefone. Apenas telefones com DDI 55 são aceitos. Até
     * 3 caracteres.
     */
    @NotNull
    @JsonProperty("area")
    private Integer ddd;

    /**
     * Telefone do assinante com até 9 caracteres.
     */
    @NotNull
    @JsonProperty("number")
    private String numero;

    /**
     * Indica o tipo de telefone. - MOBILE se for um telefone celular. -
     * BUSINESS se for um telefone comercial. - HOME se for um telefone
     * residencial.
     */
    @NotNull
    @JsonProperty("type")
    private String tipo;

    public TelefonePagSeguro(final String telefone) {
        this.codigoPais = 55;
        this.ddd = NumberUtils.toInt(StringUtils.substringBetween(telefone, "(", ")"), 11);
        this.numero = telefone.substring(telefone.indexOf(")") + 1).replaceAll("[^\\d.]", "").trim();
        this.tipo = "MOBILE";
    }

}
