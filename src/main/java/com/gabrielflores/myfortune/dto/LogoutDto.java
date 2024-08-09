package com.gabrielflores.myfortune.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Parâmetros para desautenticação de usuário")
public class LogoutDto {

    @Schema(description = "Token do dispositivo para excluir registro do firebase", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String notificationToken;

    @Schema(description = "Token de autenticação para invalidar", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String refreshToken;

}
