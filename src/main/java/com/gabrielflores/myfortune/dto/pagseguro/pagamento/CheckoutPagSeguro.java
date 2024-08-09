package com.gabrielflores.myfortune.dto.pagseguro.pagamento;

import com.gabrielflores.myfortune.dto.pagseguro.ItemPedido;
import com.gabrielflores.myfortune.dto.pagseguro.Link;
import com.gabrielflores.myfortune.dto.pagseguro.PagSeguroObject;
import com.gabrielflores.myfortune.dto.pagseguro.PedidoPagSeguro;
import com.gabrielflores.myfortune.dto.pagseguro.dadospessoais.ClientePagSeguro;
import com.gabrielflores.myfortune.model.assinatura.Assinatura;
import com.gabrielflores.myfortune.util.CollectionUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * https://dev.pagbank.uol.com.br/reference/objeto-checkout
 *
 * @author Douglas Pasqualin <douglas.pasqualin@gmail.com>
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CheckoutPagSeguro extends PagSeguroObject {

    /**
     * Data de expiração do checkout. Caso não seja informado, a data de
     * expiração será a data e hora atual + 2 horas. Padrão esperado: ISO-8601
     * Date time with timezone offset
     */
    @JsonProperty("expiration_date")
    private String dataExpiracao;

    /**
     * Dados pessoais do cliente informado pelo vendedor. Obrigatório caso
     * "customer_modifiable" for false.
     */
    @JsonProperty("customer")
    private ClientePagSeguro cliente;

    /**
     * Indicador da imutabilidade dos dados pessoais na criação do checkout,
     * possibilitando pular o step de dados pessoais. Caso não informado, o
     * valor padrão é true. Regras do campo: O objeto customer torna-se
     * obrigatório caso o valor informado seja false.
     */
    @JsonProperty("customer_modifiable")
    private Boolean permiteAlterarCliente;

    /**
     * Lista de produtos informado pelo vendedor, deve conter no mínimo Nome,
     * Quantidade e Valor unitário.
     */
    @JsonProperty("items")
    private List<ItemPedido> itensPedido;

    /**
     * Valor adicional a ser cobrado (Em centavos). BigInteger (0 - 999999900)
     */
    @JsonProperty("additional_amount")
    private Integer valorAdicional;

    /**
     * Valor a ser descontado do valor total (Em centavos). BigInteger (0 -
     * 999999900)
     */
    @JsonProperty("discount_amount")
    private Integer valorDesconto;

    /**
     * Dados de entrega do produto, caso não informado será considerado que não
     * há necessidade de entrega, caso informado, deve indicar se o valor da
     * entrega é Fixo ou Grátis.
     */
    //@JsonProperty("shipping")
    //private Object entrega;
    /**
     * Meios de pagamento aceitos.
     */
    @JsonProperty("payment_methods")
    private List<FormaPagamentoPagSeguro> formasPagamento;
    /**
     * Configurações dos meios de pagamento.
     */
    @JsonProperty("payment_methods_configs")
    private List<ConfigFormaPagamentoPagSeguro> configFormasPagamento;
    /**
     * Texto adicional que será apresentado junto ao nome do estabelecimento na
     * fatura do cartão de crédito do comprador. Limite: 17 caracteres
     */
    @JsonProperty("soft_descriptor")
    private String descricaoCobrancaFatura;

    /**
     * URL para redirecionamento após pagamento. Limite: 255 caracteres
     */
    @JsonProperty("redirect_url")
    private String urlRetorno;

    /**
     * Lista de URLs para notificação de status do checkout. Limite: (5-100
     * caracteres)
     */
    @JsonProperty("notification_urls")
    private List<String> webhooks;

    /**
     * Lista de URLs para notificação dos status de pagamento. Limite: (5-100
     * caracteres)
     */
    @JsonProperty("payment_notification_urls")
    private List<String> webhooksPagamento;

    /* ========= properties que existem somente no response ========== */
    /**
     * Status do checkout ('ACTIVE', 'INACTIVE', 'EXPIRED'). Por padrão, o
     * checkout é criado com status ACTIVE.
     */
    @JsonProperty("status")
    private Situacao situacao;

    /**
     * Objeto contendo as informações de links relacionado ao recurso.
     */
    @JsonProperty("links")
    private List<Link> links;

    /**
     * pedido que o checkout irá gerar automaticamente
     */
    @JsonProperty("orders")
    private List<PedidoPagSeguro> pedidos;

    @JsonIgnore
    public boolean isAtivo() {
        return Situacao.ATIVO.equals(this.situacao);
    }

    public CheckoutPagSeguro(final Assinatura assinatura, final String urlRetorno, final String webhook, final String webhookCheckout) {
        super();
        final ZonedDateTime expiracao = ZonedDateTime.now().plusHours(1);
        //final ZonedDateTime expiracao = ZonedDateTime.now().plusMinutes(2);
        this.dataExpiracao = DateTimeFormatter.ISO_DATE_TIME.format(expiracao);
        this.cliente = new ClientePagSeguro(assinatura.getUser());
        this.permiteAlterarCliente = !this.cliente.isDadosCompletos();
        this.itensPedido = List.of(new ItemPedido(assinatura));
        this.valorAdicional = 0;
        this.valorDesconto = assinatura.getValorDesconto().multiply(new BigDecimal(100)).intValueExact();
        //Reunião Roger 10/10/23: Aceitar somente cartão e pix
        this.formasPagamento = List.of(new FormaPagamentoPagSeguro("CREDIT_CARD"), new FormaPagamentoPagSeguro("PIX"));
        final Integer limiteParcelas = assinatura.getPlano().getLimiteParcelas();
        //Parcelamento sem juros é direto nas configurações do PagSeguro, "Criar Promoção"
        if (limiteParcelas != null) {
            this.configFormasPagamento = List.of(new ConfigFormaPagamentoPagSeguro("CREDIT_CARD", limiteParcelas));
        }
        this.descricaoCobrancaFatura = "Pet Connect";
        this.urlRetorno = urlRetorno;
        this.webhooks = List.of(webhookCheckout);
        this.webhooksPagamento = List.of(webhook);
    }

    @JsonIgnore
    public Boolean isExpirado() {
        final ZonedDateTime dataExpiracao = ZonedDateTime.parse(this.dataExpiracao, DateTimeFormatter.ISO_DATE_TIME);
        return ZonedDateTime.now().isAfter(dataExpiracao);
    }

    @JsonIgnore
    public String getLinkCheckout() {
        return CollectionUtils.streamOf(links)
                .filter(l -> l.isLinkPagamento())
                .findFirst()
                .map(p -> p.getEndereco())
                .orElse(null);
    }

    @JsonIgnore
    public PedidoPagSeguro getPedido() {
        return CollectionUtils.getFirst(this.pedidos);
    }

    @Getter
    public enum Situacao {
        @JsonProperty("ACTIVE")
        ATIVO,
        @JsonProperty("INACTIVE")
        INATIVO,
        @JsonProperty("EXPIRED")
        EXPIRADO;
    }

}
