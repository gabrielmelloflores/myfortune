package com.gabrielflores.myfortune.model.dto.assinatura;

import com.gabrielflores.myfortune.model.assinatura.VigenciaPlano;
import java.math.BigDecimal;

/**
 *
 * @author Douglas Pasqualin <douglas.pasqualin@gmail.com>
 */
public interface PlanoDtoList {

    Long getId();

    String getDescricao();

    BigDecimal getValor();

    VigenciaPlano getVigencia();

    Integer getDias();

    Boolean getTrial();

    Boolean getAtivo();
}
