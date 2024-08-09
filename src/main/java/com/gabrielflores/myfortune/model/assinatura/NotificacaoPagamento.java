package com.gabrielflores.myfortune.model.assinatura;

import com.gabrielflores.myfortune.model.entity.Entidade;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 *
 * @author Douglas Pasqualin <douglas.pasqualin@gmail.com>
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@ToString(callSuper = false, onlyExplicitlyIncluded = false)
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = false)
@Entity
@Table(name = "notificacoes_pagamentos")
public class NotificacaoPagamento extends Entidade {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "notificacao_pagamento_seq")
    @SequenceGenerator(name = "notificacao_pagamento_seq", sequenceName = "seq_notificacoes_pagamentos", allocationSize = 1)
    private Long id;

    @Column(name = "id_checkout")
    private String idCheckout;

    @Column(name = "codigo")
    private String codigo;

    @Column(name = "dh_recebimento")
    private LocalDateTime dataRecebimento;

    @Column(name = "situacao")
    @Convert(converter = Situacao.Converter.class)
    private Situacao situacao;

    public NotificacaoPagamento(final String idCheckout, final String codigo, final Situacao situacao) {
        this.idCheckout = idCheckout;
        this.codigo = codigo;
        this.situacao = situacao;
        this.dataRecebimento = LocalDateTime.now();
    }

    /**
     * Retorna se essa notificação é transacional, ou seja enviada no formato
     * JSON <br>
     * https://dev.pagbank.uol.com.br/docs/recebendo-mudancas-de-status
     *
     * @return
     */
    public boolean isNotificacaoTransacional() {
        return this.idCheckout != null && this.situacao != null;
    }

    /**
     * Retorna se essa notificação é pós-transacional, ou seja enviado somente
     * um código que deve ser consultado diretamente no PagSeguro (retorno em
     * XML) <br>
     * https://dev.pagbank.uol.com.br/docs/recebendo-mudancas-de-status
     *
     * @return
     */
    public boolean isNotificacaoPosTransacional() {
        return this.codigo != null && this.situacao != null;
    }

    /**
     * Retorna se essa notificação é originada de uma notificação de checkout,
     * geralmente enviada pelo PagSeguro para notificar que o tempo para efetuar
     * o checkout expirou
     *
     * @return
     */
    public boolean isNotificacaoCheckout() {
        return this.idCheckout != null && this.situacao == null;
    }

}
