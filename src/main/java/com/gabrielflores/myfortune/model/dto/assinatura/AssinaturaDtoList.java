package com.gabrielflores.myfortune.model.dto.assinatura;

import com.gabrielflores.myfortune.model.assinatura.Situacao;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Value;

/**
 *
 * @author Douglas Pasqualin <douglas.pasqualin@gmail.com>
 */
public interface AssinaturaDtoList {

    Long getId();

    PlanoDtoList getPlano();

    @Value("#{target.diasValidade}")
    Long getDiasValidade();

    LocalDateTime getDataCancelamento();

    Situacao getSituacao();

    @Value("#{target.vigente}")
    Boolean getVigente();

    @Value("#{target.cancelada}")
    Boolean getCancelada();

}
