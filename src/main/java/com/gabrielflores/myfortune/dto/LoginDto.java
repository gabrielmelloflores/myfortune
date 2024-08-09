package com.gabrielflores.myfortune.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
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
@Schema(description = "Parâmetros para autenticação de usuário")
public class LoginDto {

    @NotBlank(message = "O e-mail é obrigatório")
    @Email(message = "E-mail inválido")
    @Schema(description = "E-mail do usuário", requiredMode = Schema.RequiredMode.REQUIRED)
    private String email;

    @NotBlank(message = "A senha é obrigatória")
    @Schema(description = "Senha do usuário", requiredMode = Schema.RequiredMode.REQUIRED)
    private String senha;

    @Schema(description = "Token do dispositivo para registro no firebase", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String tokenNotificacao;

}
