package com.gabrielflores.myfortune.model.assinatura;

import com.gabrielflores.myfortune.dto.assinatura.CoberturaDtoCadastro;
import com.gabrielflores.myfortune.model.entity.EntidadeBase;
import jakarta.persistence.Column;
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
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.hibernate.envers.Audited;

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
@Table(name = "planos_coberturas")
public class Cobertura extends EntidadeBase {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "planos_coberturas_seq")
    @SequenceGenerator(name = "planos_coberturas_seq", sequenceName = "seq_planos_coberturas", allocationSize = 1)
    private Long id;

    @NotNull
    @EqualsAndHashCode.Include
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_plano")
    private Plano plano;

    @NotNull
    @ToString.Include
    @EqualsAndHashCode.Include
    @Column(name = "cobertura")
    private TipoCobertura tipoCobertura;

    @NotNull
    @EqualsAndHashCode.Include
    @ToString.Include
    @Column(name = "dt_inicio")
    private LocalDate dataInicio;

    @EqualsAndHashCode.Include
    @ToString.Include
    @Column(name = "dt_fim")
    private LocalDate dataFim;

    @EqualsAndHashCode.Include
    @ToString.Include
    @Column(name = "quantidade")
    private Integer quantidade;

    @ToString.Include
    @EqualsAndHashCode.Include
    @Column(name = "valor_excedente")
    private BigDecimal valorExcedente;

    public Cobertura(final CoberturaDtoCadastro coberturaDtoCadastro, final Plano plano) {
        super();
        this.plano = plano;
        this.tipoCobertura = coberturaDtoCadastro.getTipoCobertura();
        this.dataInicio = coberturaDtoCadastro.getDataInicio();
        this.quantidade = coberturaDtoCadastro.getQuantidade();
        this.valorExcedente = this.quantidade != null ? coberturaDtoCadastro.getValorExcedente() : null;
    }

    public boolean isAtiva() {
        return this.dataFim == null || LocalDate.now().isBefore(dataFim);
    }

    public boolean permiteExcederQuantidade() {
        return this.valorExcedente != null;
    }

}
