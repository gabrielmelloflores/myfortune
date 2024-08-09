package com.gabrielflores.myfortune.model.assinatura;

/**
 *
 * @author Douglas Pasqualin <douglas.pasqualin@gmail.com>
 */
import com.gabrielflores.myfortune.model.entity.EntidadeBase;
import com.gabrielflores.myfortune.util.AnoMesReferencia;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
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
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.hibernate.envers.Audited;

@Data
@NoArgsConstructor
@Accessors(chain = true)
@ToString(callSuper = false, onlyExplicitlyIncluded = true)
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
@Entity
@Audited
@Table(name = "coberturas_utilizadas")
public class CoberturaUtilizada extends EntidadeBase {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "coberturas_utilizadas_seq")
    @SequenceGenerator(name = "coberturas_utilizadas_seq", sequenceName = "seq_coberturas_utilizadas", allocationSize = 1)
    private Long id;

    @NotNull
    @ToString.Include
    @EqualsAndHashCode.Include
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cobertura")
    private Cobertura cobertura;

    @NotNull
    @EqualsAndHashCode.Include
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_assinatura")
    private Assinatura assinatura;

    @NotNull
    @ToString.Include
    @EqualsAndHashCode.Include
    @Column(name = "dh_utilizacao")
    private LocalDateTime dataUtilizacao;

    @Embedded
    @ToString.Include
    @AttributeOverrides({
        @AttributeOverride(name = "ano", column = @Column(name = "ano_ref", updatable = false, insertable = false)),
        @AttributeOverride(name = "mes", column = @Column(name = "mes_ref", updatable = false, insertable = false))
    })
    private AnoMesReferencia anoMesReferencia;

    public CoberturaUtilizada(final Cobertura cobertura, final Assinatura assinatura) {
        super();
        this.cobertura = cobertura;
        this.assinatura = assinatura;
        this.dataUtilizacao = LocalDateTime.now();
    }

}
