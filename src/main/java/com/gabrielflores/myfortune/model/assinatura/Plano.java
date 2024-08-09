package com.gabrielflores.myfortune.model.assinatura;

import com.gabrielflores.myfortune.dto.assinatura.PlanoDtoCadastro;
import com.gabrielflores.myfortune.model.entity.EntidadeBase;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Set;
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
@Table(name = "planos_assinaturas")
public class Plano extends EntidadeBase {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "planos_assinaturas_seq")
    @SequenceGenerator(name = "planos_assinaturas_seq", sequenceName = "seq_planos_assinaturas", allocationSize = 1)
    private Long id;

    @NotEmpty
    @ToString.Include
    @EqualsAndHashCode.Include
    @Column(name = "descricao")
    private String descricao;

    @NotNull
    @ToString.Include
    @EqualsAndHashCode.Include
    @Column(name = "valor")
    private BigDecimal valor;

    @NotNull
    @Column(name = "vigencia")
    @ToString.Include
    @EqualsAndHashCode.Include
    @Convert(converter = VigenciaPlano.Converter.class)
    private VigenciaPlano vigencia;

    @Column(name = "num_dias")
    @ToString.Include
    @EqualsAndHashCode.Include
    private Integer dias;

    @Column(name = "limite_parcelas")
    @ToString.Include
    private Integer limiteParcelas;

    @NotNull
    @ToString.Include
    @EqualsAndHashCode.Include
    @Column(name = "ativo")
    private Boolean ativo;

    @NotNull
    @ToString.Include
    @EqualsAndHashCode.Include
    @Column(name = "trial")
    private Boolean trial;

    @OneToMany(fetch = FetchType.LAZY,
            cascade = CascadeType.PERSIST,
            mappedBy = "plano")
    private Set<Cobertura> coberturas;

    public Plano(final PlanoDtoCadastro planoDtoCadastro) {
        super();
        setDadosFromDto(planoDtoCadastro);
        this.ativo = Boolean.TRUE;
    }

    public void atualizaDados(final PlanoDtoCadastro planoDtoCadastro) {
        setDadosFromDto(planoDtoCadastro);
    }

    private void setDadosFromDto(final PlanoDtoCadastro planoDtoCadastro) {
        this.descricao = planoDtoCadastro.getDescricao();
        this.valor = planoDtoCadastro.getValor();
        this.vigencia = planoDtoCadastro.getVigencia();
        this.dias = planoDtoCadastro.getDias();
        this.limiteParcelas = planoDtoCadastro.getLimiteParcelas();
        this.trial = planoDtoCadastro.getTrial();
    }

    public void inativa() {
        this.ativo = Boolean.FALSE;
    }

    public void reativa() {
        this.ativo = Boolean.TRUE;
    }

    /**
     * Somente para manter compatibilidade com a integraçao anterior usando o
     * sistema de assinaturas do PagSeguro. Atualmente está sendo usado o
     * sistema de checkouts
     *
     * @return ID do plano no provedor externo, ex: PagSeguro
     */
    public String getIdExterno() {
        return null;
    }
}
