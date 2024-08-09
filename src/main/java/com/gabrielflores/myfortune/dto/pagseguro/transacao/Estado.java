package com.gabrielflores.myfortune.dto.pagseguro.transacao;

import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 *
 * @author Douglas Pasqualin <douglas.pasqualin@gmail.com>
 */
@AllArgsConstructor
@Getter
@XmlEnum
public enum Estado {

    /**
     * O comprador iniciou a transação, mas até o momento o PagSeguro não
     * recebeu nenhuma informação sobre o pagamento. Quando a resposta da
     * instituição financeira é muito rápida, omitimos esta notificação. <br>
     *
     * Transições:<br>
     * Para Paga: Quando a operação é confirmada pela instituição
     * financeira.<br>
     * Para Em análise: Quando a operação entra em uma fila para que sejam
     * feitas análises adicionais pela equipe do PagSeguro.<br>
     * Para Cancelada: Quando a operação é negada pela instituição financeira ou
     * quando o PagSeguro não recebe uma confirmação após um intervalo de tempo.
     */
    @XmlEnumValue("1")
    AGUARDA_PAGAMENTO(1),
    /**
     * O comprador optou por pagar com um cartão de crédito e o PagSeguro está
     * analisando o risco da transação.<br>
     *
     * Transições:<br>
     * Para Paga: Quando tanto o PagSeguro quanto a operadora de cartões de
     * crédito aprovam a transação.<br>
     * Para Cancelada: Quando o PagSeguro ou a operadora de cartões de crédito
     * negam a transação.
     */
    @XmlEnumValue("2")
    EM_ANALISE(2),
    /**
     * A transação foi paga pelo comprador e o PagSeguro já recebeu uma
     * confirmação da instituição financeira responsável pelo processamento.
     * Quando uma transação tem seu status alterado para Paga, isso significa
     * que você já pode liberar o produto vendido ou prestar o serviço
     * contratado. Porém, note que o valor da transação pode ainda não estar
     * disponível para retirada de sua conta, pois o PagSeguro pode esperar o
     * fim do prazo de liberação da transação.<br>
     *
     * Transições: <br>
     * Para Em disputa: Quando o comprador, dentro do prazo de liberação da
     * transação, indicar que não recebeu o produto ou serviço adquirido, ou que
     * o mesmo foi entregue com problemas. Este processo é chamado de disputa e
     * é mediado pela equipe do PagSeguro. Para saber mais, veja a página de
     * explicação sobre disputas.<br>
     * Para Devolvida: Quando você entrar em acordo com o comprador para
     * devolver o valor da transação, pois não possui mais o produto em estoque
     * ou não pode mais prestar o serviço contratado.<br>
     * Para Disponível: Quando a transação chega ao final de seu prazo de
     * liberação sem ter sido retornada e não há nenhuma disputa aberta.
     */
    @XmlEnumValue("3")
    PAGA(3),
    /**
     * A transação foi paga e chegou ao final de seu prazo de liberação sem ter
     * sido retornada e sem que haja nenhuma disputa aberta. Este status indica
     * que o valor da transação está disponível para saque.<br>
     *
     * Transições:<br>
     * Para Devolvida: Quando você entrar em acordo com o comprador para
     * devolver o valor da transação, pois não possui mais o produto em estoque
     * ou não pode mais prestar o serviço contratado.<br>
     * Para Em disputa: Quando o comprador indicar que não recebeu o produto ou
     * serviço adquirido, ou que o mesmo foi entregue com problemas. Este
     * processo é chamado de disputa e é mediado pela equipe do PagSeguro. Para
     * saber mais, veja a página de explicação sobre disputas. Uma transação
     * pode entrar em disputa, mesmo após a finalização do prazo de liberação do
     * pagamento.
     */
    @XmlEnumValue("4")
    DISPONIVEL(4),
    /**
     * O comprador, dentro do prazo de liberação da transação, abriu uma
     * disputa. A disputa é um processo iniciado pelo comprador para indicar que
     * não recebeu o produto ou serviço adquirido, ou que o mesmo foi entregue
     * com problemas. Este é um mecanismo de segurança oferecido pelo PagSeguro.
     * A equipe do PagSeguro é responsável por mediar a resolução de todas as
     * disputas, quando solicitado pelo comprador. Para mais informações, veja a
     * página de explicação sobre disputas.<br>
     *
     * Transições:<br>
     * Para Disponível: Quando a disputa é resolvida em favor do vendedor,
     * indicando que o produto ou serviço foi efetivamente entregue
     * corretamente.<br>
     * Para Devolvida: Quando a disputa é resolvida em favor do comprador,
     * indicando que o produto não foi entregue ou foi entregue fora das
     * especificações e deve ser devolvido.<br>
     * Para Paga: Quando a disputa é resolvida em favor do vendedor, porém antes
     * da finalização do prazo de liberação do pagamento.
     */
    @XmlEnumValue("5")
    EM_DISPUTA(5),
    /**
     * O *valor da transação foi devolvido para o comprador. Se você não possui
     * mais o produto vendido em estoque, ou não pode por alguma razão prestar o
     * serviço contratado, você pode devolver o valor da transação para o
     * comprador. Esta também é a ação tomada quando uma disputa é resolvida em
     * favor do comprador. Transações neste status não afetam o seu saldo no
     * PagSeguro, pois não são nem um crédito e nem um débito.<br>
     *
     * Transições:<br>
     * Nenhuma.
     */
    @XmlEnumValue("6")
    DEVOLVIDA(6),
    /**
     * A transação foi cancelada sem ter sido finalizada. Quando o comprador
     * opta por pagar com débito online ou boleto bancário e não finaliza o
     * pagamento, a transação assume este status. Isso também ocorre quando o
     * comprador escolhe pagar com um cartão de crédito e o pagamento não é
     * aprovado pelo PagSeguro ou pela operadora.<br>
     *
     */
    @XmlEnumValue("7")
    CANCELADA(7),
    /**
     * A valor da transação foi devolvido para o comprador.<br>
     */
    @XmlEnumValue("8")
    DEBITADO(8),
    /**
     * O comprador abriu uma solicitação de chargeback junto à operadora do
     * cartão de crédito.
     */
    @XmlEnumValue("9")
    RETENCAO_TEMPORARIA(9),
    /**
     * Não tem na documentação, simulei via interface e descobri que 10 era "Em
     * devolução"
     */
    @XmlEnumValue("10")
    EM_DEVOLUCAO(10);

    private final Integer estadoItem;
}
