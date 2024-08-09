package com.gabrielflores.myfortune.dto.user;

import com.gabrielflores.myfortune.model.user.Genero;
import com.gabrielflores.myfortune.validation.Exists;
import com.gabrielflores.myfortune.validation.FieldMatch;
import com.gabrielflores.myfortune.validation.IsUnique;
import com.gabrielflores.myfortune.validation.Password;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
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
@Schema(title = "Cadastro Usuário", description = "Dados para cadastro de usuário.")
@Exists(property = "perfil", repository = "perfilRepository", message = "Perfil não encontrado")
@IsUnique(property = "email", repository = "usuarioRepository", message = "E-mail já cadastrado", action = IsUnique.Action.INSERT)
@IsUnique(property = "cpf", repository = "usuarioRepository", message = "CPF já cadastrado", action = IsUnique.Action.INSERT)
@Password(passwordField = "senha", action = Password.Action.INSERT)
@FieldMatch(first = "senha", second = "confirmaSenha", message = "Os campos de senha precisam ser iguais")
public class UsuarioDtoCadastro {

    @Email
    @NotBlank
    @Size(max = 255)
    @Schema(title = "E-mail do usuário", description = "Chave usada para identificar o usuário.", requiredMode = Schema.RequiredMode.REQUIRED)
    private String email;

    @CPF
    @NotBlank
    @Size(max = 255)
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

    @Size(max = 128)
    @Schema(title = "Senha do usuário", description = "Senha inicial do usuário. Null para gerar senha.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String senha;

    @Size(max = 128)
    @Schema(title = "Confirma senha", description = "Confirma senha do usuário. Null para gerar senha.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String confirmaSenha;

    @Schema(title = "Perfil do usuário", description = "Perfil inicial do usuário.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Long perfil;

    @Schema(title = "Assinatura", description = "ID da assinatura escolhida.", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long idAssinatura;

    @Schema(title = "Usuário ativo", description = "Indica se o usuário está ativo (default = true).", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Boolean ativo;

    public boolean isAtivo() {
        return Optional.ofNullable(ativo).orElse(Boolean.TRUE);
    }

}
