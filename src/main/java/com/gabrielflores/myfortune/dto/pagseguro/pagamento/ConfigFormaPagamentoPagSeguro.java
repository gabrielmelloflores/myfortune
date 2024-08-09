package com.gabrielflores.myfortune.dto.pagseguro.pagamento;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author Douglas Pasqualin <douglas.pasqualin@gmail.com>
 */
@Data
@NoArgsConstructor
public class ConfigFormaPagamentoPagSeguro {

    /**
     * Meio de pagamento que receberá algum tipo de configuração ('CREDIT_CARD',
     * 'DEBIT_CARD').
     */
    @JsonProperty("type")
    private String formaPagamento;

    /**
     * Configurações dos meios de pagamento.
     */
    @JsonProperty("config_options")
    private List<Configuracao> configuracoes;

    public ConfigFormaPagamentoPagSeguro(final String formaPagamento, final Integer limiteParcelas) {
        super();
        this.formaPagamento = formaPagamento;
        this.configuracoes = List.of(new Configuracao(limiteParcelas));
    }

    @Data
    @NoArgsConstructor
    public static class Configuracao {

        @JsonProperty("option")
        public String configuracao;

        @JsonProperty("value")
        public String valor;

        public Configuracao(final Integer limiteParcelas) {
            super();
            this.configuracao = "installments_limit";
            this.valor = String.valueOf(limiteParcelas);
        }
    }
}
