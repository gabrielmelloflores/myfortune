package com.gabrielflores.myfortune.dto.pagseguro.pagamento;

import com.gabrielflores.myfortune.dto.pagseguro.PagSeguroObject;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 *
 * @author Douglas Pasqualin <douglas.pasqualin@gmail.com>
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class QrCodePagSeguro extends PagSeguroObject {

    /**
     * Objeto contendo as informações do valor a ser utilizado no QR Code.
     */
    @JsonProperty("amount")
    private Montante montante;

    /**
     * Data de expiração do QR Code. Por padrão, o QR Code gerado tem validade
     * de 24 horas caso o parâmetro não seja definido na requisição. <br/>
     * Exemplo: 2021-08-29T20:15:59-03:00
     */
    @JsonProperty("expiration_date")
    private String dataExpiracao;

    /* ========= properties que existem somente no response ========== */
    @JsonProperty("text")
    private String pixCopiaECola;

    public QrCodePagSeguro(final Integer valor) {
        this.montante = new Montante(valor);
    }

    public QrCodePagSeguro(final BigDecimal valor) {
        this(valor.multiply(new BigDecimal(100)).intValueExact());
        final ZonedDateTime expiracao = ZonedDateTime.now().plusHours(1);
        this.dataExpiracao = DateTimeFormatter.ISO_DATE_TIME.format(expiracao);
    }

    @Data
    @NoArgsConstructor
    public class Montante {

        /**
         * Valor do QR Code.
         */
        @JsonProperty("value")
        private Integer valor;

        public Montante(final Integer valor) {
            this.valor = valor;
        }

    }
}
