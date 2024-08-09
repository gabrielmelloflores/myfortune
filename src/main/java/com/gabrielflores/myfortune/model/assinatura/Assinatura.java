package com.gabrielflores.myfortune.model.assinatura;

import com.gabrielflores.myfortune.model.entity.EntidadeBase;
import com.gabrielflores.myfortune.model.user.Usuario;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
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
@Table(name = "assinaturas")
public class Assinatura extends EntidadeBase {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "assinaturas_seq")
    @SequenceGenerator(name = "assinaturas_seq", sequenceName = "seq_assinaturas", allocationSize = 1)
    private Long id;

    @NotNull
    @EqualsAndHashCode.Include
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario")
    private Usuario User;

    @NotNull
    @EqualsAndHashCode.Include
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_plano")
    private Plano plano;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cupom")
    private CupomDesconto cupomDesconto;

    @NotNull
    @EqualsAndHashCode.Include
    @Column(name = "valor")
    private BigDecimal valor;

    @Column(name = "vl_desconto")
    private BigDecimal valorDesconto;

    @NotNull
    @EqualsAndHashCode.Include
    @Column(name = "valor_final")
    private BigDecimal valorFinal;

    @NotNull
    @EqualsAndHashCode.Include
    @Column(name = "dh_assinatura")
    private LocalDateTime dataAssinatura;

    @NotNull
    @EqualsAndHashCode.Include
    @Column(name = "situacao")
    @Convert(converter = Situacao.Converter.class)
    private Situacao situacao;

    @NotNull
    @Column(name = "dt_proxima_cobranca")
    private LocalDate dataProximaCobranca;

    @Column(name = "dh_cancelamento")
    private LocalDateTime dataCancelamento;

    @Column(name = "motivo")
    private String motivoCancelamento;

    @Column(name = "url_checkout")
    private String urlCheckout;

    @Column(name = "id_checkout")
    private String idCheckout;

    @Column(name = "id_transacao")
    private String idTransacao;

    @Getter(AccessLevel.NONE)
    @Column(name = "checkout_valido")
    private Boolean checkoutValido;

    @Column(name = "forma_pagamento")
    @Convert(converter = FormaPagamento.Converter.class)
    private FormaPagamento formaPagamento;

    @Column(name = "num_parcelas")
    private Integer numeroParcelas;

    @Column(name = "dh_pagamento")
    private LocalDateTime dataPagamento;

    @Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_notif_pagamento")
    private NotificacaoPagamento notificacaoPagamento;

    @OneToMany(mappedBy = "assinatura", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    private Set<UsuarioAssinatura> coleiras;

    public Assinatura(final Usuario User, final Plano plano, final CupomDesconto cupomDesconto) {
        this.User = User;
        this.plano = plano;
        this.valor = plano.getValor();
        this.cupomDesconto = cupomDesconto;
        this.valorDesconto = this.cupomDesconto != null ? cupomDesconto.getValorDesconto(this.valor) : BigDecimal.ZERO;
        this.valorFinal = this.valor.subtract(this.valorDesconto);
        this.dataAssinatura = LocalDateTime.now();
        this.situacao = Situacao.PENDENTE;
        atualizaDataProximaCobranca();
    }

    public final void atualizaDataProximaCobranca() {
        this.dataProximaCobranca = switch (this.plano.getVigencia()) {
            case MENSAL ->
                LocalDate.now().plusMonths(1);
            case TRIMESTRAL ->
                LocalDate.now().plusMonths(3);
            case SEMESTRAL ->
                LocalDate.now().plusMonths(6);
            case ANUAL ->
                LocalDate.now().plusYears(1);
            case DIAS ->
                LocalDate.now().plusDays(plano.getDias());
        };
    }

    public void atualizaDadosParaNovoCheckout() {
        atualizaDataProximaCobranca();
        this.dataAssinatura = LocalDateTime.now();
        this.situacao = Situacao.PENDENTE;
    }

    public void addColeira(final UsuarioAssinatura UsuarioAssinatura) {
        if (this.coleiras == null) {
            this.coleiras = new HashSet<>();
        }
        this.coleiras.add(UsuarioAssinatura);
    }

    public boolean isCancelada() {
        return this.dataCancelamento != null;
    }

    public boolean isVigente() {
        final LocalDate hoje = LocalDate.now();
        return (this.dataProximaCobranca.isAfter(hoje) || this.dataProximaCobranca.isEqual(hoje))
                && this.situacao.isPago() && !isCancelada();
    }

    /**
     * Retorna quantos dias essa assinatura ainda possui antes de expirar
     *
     * @return dias restante de assinatura ou null se a assinatura já expirou
     */
    public Long getDiasValidade() {
        if (isVigente()) {
            return ChronoUnit.DAYS.between(LocalDate.now(), this.dataProximaCobranca);
        }
        return null;
    }

    public boolean isCheckoutValido() {
        if (this.urlCheckout == null) {
            return false;
        }
        return Optional.ofNullable(this.checkoutValido).orElse(false);
    }
}
