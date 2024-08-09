package com.gabrielflores.myfortune.model.assinatura;

import com.gabrielflores.myfortune.model.entity.EntidadeBase;
import com.gabrielflores.myfortune.util.AnoMesReferencia;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

/**
 *
 * @author Douglas Pasqualin <douglas.pasqualin@gmail.com>
 */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@ToString(callSuper = false, onlyExplicitlyIncluded = true)
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
@Entity
@Audited
@Table(name = "coberturas_pagamentos")
public class CoberturaPagamento extends EntidadeBase {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "coberturas_pagamentos_seq")
    @SequenceGenerator(name = "coberturas_pagamentos_seq", sequenceName = "seq_coberturas_pagamentos", allocationSize = 1)
    private Long id;

    @NotNull
    @EqualsAndHashCode.Include
    @ToString.Include
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cobertura")
    private Cobertura cobertura;

    @NotNull
    @EqualsAndHashCode.Include
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_assinatura")
    private Assinatura assinatura;

    @NotNull
    @EqualsAndHashCode.Include
    @Column(name = "quantidade")
    private Integer quantidade;

    @NotNull
    @ToString.Include
    @EqualsAndHashCode.Include
    @Column(name = "dh_geracao")
    private LocalDateTime dataGeracao;

    @NotNull
    @ToString.Include
    @EqualsAndHashCode.Include
    private AnoMesReferencia anoMesReferencia;

    @NotNull
    @ToString.Include
    @EqualsAndHashCode.Include
    @Column(name = "situacao")
    @Convert(converter = Situacao.Converter.class)
    private Situacao situacao;

    @Column(name = "url_checkout")
    private String urlCheckout;

    @EqualsAndHashCode.Include
    @Column(name = "id_checkout")
    private String idCheckout;

    @Column(name = "id_transacao")
    private String idTransacao;

    @Getter(AccessLevel.NONE)
    @Column(name = "checkout_valido")
    private Boolean checkoutValido;

    @EqualsAndHashCode.Include
    @Column(name = "forma_pagamento")
    @Convert(converter = FormaPagamento.Converter.class)
    private FormaPagamento formaPagamento;

    @EqualsAndHashCode.Include
    @Column(name = "dh_pagamento")
    private LocalDateTime dataPagamento;

    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_notif_pagamento")
    private NotificacaoPagamento notificacaoPagamento;

    public boolean isCheckoutValido() {
        if (this.urlCheckout == null) {
            return false;
        }
        return Optional.ofNullable(this.checkoutValido).orElse(false);
    }
}
