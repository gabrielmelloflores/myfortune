package com.gabrielflores.myfortune.dto.assinatura;

import com.gabrielflores.myfortune.model.assinatura.VigenciaPlano;
import com.gabrielflores.myfortune.validation.ConditionalRequired;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
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
@ConditionalRequired(field = "vigencia", value = "DIAS", required = {"dias"})
@Schema(title = "Cadastro de Planos de assinaturas", description = "Dados para cadastrar ou alterar um plano de assinatura.")
public class PlanoDtoCadastro {

    @NotEmpty
    @Size(max = 30, message = "{error.max.size}")
    @Schema(title = "Descrição", description = "Decrição do plano de assinatura", requiredMode = Schema.RequiredMode.REQUIRED)
    private String descricao;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = true)
    @Digits(integer = 10, fraction = 2)
    @Schema(title = "Valor", description = "O valor a ser cobrado pelo plano", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal valor;

    @NotNull
    @Schema(title = "Vigência", description = "Tipo de vigência do plano", requiredMode = Schema.RequiredMode.REQUIRED)
    private VigenciaPlano vigencia;

    @Schema(title = "Número de dias", description = "Se vigência for igual a dias, precisará especificar", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer dias;

    @Min(1)
    @Max(12)
    @Schema(title = "Limite de parcelas", description = "Número máximo de parcelas para pagamento do plano", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer limiteParcelas;

    @NotNull
    @Schema(title = "Plano trial", description = "Indica se o plano é trial. Se sim, uma assinatura com esse plano será criada no momento que uma coleira for vinculada com o User", requiredMode = Schema.RequiredMode.REQUIRED)
    private Boolean trial;

}
