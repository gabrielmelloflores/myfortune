package com.gabrielflores.myfortune.dto.user;

import com.gabrielflores.myfortune.model.user.Genero;
import com.gabrielflores.myfortune.validation.IsUnique;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Optional;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.br.CPF;

/**
 *
 * @Author: Gabriel Flores - gabrielmelloflores@gmail.com  
 */
@Setter
@Getter
@Accessors(chain = true)
@Schema(title = "Atualiza Cadastro Usuário", description = "Dados para atualizar cadastro de usuário.")
@IsUnique(property = "email", repository = "usuarioRepository", message = "E-mail já cadastrado", action = IsUnique.Action.UPDATE)
@IsUnique(property = "cpf", repository = "usuarioRepository", message = "CPF já cadastrado", action = IsUnique.Action.UPDATE)
public class UsuarioDtoAlteracao {

    @NotNull
    @Schema(title = "ID do usuário", description = "Identificador do usuário.", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long id;

    @Email
    @NotBlank
    @Size(max = 255)
    @Schema(title = "E-mail do usuário", description = "Chave usada para identificar o usuário.", requiredMode = Schema.RequiredMode.REQUIRED)
    private String email;

    @CPF
    @NotBlank
    @Schema(title = "CPF do usuário", description = "Documento de identificação do usuário.", requiredMode = Schema.RequiredMode.REQUIRED)
    private String cpf;

    @Schema(title = "Gênero/Sexo", description = "Gênero do usuário.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Genero genero;

    @Schema(title = "Data nascimento", description = "Data de nascimento do usuário", requiredMode = Schema.RequiredMode.NOT_REQUIRED, format = "date")
    private LocalDate nascimento;

    @NotBlank
    @Size(max = 20)
    @Pattern(regexp = "\\(\\d{2}\\) \\d{9}", message = "deve estar no formato (##) #########")
    @Schema(title = "Telefone", description = "Telefone do usuário.", requiredMode = Schema.RequiredMode.REQUIRED)
    private String telefone;

    @NotBlank
    @Size(max = 255)
    @Schema(title = "Nome do usuário", description = "Nome completo do usuário.", requiredMode = Schema.RequiredMode.REQUIRED)
    private String nome;

    @Schema(title = "Usuário ativo", description = "Indica se o usuário está ativo (default = true).", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Boolean ativo;

    public boolean isAtivo() {
        return Optional.ofNullable(ativo).orElse(Boolean.TRUE);
    }

}
