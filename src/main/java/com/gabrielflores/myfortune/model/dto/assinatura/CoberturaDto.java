package com.gabrielflores.myfortune.model.dto.assinatura;

import com.gabrielflores.myfortune.model.assinatura.TipoCobertura;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Value;

/**
 *
 * @author Douglas Pasqualin <douglas.pasqualin@gmail.com>
 */
public interface CoberturaDto {

    Long getId();

    TipoCobertura getTipoCobertura();

    LocalDate getDataInicio();

    LocalDate getDataFim();

    Integer getQuantidade();

    BigDecimal getValorExcedente();

    @Value("#{target.ativa}")
    Boolean getAtiva();
}
