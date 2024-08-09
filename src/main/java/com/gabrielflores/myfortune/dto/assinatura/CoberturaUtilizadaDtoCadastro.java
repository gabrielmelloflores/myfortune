package com.gabrielflores.myfortune.dto.assinatura;

import com.gabrielflores.myfortune.model.assinatura.TipoCobertura;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(title = "Utilização de coberturas dos planos", description = "Dados para registrar a utilização de uma cobertura de um plano de assinatura")
public class CoberturaUtilizadaDtoCadastro {

    @NotNull
    @Schema(title = "Coleira", description = "A coleira que possui uma assinatura válida que vai utilizar o plano de cobertura.", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long coleira;

    @NotNull
    @Schema(title = "Tipo de cobertura", description = "A cobetura que está sendo utilizada", requiredMode = Schema.RequiredMode.REQUIRED)
    private TipoCobertura tipoCobertura;
}
