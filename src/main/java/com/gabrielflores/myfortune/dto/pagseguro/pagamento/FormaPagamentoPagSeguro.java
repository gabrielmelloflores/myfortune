package com.gabrielflores.myfortune.dto.pagseguro.pagamento;

import com.gabrielflores.myfortune.dto.assinatura.AssinaturaDtoCadastro;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Contém as informações do método de pagamento da cobrança.
 *
 * @author Douglas Pasqualin <douglas.pasqualin@gmail.com>
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FormaPagamentoPagSeguro {

    /**
     * Indica o método de pagamento usado na cobrança. <br/>
     * CREDIT_CARD, DEBIT_CARD, BOLETO, PIX
     */
    @JsonProperty("type")
    private String formaPagamento;

    /**
     * Quantidade de parcelas. Obrigatório para o método de pagamento Cartão de
     * Crédito.
     */
    @JsonProperty("installments")
    private Integer parcelas;

    /**
     * Parâmetro que indica se uma transação de cartão de crédito deve ser
     * apenas pré-autorizada (reserva o valor da cobrança no cartão do cliente
     * de 6 até 29 dias) ou se a transação deve ser capturada automaticamente
     * (cobrança realizada em apenas um passo).
     */
    @JsonProperty("capture")
    private Boolean preAutorizar;

    /**
     * Parâmetro responsável pelo que será exibido como Nome na Fatura do
     * cliente. Aplicável no momento apenas para Cartão de crédito. Não permite
     * caracteres especiais. Acentuações serão substituídas por caracteres sem
     * acentos, demais caracteres especiais serão removidos.
     */
    @JsonProperty("soft_descriptor")
    private String descricaoCobrancaFatura;

    /**
     * Objeto contendo os dados de Cartão de Crédito, Cartão de Débito ou Token
     * de Bandeira.
     */
    @JsonProperty("card")
    private CartaoPagSeguro cartaoCredito;

    /**
     * Objeto contendo os dados adicionais de autenticação vínculados à uma
     * transação. Obrigatório para o método de pagamento com cartão de débito.
     */
    //JsonProperty("authentication_method")
    //private Object metodoAutenticacao;
    /**
     * Objeto contendo os dados para geração do boleto.️ Obrigatório para o
     * método de pagamento com Boleto Bancário.
     */
    @JsonProperty("boleto")
    private BoletoPagSeguro boleto;

    public FormaPagamentoPagSeguro() {
        super();
        this.preAutorizar = Boolean.FALSE;
    }

    public FormaPagamentoPagSeguro(final AssinaturaDtoCadastro assinaturaDtoCadastro) {
        this();
        this.descricaoCobrancaFatura = "PetConnect";
//        this.formaPagamento = switch (assinaturaDtoCadastro.getFormaPagamento()) {
//            case CREDITO ->
//                "CREDIT_CARD";
//            case DEBITO ->
//                "DEBIT_CARD";
//            case BOLETO ->
//                "BOLETO";
//            case PIX ->
//                throw new IllegalArgumentException("Pagamento via PIX é feito diretamente nos dados do pedido e não nos dados da cobrança");
//            default ->
//                throw new IllegalArgumentException("Tipo de pagamento não suportado : " + assinaturaDtoCadastro.getFormaPagamento().getDescricao());
//        };
//        if ("CREDIT_CARD".equals(this.formaPagamento)) {
//            this.cartaoCredito = new CartaoPagSeguro(assinaturaDtoCadastro.getCartaoCredito());
//            this.parcelas = assinaturaDtoCadastro.getCartaoCredito().getTotalParcelas();
//        } else if ("BOLETO".equals(this.formaPagamento)) {
//            //TODO: vamos aceitar boleto? Precisa dos dados do endereço do User ...
//            throw new IllegalArgumentException("Forma de pagamento 'Boleto' não suportada");
//        } else {
//            throw new IllegalArgumentException("Forma de pagamento não suportada: " + assinaturaDtoCadastro.getFormaPagamento().getDescricao());
    }

    public FormaPagamentoPagSeguro(final String formaPagamento) {
        this.formaPagamento = formaPagamento;
    }
}
