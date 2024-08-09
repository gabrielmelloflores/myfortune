package com.gabrielflores.myfortune.model.assinatura;

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
import java.time.LocalDateTime;
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
@Table(name = "usuarios_assinaturas")
public class UsuarioAssinatura extends EntidadeBase {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "usuarios_assinaturas_seq")
    @SequenceGenerator(name = "usuarios_assinaturas_seq", sequenceName = "seq_usuarios_assinaturas", allocationSize = 1)
    private Long id;

    @NotNull
    @EqualsAndHashCode.Include
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_assinatura")
    private Assinatura assinatura;

    @NotNull
    @EqualsAndHashCode.Include
    @Column(name = "dh_inclusao")
    private LocalDateTime dataInclusao;

    @EqualsAndHashCode.Include
    @Column(name = "dh_remocao")
    private LocalDateTime dataRemocao;

}
