package com.gabrielflores.myfortune.dto.pagseguro.assinatura;

import com.gabrielflores.myfortune.dto.pagseguro.PagSeguroObject;
import com.gabrielflores.myfortune.model.assinatura.Plano;
import com.gabrielflores.myfortune.model.assinatura.VigenciaPlano;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

/**
 * https://dev.pagbank.uol.com.br/reference/criar-plano
 *
 * @author Douglas Pasqualin <douglas.pasqualin@gmail.com>
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class PlanoPagSeguro extends PagSeguroObject {

    /**
     * Nome do plano na sua aplicação. Até 65 caracteres.
     */
    @NotNull
    @JsonProperty("name")
    private String nome;

    /**
     * Descrição do plano na sua aplicação. Até 250 caracteres.
     */
    @JsonProperty("description")
    private String descricao;

    /**
     * Objeto contendo as informações do valor a ser cobrado.
     */
    @NotNull
    @JsonProperty("amount")
    private Montante montante;

    /**
     * Taxa de contratação a ser cobrada na assinatura em centavos de Real. Caso
     * não queira cobrar taxa de contratação para o plano, o campo setup_fee
     * precisa ficar em branco ou não enviar esse atributo.
     */
    @JsonProperty("setup_fee")
    private Integer taxaAdesao;

    /**
     * Default é mensal. Objeto contendo as definições sobre o intervalo das
     * cobranças.
     */
    @JsonProperty("interval")
    private Vigencia vigencia;

    /**
     * Quantidade de ciclos (faturas) que a assinatura terá até expirar (se não
     * informar, não haverá expiração).
     */
    @JsonProperty("billing_cycles")
    private Integer ciclosExpiracao;

    /**
     * Default é sem experimentação. Objeto contendo as informações do período
     * trial.
     */
    @JsonProperty("trial")
    private Experimentacao experimentacao;

    /**
     * Quantidade máxima de assinaturas do plano (não há limite se não
     * informar).
     */
    @JsonProperty("limit_subscriptions")
    private Integer limiteAssinaturas;

    /**
     * Formas de pagamentos aceitas no plano. BOLETO, CREDIT_CARD. Caso o
     * atributo não seja informado, a forma de pagamento default é CREDIT_CARD.
     * (case insensitive, mas nosso padrão de retorno maiúsculo)
     */
    @NotNull
    @JsonProperty("payment_method")
    private List<String> formasPagamento;

    public PlanoPagSeguro(final Plano plano) {
        super();
        this.idInterno = plano.getId().toString();
        this.nome = StringUtils.substring(plano.getDescricao(), 0, 65);
        this.montante = new Montante(plano.getValor());
        this.vigencia = new Vigencia(plano.getVigencia(), plano.getDias());
        this.experimentacao = new Experimentacao(Boolean.FALSE);
        this.formasPagamento = List.of("CREDIT_CARD");
    }

    @Override
    public String getMD5StringBuilder() {
        final StringBuilder builder = new StringBuilder();
        builder.append(this.getNome())
                .append(this.getMontante().getValor())
                .append(this.getVigencia().getPeriodicidade())
                .append(this.getVigencia().getIntervalo());
        return builder.toString();
    }

    @Data
    public class Montante {

        /**
         * Valor do plano a ser cobrado em centavos. Apenas números inteiros
         * positivos. Até 9 caracteres. Ex: R$ 1.500,99 = 150099
         */
        @NotNull
        @JsonProperty("value")
        private Integer valor;

        /**
         * Código de moeda ISO de três letras, em maiúsculas. Por enquanto,
         * apenas o Real brasileiro é suportado (“BRL”)
         */
        @NotNull
        @JsonProperty("currency")
        private String moeda;

        public Montante(final BigDecimal valor) {
            super();
            this.valor = valor.multiply(new BigDecimal(100)).intValueExact();
            this.moeda = "BRL";
        }
    }

    @Data
    public class Vigencia {

        /**
         * A unidade de medida do intervalo de cobrança, o default é MONTH.
         * Opções: DAY, MONTH, YEAR. (case insensitive, mas nosso padrão de
         * retorno maiúsculo)
         */
        @NotNull
        @JsonProperty("unit")
        private String periodicidade;

        /**
         * A duração do intervalo de cobrança, default é 1.
         */
        @NotNull
        @JsonProperty("length")
        private Integer intervalo;

        public Vigencia(final VigenciaPlano vigenciaPlano, final Integer intervalo) {
            super();
            this.periodicidade = switch (vigenciaPlano) {
                case MENSAL ->
                    "MONTH";
                case ANUAL ->
                    "YEAR";
                case DIAS ->
                    "DAY";
                default ->
                    throw new IllegalArgumentException("Vigência não suportada : " + vigenciaPlano.getDescricao());
            };
            this.intervalo = vigenciaPlano.DIAS.equals(vigenciaPlano) ? intervalo : 1;
        }
    }

    @Data
    public class Experimentacao {

        /**
         * Número de dias de trial do plano.
         */
        @JsonProperty("days")
        private Integer dias;

        /**
         * Determina se o trial está ou não habilitado. Opções: TRUE ou FALSE,
         * default é FALSE.
         */
        @NotNull
        @JsonProperty("enabled")
        private Boolean ativo;

        /**
         * Determina se o setup_fee será cobrado antes ou após o período de
         * trial. Opções: TRUE (após) ou FALSE (antes).
         */
        @JsonProperty("hold_setup_fee")
        private Boolean aguardaTaxaAdesao;

        public Experimentacao(final Boolean ativo) {
            super();
            this.ativo = ativo;
        }
    }
}
