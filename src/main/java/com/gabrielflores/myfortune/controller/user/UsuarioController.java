package com.gabrielflores.myfortune.controller.user;

import com.gabrielflores.myfortune.annotation.AccessDeniedApiResponse;
import com.gabrielflores.myfortune.annotation.DefaultApiResponses;
import com.gabrielflores.myfortune.annotation.ForbiddenApiResponse;
import com.gabrielflores.myfortune.annotation.InvalidDataApiResponse;
import com.gabrielflores.myfortune.annotation.NotFoundApiResponse;
import com.gabrielflores.myfortune.controller.BaseController;
// import com.gabrielflores.myfortune.controller.BaseStorageController;
// import com.gabrielflores.myfortune.dto.AvatarDto;
import com.gabrielflores.myfortune.dto.PasswordDto;
import com.gabrielflores.myfortune.dto.user.UsuarioDtoAlteracao;
import com.gabrielflores.myfortune.dto.user.UsuarioDtoCadastro;
import com.gabrielflores.myfortune.dto.user.UsuarioDtoPerfil;
// import com.gabrielflores.myfortune.exception.FileNotFoundException;
import com.gabrielflores.myfortune.listeners.event.ConfirmacaoContaEvent;
import com.gabrielflores.myfortune.model.dto.assinatura.AssinaturaDto;
import com.gabrielflores.myfortune.model.dto.user.UsuarioDto;
import com.gabrielflores.myfortune.model.dto.user.UsuarioDtoList;
import com.gabrielflores.myfortune.model.user.Usuario;
import com.gabrielflores.myfortune.response.MessageResponse;
import com.gabrielflores.myfortune.response.PageResponse;
import com.gabrielflores.myfortune.security.UserPrincipal;
import com.gabrielflores.myfortune.service.assinatura.AssinaturaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.io.FileNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Giuliano Ferreira <giuliano@ufsm.br>
 */
@RestController
@DefaultApiResponses
@RequiredArgsConstructor
@RequestMapping(path = "/usuario", produces = MediaType.APPLICATION_JSON_VALUE)
@SecurityRequirement(name = "bearer-key")
@PreAuthorize("isAuthenticated()")
@Tag(name = "Usuário", description = "Gerenciamento de conta de usuário")
public class UsuarioController extends BaseController{

    private final ApplicationEventPublisher eventPublisher;

    private final AssinaturaService assinaturaService;

    // @Operation(summary = "Auto cadastro de usuário com token", responses = {
    //     @ApiResponse(responseCode = "200", description = "Dados básicos do convite", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioDtoCadastro.class)))
    // })
    // @GetMapping("/novo")
    // @PreAuthorize("permitAll")
    // public ResponseEntity<UsuarioDtoCadastro> autoCadastro(@Parameter(description = "Token para iniciar cadastro") @RequestParam("token") final String token) {
    //     final ConvitePetDto convite = convitePetService.findByToken(token);
    //     final UsuarioDtoCadastro dtoCadastro = new UsuarioDtoCadastro().setEmail(convite.getEmail());
    //     return ResponseEntity.ok(dtoCadastro);
    // }

    @Operation(summary = "Auto cadastro de usuário", responses = {
        @ApiResponse(responseCode = "200", description = "Usuário cadastrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioDto.class)))
    })
    @InvalidDataApiResponse
    @PostMapping("/novo")
    @PreAuthorize("permitAll")
    public ResponseEntity<UsuarioDto> autoCadastro(@Valid @RequestBody UsuarioDtoCadastro dtoCadastro) {
        final UsuarioDto usuario = usuarioService.save(dtoCadastro);
        if (!usuario.getConfirmado()) {
            eventPublisher.publishEvent(new ConfirmacaoContaEvent(usuario));
        }
        return ResponseEntity.ok(usuario);
    }

    @Operation(summary = "Usuário autenticado", responses = {
        @ApiResponse(responseCode = "200", description = "Informações do usuário autenticado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioDto.class)))
    })
    @GetMapping("/autenticado")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UsuarioDto> autenticado() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        final UserPrincipal user = (UserPrincipal) authentication.getPrincipal();
        final UsuarioDto usuario = usuarioService.getById(user.getId(), UsuarioDto.class);
        return ResponseEntity.ok(usuario);
    }

    @Operation(summary = "Atualiza cadastro usuário autenticado", responses = {
        @ApiResponse(responseCode = "200", description = "Cadastro atualizado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioDto.class)))
    })
    @InvalidDataApiResponse
    @AccessDeniedApiResponse
    @PutMapping("/autenticado")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UsuarioDto> autenticado(@Valid @RequestBody UsuarioDtoAlteracao dtoCadastro) {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        final UserPrincipal user = (UserPrincipal) authentication.getPrincipal();
        if (!user.getId().equals(dtoCadastro.getId())) {
            throw new AccessDeniedException("Acesso não autorizado");
        }
        final UsuarioDto usuario = usuarioService.update(dtoCadastro);
        if (!usuario.getConfirmado()) {
            eventPublisher.publishEvent(new ConfirmacaoContaEvent(usuario));
        }
        return ResponseEntity.ok(usuario);
    }

    @Operation(summary = "Altera senha do usuário autenticado", responses = {
        @ApiResponse(responseCode = "200", description = "Senha alterada", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class)))
    })
    @InvalidDataApiResponse
    @AccessDeniedApiResponse
    @PutMapping("/senha")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<MessageResponse> alteraSenha(@Valid @RequestBody PasswordDto passwordDto) {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        final UserPrincipal user = (UserPrincipal) authentication.getPrincipal();
        if (!user.getId().equals(passwordDto.getId())) {
            throw new AccessDeniedException("Acesso não autorizado");
        }
        usuarioService.alteraSenha(passwordDto);
        return ResponseEntity.ok(new MessageResponse("Senha alterada"));
    }

    // @Operation(summary = "Foto do usuário autenticado", responses = {
    //     @ApiResponse(responseCode = "200", description = "Foto do usuário", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AvatarDto.class)))
    // })
    // @NotFoundApiResponse
    // @GetMapping(value = "/foto")
    // public ResponseEntity<AvatarDto> foto() {
    //     final Usuario usuario = getUserLogado();
    //     final AvatarDto avatar = getAvatar(usuario);
    //     return ResponseEntity.ok(avatar);
    // }

    // @Operation(summary = "Altera ou remove foto do usuário autenticado", responses = {
    //     @ApiResponse(responseCode = "200", description = "Foto alterada/removida", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AvatarDto.class)))
    // })
    // @NotFoundApiResponse
    // @InvalidDataApiResponse
    // @PostMapping(value = "/foto")
    // public ResponseEntity<AvatarDto> alteraFoto(@Valid @RequestBody AvatarDto avatarDto) {
    //     Usuario usuario = getUserLogado();
    //     avatarDto.setUsuario(usuario);

    //     switch (avatarDto.getAction()) {
    //         case "U" -> {
    //             usuario = alteraAvatarUsuario(avatarDto);
    //             usuarioService.update(usuario);
    //             avatarDto = getAvatar(usuario);
    //         }
    //         case "D" -> {
    //             usuario = deletaAvatarUsuario(avatarDto);
    //             usuarioService.update(usuario);
    //             avatarDto = null;
    //         }
    //         default ->
    //             throw new IllegalArgumentException("Acão não suportada: " + avatarDto.getAction());
    //     }
    //     return ResponseEntity.ok().body(avatarDto);
    // }

    // @Operation(summary = "Altera ou remove foto do usuário autenticado", responses = {
    //     @ApiResponse(responseCode = "200", description = "Foto alterada/removida", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AvatarDto.class)))
    // })
    // @NotFoundApiResponse
    // @PostMapping(value = "/arquivo/", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    // public ResponseEntity<AvatarDto> alteraForo(
    //         @Parameter(description = "Operação (update|delete)")
    //         @Pattern(regexp = "[UD]", message = "Valores válidos: \"U\" ou \"D\"")
    //         @NotBlank @Size(max = 1) @RequestParam("action") String action,
    //         @Parameter(description = "Foto do usuário", content = @Content(mediaType = MediaType.APPLICATION_OCTET_STREAM_VALUE))
    //         @RequestParam(name = "file", required = false) MultipartFile file) {

    //     AvatarDto avatarDto;
    //     Usuario usuario = getUserLogado();

    //     switch (action) {
    //         case "U" -> {
    //             if (file == null || file.isEmpty()) {
    //                 throw new FileNotFoundException("Arquivo vazio");
    //             }
    //             usuario = alteraAvatarUsuario(usuario, file);
    //             usuarioService.update(usuario);
    //             avatarDto = getAvatar(usuario);
    //         }
    //         case "D" -> {
    //             usuario = deletaAvatarUsuario(usuario);
    //             usuarioService.update(usuario);
    //             avatarDto = null;
    //         }
    //         default ->
    //             throw new IllegalArgumentException("Acão não suportada: " + action);
    //     }
    //     return ResponseEntity.ok().body(avatarDto);
    // }

    @Operation(summary = "Lista de usuários (admin)", responses = {
        @ApiResponse(responseCode = "200", description = "Usuários", useReturnTypeSchema = true)
    })
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PageResponse<UsuarioDtoList>> lista(
            @Parameter(description = "Busca pelo email do usuário (equal)") @RequestParam(value = "email", required = false) String email,
            @Parameter(description = "Busca pelo nome do usuário (ilike)") @RequestParam(value = "nome", required = false) String nome,
            @ParameterObject Pageable pageable) {
        final Page<UsuarioDtoList> usuarios;
        if (StringUtils.isNotBlank(email)) {
            usuarios = usuarioService.findByEmail(email, pageable);
        } else if (StringUtils.isNotBlank(nome)) {
            usuarios = usuarioService.findByNome(nome, pageable);
        } else {
            usuarios = usuarioService.findAll(pageable, UsuarioDtoList.class);
        }
        return ResponseEntity.ok(new PageResponse<>(usuarios));
    }

    @Operation(summary = "Informações do usuário (admin)", responses = {
        @ApiResponse(responseCode = "200", description = "Detalhes Usuário", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioDto.class)))
    })
    @NotFoundApiResponse
    @GetMapping("/{idUsuario}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UsuarioDto> detalha(@Parameter(description = "ID do usuário") @PathVariable("idUsuario") final Long idUsuario) {
        final UsuarioDto usuario = usuarioService.getById(idUsuario, UsuarioDto.class);
        return ResponseEntity.ok(usuario);
    }

    @Operation(summary = "Cadastro de usuário (admin)", responses = {
        @ApiResponse(responseCode = "200", description = "Usuário cadastrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioDto.class)))
    })
    @InvalidDataApiResponse
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UsuarioDto> cadastra(@Valid @RequestBody UsuarioDtoCadastro dtoCadastro) {
        final UsuarioDto usuario = usuarioService.save(dtoCadastro);
        if (!usuario.getConfirmado()) {
            eventPublisher.publishEvent(new ConfirmacaoContaEvent(usuario));
        }
        return ResponseEntity.ok(usuario);
    }

    @Operation(summary = "Atualiza cadastro de usuário (admin)", responses = {
        @ApiResponse(responseCode = "200", description = "Usuário atualizado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioDto.class)))
    })
    @NotFoundApiResponse
    @InvalidDataApiResponse
    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UsuarioDto> atualiza(@Valid @RequestBody UsuarioDtoAlteracao dtoCadastro) {
        final UsuarioDto usuario = usuarioService.update(dtoCadastro);
        if (!usuario.getConfirmado()) {
            eventPublisher.publishEvent(new ConfirmacaoContaEvent(usuario));
        }
        return ResponseEntity.ok(usuario);
    }

    @Operation(summary = "Remove usuário (admin)", responses = {
        @ApiResponse(responseCode = "200", description = "Usuário removido", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class)))
    })
    @NotFoundApiResponse
    @DeleteMapping("/{idUsuario}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MessageResponse> deleta(@Parameter(description = "ID do usuário") @PathVariable("idUsuario") final Long idUsuario) {
        final Usuario usuario = usuarioService.getById(idUsuario);
        usuarioService.delete(usuario);
        return ResponseEntity.ok(new MessageResponse("Usuário excluído"));
    }

    @Operation(summary = "Adiciona perfil ao usuário (admin)", responses = {
        @ApiResponse(responseCode = "200", description = "Perfil adicionado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioDto.class)))
    })
    @NotFoundApiResponse
    @InvalidDataApiResponse
    @PutMapping("/perfil")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UsuarioDto> adicionaPerfil(@Valid @RequestBody UsuarioDtoPerfil dtoPerfil) {
        final UsuarioDto usuario = usuarioService.addPerfil(dtoPerfil);
        return ResponseEntity.ok().body(usuario);
    }

    @Operation(summary = "Remove perfil do usuário (admin)", responses = {
        @ApiResponse(responseCode = "200", description = "Perfil removido", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioDto.class)))
    })
    @NotFoundApiResponse
    @InvalidDataApiResponse
    @DeleteMapping("/perfil")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UsuarioDto> removePerfil(@Valid @RequestBody UsuarioDtoPerfil dtoPerfil) {
        final UsuarioDto usuario = usuarioService.removePerfil(dtoPerfil);
        return ResponseEntity.ok().body(usuario);
    }

    @Operation(summary = "Assinaturas vigentes do User logado", responses = {
        @ApiResponse(responseCode = "200", description = "Lista de assinaturas vigentes", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AssinaturaDto.class)))
    })
    @GetMapping("/assinatura/vigentes")
    public ResponseEntity<List<AssinaturaDto>> listaAssinaturasVigentes() {
        final Usuario UserLogado = getUserLogado();
        final List<AssinaturaDto> assinaturas = assinaturaService.listVigenteByUser(UserLogado.getId());
        return ResponseEntity.ok(assinaturas);
    }

    // @Operation(summary = "Assinaturas vigentes de uma coleira pertencente ao User logado", responses = {
    //     @ApiResponse(responseCode = "200", description = "Lista de assinaturas vigentes de uma coleira", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AssinaturaDto.class)))
    // })
    // @GetMapping("/assinatura/vigentes/{idColeira}")
    // public ResponseEntity<List<AssinaturaDto>> listaAssinaturasVigentesByColeira(@Parameter(description = "ID da coleira") @PathVariable("idColeira") final Long idColeira) {
    //     final Usuario UserLogado = getUserLogado();
    //     final List<AssinaturaDto> assinaturas = assinaturaService.listVigenteByUserColeira(UserLogado.getId(), idColeira);
    //     return ResponseEntity.ok(assinaturas);
    // }
}
