package com.gabrielflores.myfortune.dto.assinatura;

import com.gabrielflores.myfortune.model.assinatura.TipoCobertura;
import com.gabrielflores.myfortune.validation.Exists;
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
@Schema(title = "Cadastro de Coberturas de planos assinaturas", description = "Dados para cadastrar uma cobertura de um plano.")
@Exists(property = "plano", repository = "planoRepository", message = "Plano não cadastrado no sistema!")
public class CoberturaDtoCadastro {

    @NotNull
    @Schema(title = "Plano", description = "Plano de assinatura da cobertura.", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long plano;

    @NotNull
    @Schema(title = "Tipo de cobertura", description = "A cobertura para esse plano.", requiredMode = Schema.RequiredMode.REQUIRED)
    private TipoCobertura tipoCobertura;

    @NotNull
    @Schema(title = "Data de início", description = "A patir de que data esta cobertura faz parte do plano.", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDate dataInicio;

    @Positive
    @Schema(title = "Quantidade", description = "Quantidade mensal de uso desta cobertura que não terá custo adicional. Ou null se não tiver limite", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer quantidade;

    @DecimalMin(value = "0.0", inclusive = false)
    @Digits(integer = 10, fraction = 2)
    @Schema(title = "Valor Excedente", description = "Valor unitário a ser cobrado quando a utilização da cobertura extrapolar a 'quantidade' cadastrada", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private BigDecimal valorExcedente;

}
