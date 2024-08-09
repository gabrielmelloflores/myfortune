package com.gabrielflores.myfortune.dto;

import com.gabrielflores.myfortune.validation.FieldMatch;
import com.gabrielflores.myfortune.validation.Password;
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
@Schema(description = "Parâmetros para recuperação da senha do usuário")
@Password(passwordField = "novaSenha", action = Password.Action.RESET)
@FieldMatch(first = "novaSenha", second = "confirmaSenha", message = "Os campos de senha precisam ser iguais")
public class PasswordTokenDto {

    @NotBlank
    @Schema(description = "Token para resetar a senha.", requiredMode = Schema.RequiredMode.REQUIRED)
    private String token;

    @NotBlank
    @Schema(description = "Nova senha", requiredMode = Schema.RequiredMode.REQUIRED)
    private String novaSenha;

    @NotBlank
    @Schema(description = "Confirmação da nova senha", requiredMode = Schema.RequiredMode.REQUIRED)
    private String confirmaSenha;
}
