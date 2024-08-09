package com.gabrielflores.myfortune.dto.assinatura.pagamento;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
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
@Schema(title = "Dados de um Cartão de Crédito", description = "Dados de um Cartão de Crédito utilizado para pagamentos")
public class CartaoCreditoDto {

    /**
     * A maioria dos cartões de crédito apresenta 16 algarismos, separados em 4
     * blocos de 4 cada um. Mas existem variações. Os cartões AMEX, por exemplo,
     * têm apenas 15 números, enquanto os da Diners apresentam 14.
     */
    @NotNull
    @Pattern(regexp = "^\\d{14,16}$", message = "Número do cartão deve ter entre 14 e 16 dígitos")
    @Schema(title = "Número", description = "Número do cartão de crédito.", requiredMode = Schema.RequiredMode.REQUIRED)
    private String numero;

    @NotNull
    @Schema(title = "Mês de expiração", description = "Mês de expiração do cartão de crédito.", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer mesExpiracao;

    @NotNull
    @Schema(title = "Ano de expiração", description = "Ano de expiração do cartão de crédito.", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer anoExpiracao;

    @NotNull
    @Pattern(regexp = "^\\d{3}$", message = "Código de segurança deve ter 3 digitos")
    @Schema(title = "Código de segurança", description = "Código de segurança (CVV) contido no verso do cartão.", requiredMode = Schema.RequiredMode.REQUIRED)
    private String codigoSeguranca;

    @NotNull
    @Schema(title = "Portador", description = "Nome do portador do cartão de crédito.", requiredMode = Schema.RequiredMode.REQUIRED)
    private String nomePortador;

    @NotNull
    @Min(1)
    @Schema(title = "Parcelas", description = "Total de parcelas do pagamento.", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer totalParcelas;
}
