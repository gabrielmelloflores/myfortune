package com.gabrielflores.myfortune.dto.assinatura;

import com.gabrielflores.myfortune.model.assinatura.TipoDesconto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
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
@Schema(title = "Manutenção de cupons de desconto", description = "Dados para criação/alteração de um cupom de desconto")
public class CupomDtoCadastro {

    @NotEmpty
    @Size(max = 20, message = "{error.max.size}")
    @Schema(title = "Código", description = "O código para utilização do cupom. Precisa ser único.", requiredMode = Schema.RequiredMode.REQUIRED)
    private String codigo;

    @Size(max = 60, message = "{error.max.size}")
    @Schema(title = "Descrição", description = "A descrição do cupom (Opcional).", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String descricao;

    @NotNull
    @Schema(title = "Tipo de desconto", description = "Como o desconto será aplicado no valor final", requiredMode = Schema.RequiredMode.REQUIRED)
    private TipoDesconto tipoDesconto;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    @Digits(integer = 10, fraction = 2)
    @Schema(title = "Valor", description = "O valor que o cupom abate", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal valor;

    @DecimalMin(value = "0.0", inclusive = false)
    @Digits(integer = 10, fraction = 2)
    @Schema(title = "Valor", description = "O valor minímo da compra para utilizar o cupom (Opcional)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private BigDecimal valorMinimo;

    @FutureOrPresent
    //@JsonFormat(pattern = "dd/MM/yyyy")
    @Schema(title = "Data de validade", description = "A data máxima que o cupom é válido (Opcional)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private LocalDate validade;

    @Positive
    @Schema(title = "Quantidade disponível", description = "Quantidade disponível de cupons (Opcional)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer disponivel;
}
