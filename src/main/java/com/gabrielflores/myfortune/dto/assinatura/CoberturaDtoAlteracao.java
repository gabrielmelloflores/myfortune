package com.gabrielflores.myfortune.dto.assinatura;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDate;
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
@Schema(title = "Alteração de Coberturas de planos assinaturas", description = "Dados para alterar uma cobertura de um plano.")
public class CoberturaDtoAlteracao {

    @NotNull
    @Schema(title = "Data de início", description = "A patir de que data esta cobertura faz parte do plano.", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDate dataInicio;

    @Schema(title = "Data de fim", description = "A patir de que data esta cobertura não faz mais parte do plano.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private LocalDate dataFim;

    @Positive
    @Schema(title = "Quantidade", description = "Quantidade mensal de uso desta cobertura que não terá custo adicional. Ou null se não tiver limite", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer quantidade;

    @DecimalMin(value = "0.0", inclusive = false)
    @Digits(integer = 10, fraction = 2)
    @Schema(title = "Valor Excedente", description = "Valor unitário a ser cobrado quando a utilização da cobertura extrapolar a 'quantidade' cadastrada", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private BigDecimal valorExcedente;

    @Schema(hidden = true)
    public boolean isPeriodoValido() {
        return this.dataFim == null || this.dataInicio.isBefore(this.dataFim);
    }
}
