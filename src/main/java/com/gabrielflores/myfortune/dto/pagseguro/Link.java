package com.gabrielflores.myfortune.dto.pagseguro;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Objeto contendo as informações de links relacionado ao recurso.
 *
 * @author Douglas Pasqualin <douglas.pasqualin@gmail.com>
 */
@Data
@NoArgsConstructor
public class Link {

    /**
     * Indica o relacionamento do recurso ('SELF', ‘PAY’, ‘ACTIVATE',
     * 'INACTIVATE’).
     */
    @JsonProperty("rel")
    private Relacionamento relacionamento;

    /**
     * Endereço HTTP do recurso.
     */
    @JsonProperty("href")
    private String endereco;

    /**
     * Método HTTP em uso ('POST', ‘GET’).
     */
    @JsonProperty("method")
    private String metodo;

    @JsonIgnore
    public Boolean isLinkPagamento() {
        return this.relacionamento.isLinkPagamento();
    }

    @Getter
    public enum Relacionamento {
        SELF, PAY, ACTIVATE, INACTIVATE;

        public boolean isLinkPagamento() {
            return this.equals(Relacionamento.PAY);
        }
    }
}
