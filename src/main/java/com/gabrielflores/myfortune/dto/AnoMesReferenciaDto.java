package com.gabrielflores.myfortune.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 *
 * @author Douglas Pasqualin <douglas.pasqualin@gmail.com>
 */
@Setter
@Getter
@Accessors(chain = true)
@Schema(description = "Parâmetros de mês e ano")
public class AnoMesReferenciaDto {

    @NotNull
    @Min(2023)
    @Schema(description = "Ano de referência", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer ano;

    @NotNull
    @Min(1)
    @Max(12)
    @Schema(description = "Mês referência", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer mes;
}
