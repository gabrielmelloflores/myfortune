package com.gabrielflores.myfortune.model.assinatura;

import com.gabrielflores.myfortune.dto.assinatura.CupomDtoCadastro;
import com.gabrielflores.myfortune.model.entity.EntidadeBase;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
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
@Table(name = "cupons_descontos")
public class CupomDesconto extends EntidadeBase {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cupons_descontos_seq")
    @SequenceGenerator(name = "cupons_descontos_seq", sequenceName = "seq_cupons_descontos", allocationSize = 1)
    private Long id;

    @NotEmpty
    @EqualsAndHashCode.Include
    @ToString.Include
    @Column(name = "codigo")
    private String codigo;

    @Column(name = "descricao")
    private String descricao;

    @NotNull
    @Column(name = "tipo_desconto")
    @EqualsAndHashCode.Include
    @ToString.Include
    @Convert(converter = TipoDesconto.Converter.class)
    private TipoDesconto tipoDesconto;

    @NotNull
    @Column(name = "valor")
    @EqualsAndHashCode.Include
    @ToString.Include
    private BigDecimal valor;

    @Column(name = "vl_minimo")
    private BigDecimal valorMinimo;

    //@DateTimeFormat(pattern = "dd/MM/yyyy")
    @Column(name = "dt_validade")
    private LocalDate validade;

    @Column(name = "qt_disponivel")
    private Integer disponivel;

    @Column(name = "qt_utilizada")
    private Integer utilizado;

    public CupomDesconto(final CupomDtoCadastro cupomDtoCadastro) {
        super();
        setDadosFromDto(cupomDtoCadastro);
    }

    public void atualizaDados(final CupomDtoCadastro cupomDtoCadastro) {
        setDadosFromDto(cupomDtoCadastro);
    }

    private void setDadosFromDto(final CupomDtoCadastro cupomDtoCadastro) {
        this.codigo = cupomDtoCadastro.getCodigo().toUpperCase();
        this.descricao = Optional.ofNullable(cupomDtoCadastro.getDescricao()).orElse(this.codigo);
        this.tipoDesconto = cupomDtoCadastro.getTipoDesconto();
        this.valor = cupomDtoCadastro.getValor();
        this.validade = cupomDtoCadastro.getValidade();
        this.disponivel = cupomDtoCadastro.getDisponivel();
        this.valorMinimo = cupomDtoCadastro.getValorMinimo();
    }

    public BigDecimal getValorDesconto(final BigDecimal valorBase) {
        BigDecimal valorDesconto;
        if (TipoDesconto.PERCENTUAL.equals(this.tipoDesconto)) {
            valorDesconto = valorBase.multiply(this.valor.divide(new BigDecimal(100)));
        } else {
            valorDesconto = this.valor;
        }
        return valorDesconto;
    }

    public Boolean possuiValorMinimo() {
        return this.valorMinimo != null && this.valorMinimo.compareTo(BigDecimal.ZERO) > 0;
    }

    public void utiliza() {
        Integer totalUtilizado = Optional.ofNullable(this.utilizado).orElse(0);
        this.utilizado = ++totalUtilizado;
    }

    public boolean isValido() {
        boolean valido;
        if (this.validade != null) {
            valido = this.validade.isEqual(LocalDate.now()) || this.validade.isAfter(LocalDate.now());
        } else {
            valido = true;
        }
        //Se já não é válido pela data, já retorna
        if (!valido) {
            return false;
        }
        //Testar pelas quantidades
        if (this.disponivel != null) {
            return Optional.ofNullable(this.utilizado).orElse(0) < this.disponivel;
        }
        return true;
    }

    /**
     * Somente para manter compatibilidade com a integraçao anterior usando o
     * sistema de assinaturas do PagSeguro. Atualmente está sendo usado o
     * sistema de checkouts
     *
     * @return ID do cupom no provedor externo, ex: PagSeguro
     */
    public String getIdExterno() {
        return null;
    }
}
