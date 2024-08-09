package com.gabrielflores.myfortune.dto.pagseguro.pagamento;

import com.gabrielflores.myfortune.dto.assinatura.pagamento.CartaoCreditoDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author Douglas Pasqualin <douglas.pasqualin@gmail.com>
 */
@Data
@NoArgsConstructor
public class CartaoPagSeguro {

    /**
     * Identificador PagBank do Cartão de Crédito salvo (Cartão Tokenizado pelo
     * PagBank). Função indisponível para o método de pagamento com Cartão de
     * Débito e Token de Bandeira.
     */
    @JsonProperty("id")
    private String idSalvo;

    /**
     * Criptograma do cartão criptografado
     */
    //@JsonProperty("encrypted")
    //private String criptograma;
    /**
     * Número do Cartão de Crédito ou Cartão de Débito.
     */
    @JsonProperty("number")
    private String numero;

    /**
     * Número do Token de Bandeira.
     */
    //@JsonProperty("network_token")
    //private String tokenBandeira;
    /**
     * Mês de expiração do Cartão de Crédito, Cartão de Débito ou Token de
     * Bandeira.
     */
    @JsonProperty("exp_month")
    private Integer mesExpiracao;

    /**
     * Ano de expiração do Cartão de Crédito, Cartão de Débito ou Token de
     * Bandeira.
     */
    @JsonProperty("exp_year")
    private Integer anoExpiracao;

    /**
     * Código de Segurança do Cartão de Crédito, Cartão de Débito ou Token de
     * Bandeira.
     */
    @JsonProperty("security_code")
    private String codigoSeguranca;

    /**
     * Indica se o cartão deverá ser armazenado no PagBank para futuras compras.
     * ️Função indisponível para o método de pagamento com Cartão de Débito e
     * Token de Bandeira.
     */
    @JsonProperty("store")
    private Boolean armazenarCartao;

    /**
     * Contém as informações do portador do Cartão de Crédito, Cartão de Débito
     * e Token de Bandeira.
     */
    @JsonProperty("holder")
    private PortadorCartao portador;

    /**
     * Objeto contendo os dados adicionais de Tokenização de Bandeira. Deve ser
     * enviado quando um Cartão de Crédito ou Débito Tokenizado pelas bandeiras
     * Visa ou Mastercard é utilizado.
     */
    //@JsonProperty("token_data")
    //private Object dadosToken;
    public CartaoPagSeguro(final CartaoCreditoDto cartaoCreditoDto) {
        this.armazenarCartao = Boolean.TRUE;
        this.numero = cartaoCreditoDto.getNumero();
        this.mesExpiracao = cartaoCreditoDto.getMesExpiracao();
        this.anoExpiracao = cartaoCreditoDto.getAnoExpiracao();
        this.codigoSeguranca = cartaoCreditoDto.getCodigoSeguranca();
        this.portador = new PortadorCartao(cartaoCreditoDto.getNomePortador());
    }

    /**
     * Objeto contendo os dados adicionais de Tokenização de Bandeira. Deve ser
     * enviado quando um Cartão de Crédito ou Débito Tokenizado pelas bandeiras
     * Visa ou Mastercard é utilizado.
     */
    //@JsonProperty("token_data")
    //private Object dadosToken;
    @Data
    public class PortadorCartao {

        /**
         * Nome do portador do Cartão de Crédito, Cartão de Débito e Token de
         * Bandeira.
         */
        @JsonProperty("name")
        private String nome;

        public PortadorCartao() {
            super();
        }

        public PortadorCartao(final String nome) {
            this();
            this.nome = nome;
        }

    }
}
