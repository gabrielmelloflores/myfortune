package com.gabrielflores.myfortune.dto.pagseguro.dadospessoais;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 *
 * @author Douglas Pasqualin <douglas.pasqualin@gmail.com>
 */
@Data
public class EnderecoPagSeguro {

    /**
     * Logradouro do endereço com até 150 caracteres e não pode ser vazio.
     */
    @NotNull
    @JsonProperty("street")
    private String rua;

    /**
     * Número do endereço com até 8 caracteres e não pode ser vazio.
     */
    @NotNull
    @JsonProperty("number")
    private String numero;

    /**
     * Complemento do endereço com até 40 caracteres, não aceita espaços entre
     * palavras.
     */
    @NotNull
    @JsonProperty("complement")
    private String complemento;

    /**
     * Bairro com até 60 caracteres e não pode ser vazio.
     */
    @NotNull
    @JsonProperty("locality")
    private String bairro;

    /**
     * Cidade com até 60 caracteres e não pode ser vazio.
     */
    @NotNull
    @JsonProperty("city")
    private String cidade;

    /**
     * Estado (sigla) com 2 caracteres. Exemplo: MG e não pode ser vazio.
     */
    @NotNull
    @JsonProperty("region_code")
    private String estado;

    /**
     * País em formato ISO-alpha3. Exemplo BRA.
     */
    @NotNull
    @JsonProperty("country")
    private String pais;

    /**
     * CEP do endereço com 8 caracteres. Apenas dígitos numéricos e não pode ser
     * vazio.
     */
    @NotNull
    @JsonProperty("postal_code")
    private String cep;
}
