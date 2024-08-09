package com.gabrielflores.myfortune.model.dto.assinatura;

import java.math.BigDecimal;
import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Value;

/**
 *
 * @author Douglas Pasqualin <douglas.pasqualin@gmail.com>
 */
public interface AssinaturaDto extends AssinaturaDtoList {

    BigDecimal getValorFinal();

    LocalDate getDataProximaCobranca();

    String getUrlCheckout();

    @Value("#{target.checkoutValido}")
    Boolean getCheckoutValido();
}
