package com.gabrielflores.myfortune.model.dto.assinatura;

import com.gabrielflores.myfortune.model.assinatura.TipoDesconto;
import java.math.BigDecimal;
import org.springframework.beans.factory.annotation.Value;

/**
 *
 * @author Douglas Pasqualin <douglas.pasqualin@gmail.com>
 */
public interface CupomDescontoDtoBasic {

    Long getId();

    String getCodigo();

    String getDescricao();

    TipoDesconto getTipoDesconto();

    BigDecimal getValor();

    BigDecimal getValorMinimo();

    @Value("#{target.valido}")
    Boolean getValido();
}
