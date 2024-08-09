package com.gabrielflores.myfortune.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 *
 * @author Giuliano Ferreira <giuliano@ufsm.br>
 */
@Setter
@Getter
@Accessors(chain = true)
@Schema(description = "Parâmetros para renovar o token")
public class RefreshTokenDto {

    @NotBlank
    @Schema(description = "Chave para validar o usuário e gerar novo token", requiredMode = Schema.RequiredMode.REQUIRED)
    private String token;
}
