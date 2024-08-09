package com.gabrielflores.myfortune.dto.pagseguro.assinatura;

import com.gabrielflores.myfortune.dto.pagseguro.PagSeguroObject;
import com.gabrielflores.myfortune.dto.pagseguro.dadospessoais.EnderecoPagSeguro;
import com.gabrielflores.myfortune.dto.pagseguro.dadospessoais.TelefonePagSeguro;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * https://dev.pagbank.uol.com.br/reference/criar-assinante
 *
 * @author Douglas Pasqualin <douglas.pasqualin@gmail.com>
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class AssinantePagSeguro extends PagSeguroObject {

    /**
     * Nome completo do assinante. Até 150 caracteres e não pode ser vazio.
     */
    @NotNull
    @JsonProperty("name")
    private String nome;

    /**
     * E-mail válido do assinante com até 60 caracteres.
     */
    @NotNull
    @JsonProperty("email")
    private String email;

    /**
     * Número do documento do assinante, CPF com 11 dígitos e CNPJ com 14
     * dígitos numéricos. Não aceita máscaras.
     */
    @NotNull
    @JsonProperty("tax_id")
    private String cpf;

    /**
     * Objeto contendo o(s) telefone(s) do assinante. Deve conter no mínimo um
     * telefone.
     */
    @NotNull
    @JsonProperty("phones")
    private List<TelefonePagSeguro> telefones;

    /**
     * Data de nascimento do assinante. Exemplo 2000-12-20
     */
    @NotNull
    @JsonProperty("birth_date")
    private String dataNascimento;

    /**
     * Objeto contendo as informações de endereço do assinante.
     */
    @JsonProperty("address")
    private EnderecoPagSeguro endereco;

}
