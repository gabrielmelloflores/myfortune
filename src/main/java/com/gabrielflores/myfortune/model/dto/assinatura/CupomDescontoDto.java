package com.gabrielflores.myfortune.model.dto.assinatura;

import java.time.LocalDate;

/**
 *
 * @author Douglas Pasqualin <douglas.pasqualin@gmail.com>
 */
public interface CupomDescontoDto extends CupomDescontoDtoBasic {

    LocalDate getValidade();

    Integer getDisponivel();

    Integer getUtilizado();

}
