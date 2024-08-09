package com.gabrielflores.myfortune.util;

import com.gabrielflores.myfortune.dto.AnoMesReferenciaDto;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author Douglas Pasqualin <douglas.pasqualin@gmail.com>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class AnoMesReferencia {

    @Min(2023)
    @Column(name = "ano_ref")
    private Integer ano;

    @Min(1)
    @Max(12)
    @Column(name = "mes_ref")
    private Integer mes;

    public AnoMesReferencia(final LocalDateTime localDateTime) {
        this(localDateTime.toLocalDate());
    }

    public AnoMesReferencia(final LocalDate localDate) {
        super();
        this.ano = localDate.getYear();
        this.mes = localDate.getMonthValue();
    }

    public AnoMesReferencia(final AnoMesReferenciaDto anoMesReferenciaDto) {
        this(anoMesReferenciaDto.getAno(), anoMesReferenciaDto.getMes());
    }
}
