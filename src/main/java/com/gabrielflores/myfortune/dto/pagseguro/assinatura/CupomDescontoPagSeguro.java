package com.gabrielflores.myfortune.dto.pagseguro.assinatura;

import com.gabrielflores.myfortune.dto.pagseguro.PagSeguroObject;
import com.gabrielflores.myfortune.model.assinatura.CupomDesconto;
import com.gabrielflores.myfortune.model.assinatura.TipoDesconto;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * https://dev.pagbank.uol.com.br/reference/criar-cupom
 *
 * @author Douglas Pasqualin <douglas.pasqualin@gmail.com>
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class CupomDescontoPagSeguro extends PagSeguroObject {

    /**
     * Nome do cupom com até 65 caracteres.
     */
    @NotNull
    @JsonProperty("name")
    private String nome;

    /**
     * Descrição do cupom com até 250 caracteres.
     */
    @JsonProperty("description")
    private String descricao;

    /**
     * Objeto contendo as informações do tipo de desconto do cupom.
     */
    @NotNull
    @JsonProperty("discount")
    private Desconto desconto;

    /**
     * Status do cupom: ACTIVE ou INACTIVE.
     */
    @NotNull
    @JsonProperty("status")
    private String situacao;

    /**
     * Objeto contendo a duração do cupom.
     */
    @NotNull
    @JsonProperty("duration")
    private Duracao duracao;

    /**
     * Quantidade de vezes que um cupom pode ser usado em assinaturas até que
     * ele seja inativado automaticamente. Aceita apenas dígitos e deve ter
     * valor maior que 1.
     */
    @JsonProperty("redemption_limit")
    private Integer quantidade;

    /**
     * Data de expiração do cupom. D+1, não possibilitando selecionar datas
     * inferiores.
     */
    @JsonProperty("exp_at")
    private String dataExpiracao;

    public CupomDescontoPagSeguro(final CupomDesconto cupomDesconto) {
        super();
        this.idInterno = cupomDesconto.getCodigo();
        this.nome = cupomDesconto.getCodigo();
        this.descricao = cupomDesconto.getDescricao();
        this.desconto = new Desconto(cupomDesconto.getTipoDesconto(), cupomDesconto.getValor());
        this.situacao = cupomDesconto.isValido() ? "ACTIVE" : "INACTIVE";
        this.duracao = new Duracao();
        if (Optional.ofNullable(cupomDesconto.getDisponivel()).orElse(0) > 0) {
            this.quantidade = cupomDesconto.getDisponivel();
        }
        if (cupomDesconto.getValidade() != null) {
            this.dataExpiracao = cupomDesconto.getValidade().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }
    }

    @Override
    public String getMD5StringBuilder() {
        final StringBuilder builder = new StringBuilder();
        builder.append(this.getNome())
                .append(this.getDesconto().getTipo())
                .append(this.getDesconto().getValor());
        return builder.toString();
    }

    @Data
    public class Desconto {

        /**
         * Para porcentagem, informe apenas números, de 1 a 100 representando o
         * %. Para valor fixo informe apenas números (de 1 até 999.999.999) com
         * o valor exato do desconto.
         */
        @NotNull
        @JsonProperty("value")
        private Integer valor;

        /**
         * Tipo do desconto, podendo ser: PERCENT ou AMOUNT. PERCENT é quando
         * você quer usar uma porcentagem como desconto e AMOUNT é um valor fixo
         * de desconto.
         */
        @NotNull
        @JsonProperty("type")
        private String tipo;

        public Desconto(final TipoDesconto tipoDesconto, final BigDecimal valor) {
            super();
            this.tipo = switch (tipoDesconto) {
                case PERCENTUAL ->
                    "PERCENT";
                case VALOR ->
                    "AMOUNT";
                default ->
                    throw new IllegalArgumentException("Tipo de desconto não suportado : " + tipoDesconto.getDescricao());
            };
            //Documentação não é clara, mas pelos meus testes o valor é em centavos (igual ao plano)
            this.valor = valor.multiply(new BigDecimal(100)).intValueExact();
        }
    }

    @Data
    public class Duracao {

        /**
         * Tipo de frequência do cupom: ONCE (uma vez), REPEATING (repetir) ou
         * FOREVER (válido até ser desativado).
         */
        @NotNull
        @JsonProperty("type")
        private String tipo;

        /**
         * Quando o duration.type for REPEATING, este atributo deve ser
         * informado com até 10 dígitos, para indicar em quantas cobranças de
         * uma assinatura o cupom será utilizado.
         */
        @JsonProperty("occurrences")
        private Integer ocorrencias;

        public Duracao() {
            super();
            /**
             * Acho que a ideia dessa Classe com esses valores é repetir o cupom
             * em cobranças subsequentes. A ideia que pensei inicialmente é dar
             * um cupom de desconto na hora só, por exemplo, na contratação e
             * não ficar repetindo em cada cobrança. Pode ser revista esta
             * lógica.
             *
             * Se entendi corretamente, precisa ser "ONCE", aplica só uma vez na
             * assinatura e as próximas cobranças segue o valor normal
             */
            this.tipo = "ONCE";
        }
    }
}
