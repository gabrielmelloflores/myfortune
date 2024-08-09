package com.gabrielflores.myfortune.dto.pagseguro.pagamento;

import com.gabrielflores.myfortune.dto.pagseguro.dadospessoais.EnderecoPagSeguro;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 *
 * @author Douglas Pasqualin <douglas.pasqualin@gmail.com>
 */
@Data
public class BoletoPagSeguro {

    /**
     * Data de vencimento do Boleto. Formato: YYYY-MM-DD
     */
    @JsonProperty("due_date")
    private String dataVencimento;

    /**
     * Objeto contendo as linhas de instrução do Boleto.
     */
    @JsonProperty("instruction_lines")
    private Instrucoes instrucoes;

    @Data
    public class Instrucoes {

        /**
         * Primeira linha de instruções sobre o pagamento do Boleto.
         */
        @JsonProperty("line_1")
        private String linha1;

        /**
         * Segunda linha de instruções sobre o pagamento do Boleto.
         */
        @JsonProperty("line_2")
        private String linha2;
    }

    @Data
    public class Responsavel {

        /**
         * Nome do responsável pelo pagamento do Boleto.
         */
        @JsonProperty("name")
        private String nome;

        /**
         * Número do documento do responsável pelo pagamento do Boleto.
         */
        @JsonProperty("tax_id")
        private String cpf;

        /**
         * E-mail do responsável pelo pagamento do Boleto.
         */
        @JsonProperty("email")
        private String email;

        /**
         * Objeto contendo as informações de endereço do responsável pelo
         * pagamento do Boleto. <br/>
         * OBS: pelos meus testes é obrigatório para boleto!
         */
        @JsonProperty("address")
        private EnderecoPagSeguro endereco;
    }
}
