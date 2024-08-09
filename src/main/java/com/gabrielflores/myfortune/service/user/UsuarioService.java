package com.gabrielflores.myfortune.service.user;

import com.gabrielflores.myfortune.dto.ConfimaContaDto;
import com.gabrielflores.myfortune.dto.PasswordDto;
import com.gabrielflores.myfortune.dto.PasswordTokenDto;
import com.gabrielflores.myfortune.dto.user.UsuarioDtoAlteracao;
import com.gabrielflores.myfortune.dto.user.UsuarioDtoCadastro;
import com.gabrielflores.myfortune.dto.user.UsuarioDtoPerfil;
import com.gabrielflores.myfortune.exception.EntityNotFoundException;
import com.gabrielflores.myfortune.exception.InvalidTokenException;
import com.gabrielflores.myfortune.exception.RegraNegocioException;
import com.gabrielflores.myfortune.model.assinatura.Assinatura;
import com.gabrielflores.myfortune.model.assinatura.Plano;
import com.gabrielflores.myfortune.model.dto.user.UsuarioDto;
import com.gabrielflores.myfortune.model.dto.user.UsuarioDtoList;
import com.gabrielflores.myfortune.model.user.Perfil;
import com.gabrielflores.myfortune.model.user.Provider;
import com.gabrielflores.myfortune.model.user.Usuario;
import com.gabrielflores.myfortune.redis.UsuarioToken;
import com.gabrielflores.myfortune.redis.repository.RefreshTokenRepository;
import com.gabrielflores.myfortune.redis.repository.UsuarioTokenRepository;
import com.gabrielflores.myfortune.repository.user.UsuarioRepository;
import com.gabrielflores.myfortune.service.BaseService;
import com.gabrielflores.myfortune.service.assinatura.AssinaturaService;
import com.gabrielflores.myfortune.service.assinatura.PlanoService;
import com.gabrielflores.myfortune.util.TextUtils;
import jakarta.transaction.Transactional;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/** 
 * @Author: Gabriel Flores - gabrielmelloflores@gmail.com  
*/
@Service
@RequiredArgsConstructor
public class UsuarioService extends BaseService<Usuario, UsuarioRepository> {

    private final PasswordEncoder passwordEncoder;

    private final UsuarioTokenRepository tokeRepository;

    private final RefreshTokenRepository refreshTokenRepository;

    private final PerfilService perfilService;
    
    private final AssinaturaService assinaturaService;

    public Usuario findByToken(String token) {
        return tokeRepository.findByToken(token)
                .map(UsuarioToken::getId)
                .map(this::getById)
                .orElseThrow(() -> new InvalidTokenException("Token inválido"));
    }

    public Usuario findByLogin(String login) throws EntityNotFoundException {
        return repository.findByEmail(login)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado: " + login));
    }

    public <T> T getByEmail(String email, Class<T> type) {
        return findByEmail(email, type)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado: " + email));
    }

    public <T> Optional<T> findByEmail(String email, Class<T> type) {
        return repository.findByEmail(email, type);
    }

    public Page<UsuarioDtoList> findByEmail(String email, Pageable pageable) {
        return repository.findByEmail(email, pageable);
    }

    public Page<UsuarioDtoList> findByNome(String nome, Pageable pageable) {
        return repository.findByNomeContainingIgnoreCase(nome, pageable);
    }

    @Transactional
    public UsuarioDto save(UsuarioDtoCadastro dto) {
        String senha = dto.getSenha();
        if (StringUtils.isBlank(senha)) {
            senha = TextUtils.generateRandomString(8);
        }

        Usuario usuario = new Usuario()
                .setEmail(dto.getEmail())
                .setCpf(dto.getCpf())
                .setNome(dto.getNome())
                .setGenero(dto.getGenero())
                .setNascimento(dto.getNascimento())
                .setTelefone(dto.getTelefone())
                .setAtivo(dto.isAtivo())
                .setConfirmado(Boolean.FALSE)
                .setSenha(passwordEncoder.encode(senha))
                .setProvider(Provider.LOCAL);
        if (dto.getPerfil() != null) {
            final Perfil perfil = perfilService.getById(dto.getPerfil());
            usuario.addPerfil(perfil);
        }
        usuario = save(usuario);

        // Após salvar o usuário, associa a assinatura ao usuário
        if (dto.getIdAssinatura() != null) {
            Assinatura assinatura = assinaturaService.getById(dto.getIdAssinatura());

            assinatura.setUser(usuario);
            assinaturaService.save(assinatura);
        }

        return getById(usuario.getId(), UsuarioDto.class);
    }

    @Transactional
    public UsuarioDto update(UsuarioDtoAlteracao dto) {
        Usuario usuario = getById(dto.getId());
        boolean confirmado = Objects.equals(dto.getEmail(), usuario.getEmail());
        if (!confirmado) {
            refreshTokenRepository.deleteTokens(usuario.getEmail());
        }
        usuario
                .setEmail(dto.getEmail())
                .setCpf(dto.getCpf())
                .setNome(dto.getNome())
                .setGenero(dto.getGenero())
                .setNascimento(dto.getNascimento())
                .setTelefone(dto.getTelefone())
                .setAtivo(dto.isAtivo())
                .setConfirmado(confirmado);
        usuario = update(usuario);
        return getById(usuario.getId(), UsuarioDto.class);
    }

    @Transactional
    public UsuarioDto addPerfil(UsuarioDtoPerfil dto) {
        final Usuario usuario = getById(dto.getId());
        Arrays.stream(dto.getPerfis()).forEach(nomePerfil -> {
            Optional<Perfil> perfil = perfilService.findByNome(nomePerfil);
            if (perfil.isPresent() && !usuario.hasPerfil(nomePerfil)) {
                usuario.addPerfil(perfil.get());
            }
        });
        update(usuario);
        return getById(usuario.getId(), UsuarioDto.class);
    }

    @Transactional
    public UsuarioDto removePerfil(UsuarioDtoPerfil dto) {
        final Usuario usuario = getById(dto.getId());
        Arrays.stream(dto.getPerfis()).forEach(nomePerfil -> {
            Optional<Perfil> perfil = perfilService.findByNome(nomePerfil);
            if (perfil.isPresent() && usuario.hasPerfil(nomePerfil)) {
                usuario.removePerfil(perfil.get());
            }
        });
        update(usuario);
        return getById(usuario.getId(), UsuarioDto.class);
    }

    @Transactional
    public void alteraSenha(PasswordDto passwordDto) {
        Usuario usuario = getById(passwordDto.getId());
        //TODO: se o cadastro veio por google ou apple, não tem senhaAtual ... deixar alterar igual e transformar em provider Local?
        if (passwordEncoder.matches(passwordDto.getSenhaAtual(), usuario.getSenha())) {
            usuario.setSenha(passwordEncoder.encode(passwordDto.getNovaSenha()));
            usuario.setProvider(Provider.LOCAL);
            update(usuario);
            refreshTokenRepository.deleteTokens(usuario.getEmail());
        } else {
            throw new RegraNegocioException("A senha atual não corresponde");
        }
    }

    @Transactional
    public void alteraSenha(PasswordTokenDto passwordDto) {
        final UsuarioToken token = tokeRepository.findByToken(passwordDto.getToken()).orElseThrow(() -> new InvalidTokenException("Token inválido"));
        Usuario usuario = getById(token.getId());
        usuario.setSenha(passwordEncoder.encode(passwordDto.getNovaSenha()));
        usuario.setProvider(Provider.LOCAL);
        tokeRepository.delete(token);
        refreshTokenRepository.deleteTokens(usuario.getEmail());
    }

    @Transactional
    public UsuarioDto confirmaConta(ConfimaContaDto tokenDto) {
        final UsuarioToken token = tokeRepository.findByToken(tokenDto.getToken()).orElseThrow(() -> new InvalidTokenException("Token inválido"));
        Usuario usuario = getById(token.getId());
        usuario.setConfirmado(Boolean.TRUE);
        usuario = update(usuario);
        tokeRepository.delete(token);
        return getById(usuario.getId(), UsuarioDto.class);
    }

}
