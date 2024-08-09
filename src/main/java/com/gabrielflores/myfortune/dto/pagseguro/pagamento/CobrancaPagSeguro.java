package com.gabrielflores.myfortune.dto.pagseguro.pagamento;

import com.gabrielflores.myfortune.dto.assinatura.AssinaturaDtoCadastro;
import com.gabrielflores.myfortune.dto.pagseguro.PagSeguroObject;
import com.gabrielflores.myfortune.model.assinatura.Assinatura;
import com.gabrielflores.myfortune.model.assinatura.FormaPagamento;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * https://dev.pagbank.uol.com.br/reference/objeto-charge
 *
 * @author Douglas Pasqualin <douglas.pasqualin@gmail.com>
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class CobrancaPagSeguro extends PagSeguroObject {

    /**
     * Descrição da cobrança.
     */
    @JsonProperty("description")
    private String descricao;

    /**
     * Especifica a quantia da cobrança, com o valor e moeda
     */
    @JsonProperty("amount")
    private Montante montante;

    /**
     * Contém as informações do método de pagamento da cobrança.
     */
    @JsonProperty("payment_method")
    private FormaPagamentoPagSeguro formaPagamentoPagSeguro;

    /**
     * URLs que serão notificadas em toda alteração ocorrida na cobrança️
     * Necessário que seja em ambiente seguro com SSL (HTTPS)
     */
    @JsonProperty("notification_urls")
    private List<String> webhooks;

    /* ========= properties que existem somente no response ========== */
    /**
     * Status da cobrança.
     */
    @JsonProperty("status")
    private Situacao situacao;

    /**
     * Data e horário em que a cobrança foi paga (capturada).
     */
    @JsonProperty("paid_at")
    private String dataPagamento;

    /**
     * Contém informações de resposta do provedor de pagamento.
     */
    @JsonProperty("payment_response")
    private SituacaoPagamento situacaoPagamento;

    public CobrancaPagSeguro(final Assinatura assinatura, final AssinaturaDtoCadastro assinaturaDtoCadastro, final String webhook) {
        super();
        this.descricao = assinatura.getPlano().getDescricao();
        this.montante = new Montante(assinatura.getValorFinal());
        this.formaPagamentoPagSeguro = new FormaPagamentoPagSeguro(assinaturaDtoCadastro);
        this.webhooks = List.of(webhook);
    }

    @JsonIgnore
    public LocalDateTime getDataPagamento() {
        return Optional.ofNullable(this.dataPagamento)
                .map(dt -> LocalDateTime.parse(dt, DateTimeFormatter.ISO_DATE_TIME))
                .orElse(null);
    }

    /**
     * De -> para, da situação do PagSeguro para a Situação interna do nosso
     * modelo
     *
     * @return
     */
    @JsonIgnore
    public com.gabrielflores.myfortune.model.assinatura.Situacao getSituacaoAssinatura() {
        return switch (this.situacao) {
            case AUTORIZADA ->
                com.gabrielflores.myfortune.model.assinatura.Situacao.EM_ANALISE;
            case EM_ANALISE ->
                com.gabrielflores.myfortune.model.assinatura.Situacao.EM_ANALISE;
            case PAGA ->
                com.gabrielflores.myfortune.model.assinatura.Situacao.QUITADO;
            case CANCELADA ->
                com.gabrielflores.myfortune.model.assinatura.Situacao.CANCELADO;
            case RECUSADA ->
                com.gabrielflores.myfortune.model.assinatura.Situacao.NAO_AUTORIZADO;
        };
    }

    /**
     * De -> para, da forma de pagamento do PagSeguro para a forma de pagamento
     * interna do nosso modelo
     *
     * @return
     */
    @JsonIgnore
    public FormaPagamento getFormaPagamento() {
        if (Situacao.PAGA.equals(this.situacao)) {
            return switch (this.formaPagamentoPagSeguro.getFormaPagamento()) {
                case "CREDIT_CARD" ->
                    FormaPagamento.CREDITO;
                case "DEBIT_CARD" ->
                    FormaPagamento.DEBITO;
                case "BOLETO" ->
                    FormaPagamento.BOLETO;
                case "PIX" ->
                    FormaPagamento.PIX;
                default ->
                    null;
            };
        }
        return null;
    }

    @JsonIgnore
    public String getIdTransacao() {
        return Optional.ofNullable(this.getId()).map(id -> id.replace("CHAR_", "")).orElse(null);
    }

    @Data
    @NoArgsConstructor
    public class Montante {

        /**
         * Valor a ser cobrado em centavos. Apenas números inteiros positivos.
         * Exemplo: R$ 1.500,99 = 150099
         */
        @JsonProperty("value")
        private Integer valor;

        /**
         * Código de moeda ISO de três letras, em maiúsculo. Por enquanto,
         * apenas o Real brasileiro é suportado (BRL).
         */
        @JsonProperty("currency")
        private String moeda;

        public Montante(final BigDecimal valor) {
            super();
            this.valor = valor.multiply(new BigDecimal(100)).intValueExact();
            this.moeda = "BRL";
        }

    }

    @Data
    @NoArgsConstructor
    public class SituacaoPagamento {

        /**
         * Código PagBank que indica o motivo da resposta de autorização no
         * pagamento, tanto para pagamento autorizado, quanto para negado.
         * Exemplo: 20000
         */
        @JsonProperty("code")
        private Integer codigo;
        /**
         * Mensagem amigável descrevendo motivo da não aprovação ou autorização
         * da cobrança. Compatível com o padrão ABECS - Normativo 21. Exemplo:
         * SUCESSO
         */
        @JsonProperty("message")
        private String mensagem;
        /**
         * NSU da autorização, caso o pagamento seja aprovado pelo Emissor.
         * Exemplo: 032416400102
         */
        @JsonProperty("reference")
        private String reference;
    }

    public enum Situacao {
        /**
         * indica que a cobrança está pré-autorizada.
         */
        @JsonProperty("AUTHORIZED")
        AUTORIZADA,
        /**
         * indica que a cobrança está paga (capturada).
         */
        @JsonProperty("PAID")
        PAGA,
        /**
         * indica que o comprador optou por pagar com um Cartão de Crédito e o
         * PagBank está analisando o risco da transação.
         */
        @JsonProperty("IN_ANALYSIS")
        EM_ANALISE,
        /**
         * Indica que a cobrança foi negada pelo PagBank ou Emissor.
         */
        @JsonProperty("DECLINED")
        RECUSADA,
        /**
         * indica que a cobrança foi cancelada.
         */
        @JsonProperty("CANCELED")
        CANCELADA;
    }
}
