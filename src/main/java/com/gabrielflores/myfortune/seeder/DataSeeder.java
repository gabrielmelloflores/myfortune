package com.gabrielflores.myfortune.seeder;

import com.gabrielflores.myfortune.model.dto.assinatura.AssinaturaDto;
import com.gabrielflores.myfortune.model.user.Perfil;
import com.gabrielflores.myfortune.model.user.Provider;
import com.gabrielflores.myfortune.model.user.Usuario;
import com.gabrielflores.myfortune.model.assinatura.Assinatura;
import com.gabrielflores.myfortune.model.assinatura.Plano;
import com.gabrielflores.myfortune.model.assinatura.VigenciaPlano;
import com.gabrielflores.myfortune.repository.assinatura.AssinaturaRepository;
import com.gabrielflores.myfortune.repository.assinatura.PlanoRepository;
import com.gabrielflores.myfortune.repository.user.PerfilRepository;
import com.gabrielflores.myfortune.repository.user.UsuarioRepository;
import com.gabrielflores.myfortune.security.ObjectHolder;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 *
 * @author Giuliano Ferreira <giuliano@ufsm.br>
 */
@Component
@RequiredArgsConstructor
public class DataSeeder implements ApplicationListener<ContextRefreshedEvent> {

    private static final String[] PERFIS = {"ADMIN", "USER"};

    private final PasswordEncoder passwordEncoder;

    private final PerfilRepository perfilRepository;

    private final UsuarioRepository usuarioRepository;

    private final PlanoRepository planoRepository; 

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        try {
            ObjectHolder.setCurrentUserId(0L);
            ObjectHolder.setCurrentIp("localhost");

            final Map<String, Perfil> perfis = loadPerfis();
            loadUsuarios(perfis);
            loadPlano();
        } finally {
            ObjectHolder.removeCurrentUserId();
            ObjectHolder.removeCurrentIp();
        }
    }

    private Map<String, Perfil> loadPerfis() {
        return Stream.of(PERFIS)
                .map(perfil
                        -> perfilRepository.findByNome(perfil).orElseGet(()
                        -> perfilRepository.save(new Perfil().setNome(perfil))
                )).collect(Collectors.toMap(Perfil::getNome, Function.identity()));
    }

    private void loadUsuarios(Map<String, Perfil> perfis) {
        Stream.of(
                new Usuario()
                        .setAtivo(Boolean.TRUE)
                        .setConfirmado(Boolean.TRUE)
                        .setEmail("admin@admin.com")
                        .setNome("Super Admin")
                        .setSenha(passwordEncoder.encode("admin"))
                        .setProvider(Provider.LOCAL)
                        .addPerfil(perfis.get("ADMIN")),
                new Usuario()
                        .setAtivo(Boolean.TRUE)
                        .setConfirmado(Boolean.TRUE)
                        .setEmail("user@user.com")
                        .setNome("User")
                        .setSenha(passwordEncoder.encode("user"))
                        .setProvider(Provider.LOCAL)
                        .addPerfil(perfis.get("USER"))
        ).forEach(usuario
                -> usuarioRepository.findByEmail(usuario.getEmail()).orElseGet(()
                        -> usuarioRepository.save(usuario)));
    }

    private void loadPlano(){
        Stream.of(
                new Plano()
                        .setAtivo(Boolean.TRUE)
                        .setDescricao("Plano Micro")
                        .setValor(BigDecimal.valueOf(50.00))
                        .setVigencia(Arrays.stream(VigenciaPlano.values())
                                .filter(v -> v.getValor().equals("A"))
                                .findFirst()
                                .orElseThrow(() -> new IllegalArgumentException("Invalid vigencia value: A")))
                        .setDias(30)
                        .setLimiteParcelas(12)
                        .setTrial(Boolean.FALSE),
                        new Plano()
                        .setAtivo(Boolean.TRUE)
                        .setDescricao("Plano Básico")
                        .setValor(BigDecimal.valueOf(100.00))
                        .setVigencia(Arrays.stream(VigenciaPlano.values())
                                .filter(v -> v.getValor().equals("A"))
                                .findFirst()
                                .orElseThrow(() -> new IllegalArgumentException("Invalid vigencia value: A")))
                        .setDias(30)
                        .setLimiteParcelas(12)
                        .setTrial(Boolean.FALSE),
                        new Plano()
                        .setAtivo(Boolean.TRUE)
                        .setDescricao("Plano Avançado")
                        .setValor(BigDecimal.valueOf(230.00))
                        .setVigencia(Arrays.stream(VigenciaPlano.values())
                                .filter(v -> v.getValor().equals("A"))
                                .findFirst()
                                .orElseThrow(() -> new IllegalArgumentException("Invalid vigencia value: A")))
                        .setDias(30)
                        .setLimiteParcelas(12)
                        .setTrial(Boolean.FALSE),
                        new Plano()
                        .setAtivo(Boolean.TRUE)
                        .setDescricao("FREE TRIAL")
                        .setValor(BigDecimal.valueOf(0))
                        .setVigencia(Arrays.stream(VigenciaPlano.values())
                                .filter(v -> v.getValor().equals("D"))
                                .findFirst()
                                .orElseThrow(() -> new IllegalArgumentException("Invalid vigencia value: D")))
                        .setDias(30)
                        .setLimiteParcelas(12)
                        .setTrial(Boolean.TRUE)
        ).forEach(plano
                -> planoRepository.save(plano));      
    }
}
