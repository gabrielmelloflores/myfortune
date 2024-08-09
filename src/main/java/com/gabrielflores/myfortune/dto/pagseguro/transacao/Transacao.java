package com.gabrielflores.myfortune.dto.pagseguro.transacao;

import com.gabrielflores.myfortune.model.assinatura.Situacao;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;

/**
 * hhttps://dev.pagbank.uol.com.br/v1/docs/api-notificacao-v1#consultando-uma-notificacao-de-transacao
 * https://dev.pagbank.uol.com.br/reference/webhooks
 *
 * @author Douglas Pasqualin <douglas.pasqualin@gmail.com>
 */
@Data
@XmlRootElement(name = "transaction")
@XmlAccessorType(XmlAccessType.FIELD)
public class Transacao {

    /**
     * Data da criação da transação. Informa o momento em que a transação foi
     * criada.<br>
     * Presença: Obrigatória.<br>
     * Tipo: Data/hora.<br>
     * Formato: YYYY-MM-DDThh:mm:ss.sTZD, o formato oficial do W3C para datas.
     */
    @Getter(AccessLevel.NONE)
    @XmlElement(name = "date", required = true)
    private String data;

    /**
     * Data do último evento. Informa o momento em que ocorreu a última
     * alteração no status da transação.<br>
     * Presença: Obrigatória.<br>
     * Tipo: Data/hora.<br>
     * Formato: YYYY-MM-DDThh:mm:ss.sTZD, o formato oficial do W3C para datas.
     */
    @XmlElement(name = "lastEventDate", required = true)
    private String dataUltimoEvento;

    /**
     * Código identificador da transação. Retorna o código que identifica a
     * transação de forma única. <br>
     * Presença: Obrigatória.<br>
     * Tipo: Texto.<br>
     * Formato: Uma sequência de 36 caracteres.
     */
    @XmlElement(name = "code", required = true)
    private String codigo;

    /**
     * Código de referência da transação. Informa o código que foi usado para
     * fazer referência ao pagamento. Este código foi fornecido no momento do
     * pagamento e é útil para vincular as transações do PagSeguro às vendas
     * registradas no seu sistema. <br>
     * Presença: Opcional.<br>
     * Tipo: Texto.<br>
     * Formato: Livre, com o limite de 200 caracteres.
     */
    @XmlElement(name = "reference")
    private String referencia;

    /**
     * Transaction type. Representa o tipo da transação recebida. Os valores
     * mais comuns para este campo e seus respectivos resultados são descritos
     * abaixo. <br>
     *
     * 1- Pagamento: a transação foi criada por um comprador fazendo um
     * pagamento.<br>
     * Este é o tipo mais comum de transação que você irá receber.<br>
     *
     * Outros tipos menos comuns de transações foram omitidos. Note que novos
     * tipos podem ser adicionados em versões futuras da API.<br>
     *
     * Presença: Obrigatória.<br>
     * Tipo: Número.<br>
     * Formato: Inteiro.
     */
    @XmlElement(name = "type", required = true)
    private Integer tipo;

    /**
     * Status da transação. Informa o código representando o status da
     * transação, permitindo que você decida se deve liberar ou não os produtos
     * ou serviços adquiridos.
     */
    @XmlElement(name = "status", required = true)
    private Estado estado;

    /**
     * Origem do cancelamento. Informa a origem do cancelamento da transação:
     * pelas instituições financeiras (Banco Emissor ou Operadora do Cartão) ou
     * pelo PagSeguro.<br>
     * Valor Significado<br>
     * INTERNAL PagSeguro<br>
     * EXTERNAL Instituições Financeiras<br>
     * Presença: Opcional (somente quando transactionStatus igual a 7).<br>
     * Tipo: Texto.<br>
     * Formato: Valores possíveis INTERNAL ou EXTERNAL.
     */
    @XmlElement(name = "cancellationSource")
    private String origemCancelamento;

    /**
     * Informa o tipo do meio de pagamento usado pelo comprador. Este tipo
     * agrupa diversos meios de pagamento e determina de forma geral o
     * comportamento da transação.
     */
    @XmlElement(name = "paymentMethod", required = true)
    private FormaPagamento formaPagamento;

    /**
     * Valor bruto da transação. Informa o valor bruto da transação, calculado
     * pela soma dos preços de todos os itens presentes no pagamento.<br>
     * Presença: Obrigatória.<br>
     * Tipo: Número.<br>
     * Formato: Decimal, com duas casas decimais separadas por ponto ("."). Por
     * exemplo, 1234.56.
     */
    @XmlElement(name = "grossAmount", required = true)
    private BigDecimal valor;

    /**
     * Valor do desconto dado. Informa o valor do desconto dado a compradores
     * que optaram por pagar com débito online ou boleto. Este desconto
     * aplica-se quando você opta por incluir no preço dos produtos o custo do
     * parcelamento de pagamentos com cartão de crédito. O desconto é dado para
     * não onerar os compradores que optaram por meios à vista. <br>
     * Presença: Obrigatória.<br>
     * Tipo: Número.<br>
     * Formato: Decimal, com duas casas decimais separadas por ponto ("."). Por
     * exemplo, 1234.56.
     */
    @XmlElement(name = "discountAmount", required = true)
    private BigDecimal valorDesconto;

    /**
     * Valor líquido da transação. Informa o valor líquido da transação, que
     * corresponde ao valor bruto, menos o valor das taxas. Caso presente, o
     * valor de extraAmount (que pode ser positivo ou negativo) também é
     * considerado no cálculo.<br>
     * Presença: Obrigatória.<br>
     * Tipo: Número.<br>
     * Formato: Decimal, com duas casas decimais separadas por ponto ("."). Por
     * exemplo, 1234.56.
     */
    @XmlElement(name = "netAmount", required = true)
    private BigDecimal valorLiquido;

    /**
     * Valor extra. Informa um valor extra que foi somado ou subtraído do valor
     * pago pelo comprador. Este valor é especificado por você no pagamento e
     * pode representar um valor que você quer cobrar separadamente do comprador
     * ou um desconto que quer dar a ele.<br>
     * Presença: Obrigatória.<br>
     * Tipo: Número.<br>
     * Formato: Decimal, com duas casas decimais separadas por ponto (“.”). Por
     * exemplo, 1234.56 ou -1234.56.
     */
    @XmlElement(name = "extraAmount", required = true)
    private BigDecimal valorExtra;

    /**
     * Data de crédito. Data em que o valor da transação estará disponível na
     * conta do vendedor.<br>
     * Presença: Presente apenas quando o status da transação for um dos
     * seguintes valores:<br>
     * Paga (3), Disponível (4), Em disputa (5) ou Devolvida (6).<br>
     * Tipo: Data/hora.<br>
     * Formato: YYYY-MM-DDThh:mm:ss.sTZD, o formato oficial do W3C para datas.
     * Veja mais sobre formatação de datas.
     */
    @XmlElement(name = "escrowEndDate")
    private String dataCredito;

    /**
     * Número de itens da transação. Aponta o número de itens contidos nesta
     * transação.<br>
     * Presença: Obrigatória.<br>
     * Tipo: Número.<br>
     * Formato: Inteiro.
     */
    @XmlElement(name = "itemCount")
    private Integer totalItens;

    /**
     * Itens contidos na transação
     */
    @XmlElementWrapper(name = "items")
    @XmlElement(name = "item")
    private Collection<Item> itens;

    @XmlElement(name = "sender")
    private Comprador comprador;

    @XmlTransient
    public LocalDateTime getData() {
        return LocalDateTime.parse(this.data, DateTimeFormatter.ISO_DATE_TIME);
    }

    @XmlTransient
    public BigDecimal getValorTaxas() {
        return this.valor.subtract(this.valorLiquido);
    }

    @XmlTransient
    public Situacao getSituacaoAssinatura() {
        return switch (this.estado) {
            case AGUARDA_PAGAMENTO, EM_ANALISE, EM_DISPUTA, RETENCAO_TEMPORARIA, EM_DEVOLUCAO ->
                Situacao.EM_ANALISE;
            case PAGA, DISPONIVEL ->
                Situacao.QUITADO;
            case DEVOLVIDA, DEBITADO, CANCELADA ->
                Situacao.CANCELADO;
        };
    }
}
