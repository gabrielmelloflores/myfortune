package com.gabrielflores.myfortune.model.dto.assinatura;

import java.time.LocalDateTime;

/**
 *
 * @author Douglas Pasqualin <douglas.pasqualin@gmail.com>
 */
public interface CoberturaUtilizadaDto {

    Long getId();

    CoberturaDto getCobertura();

    AssinaturaDtoList getAssinatura();

    LocalDateTime getDataUtilizacao();
}
