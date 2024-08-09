package com.gabrielflores.myfortune.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 *
 * @Author: Gabriel Flores - gabrielmelloflores@gmail.com  
 */
@Setter
@Getter
@Accessors(chain = true)
@Schema(description = "Parâmetros para atualizar os perfis de um usuário")
public class UsuarioDtoPerfil {

    @NotNull
    @Schema(title = "ID do usuário", description = "Identificador do usuário.", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long id;

    @NotEmpty
    @Schema(description = "Lista de perfis para adicionar ou remover de um usuário", requiredMode = Schema.RequiredMode.REQUIRED)
    private String[] perfis;
}
