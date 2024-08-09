package com.gabrielflores.myfortune.dto.pagseguro;

import com.gabrielflores.myfortune.dto.pagseguro.dadospessoais.ClientePagSeguro;
import com.gabrielflores.myfortune.dto.pagseguro.pagamento.CobrancaPagSeguro;
import com.gabrielflores.myfortune.dto.pagseguro.pagamento.QrCodePagSeguro;
import com.gabrielflores.myfortune.model.assinatura.Assinatura;
import com.gabrielflores.myfortune.model.assinatura.FormaPagamento;
import com.gabrielflores.myfortune.util.CollectionUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * https://dev.pagbank.uol.com.br/reference/objeto-order
 *
 * @author Douglas Pasqualin <douglas.pasqualin@gmail.com>
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class PedidoPagSeguro extends PagSeguroObject {

    /**
     * Objeto contendo as informações do comprador.
     */
    @NotNull
    @JsonProperty("customer")
    private ClientePagSeguro cliente;

    /**
     * Contém as informações dos itens inseridos no pedido.
     */
    @NotNull
    @JsonProperty("items")
    private List<ItemPedido> itensPedido;

    /**
     * Opcional. Contém as informações de entrega do pedido.
     */
    // @JsonProperty("shipping")
    // private EnderecoPagSeguro shipping;
    /**
     * Objeto contendo os QR Codes vinculados à um pedido. Ao informar o amount,
     * o QR code será gerado automaticamente e pode ser pago com aplicativos de
     * outras instituições (Pix). Para que o QR Code aceite o pagamento Pix, o
     * vendedor precisa ter pelo menos uma chave de endereçamento ativa
     * vinculada a sua conta PagBank. Caso o vendedor tenha mais de uma chave de
     * endereçamento cadastrada no PagBank, priorizaremos a utilização da chave
     * de endereçamento aleatória.
     */
    @JsonProperty("qr_codes")
    private List<QrCodePagSeguro> qrCodes;
    /**
     * Contêm as informações necessárias para a execução da cobrança, definindo
     * os dados do meio de pagamento e o valor do pedido.
     */
    @JsonProperty("charges")
    private List<CobrancaPagSeguro> cobrancas;

    /**
     * Contém as URLs que receberão as notificações do pedido (por ora, somente
     * aceitamos uma url apenas. Aceitaremos mais URLs em breve.)
     */
    @JsonProperty("notification_urls")
    private List<String> webhooks;

    public PedidoPagSeguro(final Assinatura assinatura, final FormaPagamento formaPagamento, final String webhook) {
        super();
        this.cliente = new ClientePagSeguro(assinatura.getUser());
        this.itensPedido = List.of(new ItemPedido(assinatura));
        this.webhooks = List.of(webhook);
        if (FormaPagamento.PIX.equals(formaPagamento)) {
            qrCodes = List.of(new QrCodePagSeguro(assinatura.getValorFinal()));
        }
    }

    @JsonIgnore
    public CobrancaPagSeguro getCobranca() {
        return CollectionUtils.getFirst(this.cobrancas);
    }

    @Override
    public String getMD5StringBuilder() {
        final StringBuilder builder = new StringBuilder();
        final ItemPedido pedido = CollectionUtils.getFirst(this.itensPedido);
        final String valorPix = Optional.ofNullable(qrCodes)
                .map(qr -> CollectionUtils.getFirst(qr))
                .map(qr -> qr.getMontante())
                .map(mon -> mon.getValor())
                .map(Objects::toString)
                .orElse("");
        builder.append(this.cliente.getCpf())
                .append(pedido.getDescricao())
                .append(pedido.getValor())
                .append(valorPix);
        return builder.toString();
    }

}
