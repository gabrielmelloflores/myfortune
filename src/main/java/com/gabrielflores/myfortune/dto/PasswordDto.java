package com.gabrielflores.myfortune.dto;

import com.gabrielflores.myfortune.validation.FieldMatch;
import com.gabrielflores.myfortune.validation.Password;
import com.gabrielflores.myfortune.validation.Password.Action;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
@Schema(description = "Parâmetros para alteração da senha do usuário")
@Password(passwordField = "novaSenha", action = Action.UPDATE)
@FieldMatch(first = "novaSenha", second = "confirmaSenha", message = "Os campos de senha precisam ser iguais")
public class PasswordDto {

    @NotNull
    @Schema(description = "ID do usuário", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long id;

    @NotBlank
    @Schema(description = "Senha atual", requiredMode = Schema.RequiredMode.REQUIRED)
    private String senhaAtual;

    @NotBlank
    @Schema(description = "Nova senha", requiredMode = Schema.RequiredMode.REQUIRED)
    private String novaSenha;

    @NotBlank
    @Schema(description = "Confirmação da nova senha", requiredMode = Schema.RequiredMode.REQUIRED)
    private String confirmaSenha;

}
