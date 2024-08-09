package com.gabrielflores.myfortune.controller;

import com.gabrielflores.myfortune.annotation.DefaultApiResponses;
import com.gabrielflores.myfortune.annotation.InvalidDataApiResponse;
import com.gabrielflores.myfortune.annotation.NotFoundApiResponse;
import com.gabrielflores.myfortune.dto.ConfimaContaDto;
import com.gabrielflores.myfortune.dto.LoginDto;
import com.gabrielflores.myfortune.dto.LogoutDto;
import com.gabrielflores.myfortune.dto.PasswordRecoverDto;
import com.gabrielflores.myfortune.dto.PasswordTokenDto;
import com.gabrielflores.myfortune.dto.RefreshTokenDto;
import com.gabrielflores.myfortune.exception.EntityNotFoundException;
import com.gabrielflores.myfortune.exception.InvalidTokenException;
import com.gabrielflores.myfortune.listeners.event.RecuperacaoSenhaEvent;
import com.gabrielflores.myfortune.model.dto.user.UsuarioDto;
import com.gabrielflores.myfortune.model.user.Provider;
import com.gabrielflores.myfortune.model.user.Usuario;
// import com.gabrielflores.myfortune.oauth2.AppleOAuth2UserInfo;
// import com.gabrielflores.myfortune.oauth2.GoogleOAuth2Credential;
// import com.gabrielflores.myfortune.oauth2.GoogleOAuth2UserInfo;
import com.gabrielflores.myfortune.password.PasswordChecker;
import com.gabrielflores.myfortune.password.PasswordValidationRule;
import com.gabrielflores.myfortune.redis.RefreshToken;
import com.gabrielflores.myfortune.redis.repository.RefreshTokenRepository;
import com.gabrielflores.myfortune.response.AuthTokenResponse;
import com.gabrielflores.myfortune.response.ErrorResponse;
import com.gabrielflores.myfortune.response.MessageResponse;
// import com.gabrielflores.myfortune.security.AppleOAuth2Service;
// import com.gabrielflores.myfortune.security.GoogleOAuth2Service;
import com.gabrielflores.myfortune.security.JwtTokenUtil;
import com.gabrielflores.myfortune.security.ObjectHolder;
import com.gabrielflores.myfortune.security.UserPrincipal;
// import com.gabrielflores.myfortune.service.FirebaseService;
import com.gabrielflores.myfortune.service.user.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Giuliano Ferreira <giuliano@ufsm.br>
 */
@Slf4j
@RestController
@DefaultApiResponses
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Tag(name = "Autenticação", description = "Gerenciamento de autenticação")
public class AutenticacaoController {

    private final JwtTokenUtil jwtTokenUtil;

    private final PasswordChecker passwordChecker;

    private final UserDetailsService userDetailsService;

    private final UsuarioService usuarioService;

    // private final FirebaseService firebaseService;

    private final AuthenticationManager authenticationManager;

    private final RefreshTokenRepository refreshTokenRepository;

    private final ApplicationEventPublisher eventPublisher;

    // private final GoogleOAuth2Service googleOAuth2Service;

    // private final AppleOAuth2Service appleOAuth2Service;

    @Operation(summary = "Autenticar usuário", responses = {
        @ApiResponse(responseCode = "200", description = "Autenticado com sucesso!", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AuthTokenResponse.class))),
        @ApiResponse(responseCode = "400", description = "Falha na autenticação", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @InvalidDataApiResponse
    @PostMapping(value = "/login")
    public ResponseEntity<AuthTokenResponse> login(@Valid @RequestBody LoginDto loginDto) {
        final Authentication authToken = new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getSenha());
        final Authentication authentication = authenticationManager.authenticate(authToken);
        String token = jwtTokenUtil.generateToken(authentication);
        Date expirationDate = jwtTokenUtil.getExpirationDateFromToken(token);
        String refreshToken = jwtTokenUtil.generateRefreshToken();
        UserPrincipal user = (UserPrincipal) authentication.getPrincipal();
        refreshTokenRepository.saveToken(user.getUsername(), refreshToken);

        // if (StringUtils.isNotBlank(loginDto.getTokenNotificacao())) {
        //     try {
        //         ObjectHolder.setCurrentUserId(user.getId());
        //         final Usuario usuario = usuarioService.getById(user.getId());
        //         firebaseService.registraToken(usuario, loginDto.getTokenNotificacao());
        //     } finally {
        //         ObjectHolder.removeCurrentUserId();
        //     }
        // }
        return ResponseEntity.ok(new AuthTokenResponse(token, refreshToken, expirationDate.getTime()));
    }

    @Operation(summary = "Desautenticar usuário", responses = {
        @ApiResponse(responseCode = "200", description = "Desautenticado com sucesso!", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class)))
    })
    @SecurityRequirement(name = "bearer-key")
    @PreAuthorize("isAuthenticated()")
    @DeleteMapping(value = "/login")
    public ResponseEntity<MessageResponse> logout(@Valid @RequestBody LogoutDto logoutDto) {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        final UserPrincipal user = (UserPrincipal) authentication.getPrincipal();

        if (StringUtils.isNotBlank(logoutDto.getRefreshToken())) {
            refreshTokenRepository.findByToken(logoutDto.getRefreshToken()).ifPresent(token -> {
                if (!token.getUsername().equals(user.getUsername())) {
                    throw new InvalidTokenException("Token inválido");
                }
                refreshTokenRepository.delete(token);
            });
        } else {
            refreshTokenRepository.deleteTokens(user.getUsername());
        }

        // if (StringUtils.isNotBlank(logoutDto.getNotificationToken())) {
        //     firebaseService.invalidaToken(logoutDto.getNotificationToken());
        // }
        return ResponseEntity.ok(new MessageResponse("Logout efetuado"));
    }

    // @Operation(summary = "Autenticar usuário usando Token OAuth2 Google", responses = {
    //     @ApiResponse(responseCode = "200", description = "Autenticado com sucesso!", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AuthTokenResponse.class))),
    //     @ApiResponse(responseCode = "400", description = "Falha na autenticação", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    // })
    // @InvalidDataApiResponse
    // @PostMapping(value = "/login/google")
    // public ResponseEntity<AuthTokenResponse> loginGoogle(final @RequestBody GoogleOAuth2Credential googleOAuth2Credential) {
    //     final GoogleOAuth2UserInfo googleUser = googleOAuth2Service.getGoogleUserFromToken(googleOAuth2Credential);
    //     final Usuario usuario = usuarioService.findCreateUsuarioOAuth2(googleUser, Provider.GOOGLE);
    //     return ResponseEntity.ok(getAuthToken(usuario));
    // }

    // @Operation(summary = "Autenticar usuário usando Token OAuth2 Apple", responses = {
    //     @ApiResponse(responseCode = "200", description = "Autenticado com sucesso!", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AuthTokenResponse.class))),
    //     @ApiResponse(responseCode = "400", description = "Falha na autenticação", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    // })
    // @InvalidDataApiResponse
    // @PostMapping(value = "/login/apple")
    // public ResponseEntity<AuthTokenResponse> loginApple(@RequestBody AppleOAuth2UserInfo appleUserInfo) {
    //     try {
    //         log.info("Iniciando autenticacao apple -> {}", appleUserInfo);
    //         final Map<String, Object> attributes = appleOAuth2Service.getAttributesFromTokenApple(appleUserInfo);
    //         appleUserInfo = new AppleOAuth2UserInfo(Map.of("subject", attributes.get("subject"), "email", attributes.get("email"), "nome", appleUserInfo.getName()));
    //     } catch (Exception ex) {
    //         throw new InvalidTokenException("Erro autenticando com token Apple", ex);
    //     }
    //     log.info("Usuario identificado -> {}", appleUserInfo);
    //     final Usuario usuario = usuarioService.findCreateUsuarioOAuth2(appleUserInfo, Provider.APPLE);
    //     log.info("Usuario autenticado -> {}", usuario);
    //     return ResponseEntity.ok(getAuthToken(usuario));
    // }

    // private AuthTokenResponse getAuthToken(final Usuario usuario) {
    //     final Authentication authToken = new UsernamePasswordAuthenticationToken(usuario.getEmail(), usuario.getSenha());
    //     final String token = jwtTokenUtil.generateToken(authToken);
    //     final Date expirationDate = jwtTokenUtil.getExpirationDateFromToken(token);
    //     final String refreshToken = jwtTokenUtil.generateRefreshToken();
    //     final String userName = jwtTokenUtil.getUsernameFromToken(token);
    //     refreshTokenRepository.saveToken(userName, refreshToken);
    //     return new AuthTokenResponse(token, refreshToken, expirationDate.getTime());
    // }

    @Operation(summary = "Renova o token de autenticação", responses = {
        @ApiResponse(responseCode = "200", description = "Novo token gerado com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AuthTokenResponse.class)))
    })
    @InvalidDataApiResponse
    @PostMapping(value = "/refresh")
    public ResponseEntity<AuthTokenResponse> refresh(@Valid @RequestBody RefreshTokenDto refreshTokenDto) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(refreshTokenDto.getToken())
                .orElseThrow(() -> new InvalidTokenException("Token inválido"));
        final UserDetails user = userDetailsService.loadUserByUsername(refreshToken.getUsername());
        String token = jwtTokenUtil.generateToken(user);
        Date expirationDate = jwtTokenUtil.getExpirationDateFromToken(token);
        return ResponseEntity.ok(new AuthTokenResponse(token, refreshToken.getToken(), expirationDate.getTime()));
    }

    @Operation(summary = "Confirma conta de usuário", responses = {
        @ApiResponse(responseCode = "200", description = "Usuário confirmado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioDto.class)))
    })
    @InvalidDataApiResponse
    @PostMapping(value = "/confirm")
    public ResponseEntity<UsuarioDto> confirm(@Valid @RequestBody ConfimaContaDto confimaContaDto) {
        final UsuarioDto usuario = usuarioService.confirmaConta(confimaContaDto);
        return ResponseEntity.ok(usuario);
    }

    @Operation(summary = "Altera senha do usuário identificado pelo token", responses = {
        @ApiResponse(responseCode = "200", description = "Senha alterada", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class)))
    })
    @InvalidDataApiResponse
    @PostMapping(value = "/password-reset")
    public ResponseEntity<MessageResponse> passwordReset(@Valid @RequestBody PasswordTokenDto passwordDto) {
        usuarioService.alteraSenha(passwordDto);
        return ResponseEntity.ok(new MessageResponse("Senha alterada"));
    }

    @Operation(summary = "Iniciar recuperação de senha de usuário", responses = {
        @ApiResponse(responseCode = "200", description = "Link para resetar senha enviado para o e-mail cadastrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class)))
    })
    @NotFoundApiResponse
    @InvalidDataApiResponse
    @PostMapping(value = "/password-forgot")
    public ResponseEntity<MessageResponse> passwordForgot(@Valid @RequestBody PasswordRecoverDto passwordDto) {
        final UsuarioDto usuario = usuarioService.findByEmail(passwordDto.getEmail(), UsuarioDto.class)
                .orElseThrow(() -> new EntityNotFoundException("Este e-mail não está cadastrado no sistema!"));
        eventPublisher.publishEvent(new RecuperacaoSenhaEvent(usuario));
        return ResponseEntity.ok(new MessageResponse("Um link para resetar sua senha foi enviado para seu e-mail"));
    }

    @Operation(summary = "Regras para senha criação de senha", responses = {
        @ApiResponse(responseCode = "200", description = "Regras para senha", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = String.class))))
    })
    @GetMapping("/password-rules")
    public List<String> passwordRules() {
        return passwordChecker.getValidationRules().stream()
                .map(PasswordValidationRule::getValidationMessage)
                .sorted().collect(Collectors.toList());
    }
}
