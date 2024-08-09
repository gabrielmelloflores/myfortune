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
@Schema(description = "Parâmetros para gerar link para recuperar a senha")
public class PasswordRecoverDto {

    @Email
    @NotBlank
    @Schema(description = "Endereço de e-mail para enviar o link", requiredMode = Schema.RequiredMode.REQUIRED)
    private String email;

}
