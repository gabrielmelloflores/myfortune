package com.gabrielflores.myfortune.model.dto.assinatura;

import java.util.List;

/**
 *
 * @author Douglas Pasqualin <douglas.pasqualin@gmail.com>
 */
public interface PlanoDto extends PlanoDtoList {

    Integer getLimiteParcelas();

    List<CoberturaDto> getCoberturas();
}
