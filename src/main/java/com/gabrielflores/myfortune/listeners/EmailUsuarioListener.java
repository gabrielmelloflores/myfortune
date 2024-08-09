package com.gabrielflores.myfortune.listeners;

import com.gabrielflores.myfortune.email.EmailSender;
import com.gabrielflores.myfortune.email.TipoEmail;
import com.gabrielflores.myfortune.listeners.event.EmailUsuarioEvent;
import com.gabrielflores.myfortune.model.dto.user.UsuarioDto;
import com.gabrielflores.myfortune.redis.UsuarioToken;
import com.gabrielflores.myfortune.redis.repository.UsuarioTokenRepository;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 *
 * @author Giuliano Ferreira <giuliano@ufsm.br>
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class EmailUsuarioListener implements ApplicationListener<EmailUsuarioEvent> {

    private final Environment environment;

    private final EmailSender mailSender;

    private final UsuarioTokenRepository usuarioTokenRepository;

    @Override
    public void onApplicationEvent(EmailUsuarioEvent event) {
        final TipoEmail tipoEmail = event.getTipo();
        final UsuarioDto usuario = event.getUsuario();
        final UsuarioToken token = new UsuarioToken(usuario.getId(), UUID.randomUUID().toString());
        usuarioTokenRepository.save(token);
        log.info("Event -> {} : token -> {}", tipoEmail, token.getToken());

        String url = environment.getProperty(tipoEmail.getLink()) + "?token=" + token.getToken();
        final Map<String, Object> velocityParams = new HashMap<>(3);
        velocityParams.put("nome", usuario.getNome());
        velocityParams.put("email", usuario.getEmail());
        velocityParams.put("url", url);

        final Map<String, String> imageParams = new HashMap<>(3);
        imageParams.put("logo_avmb", "avmb.png");
        imageParams.put("pets", "cachorrada.png");
        switch (tipoEmail) {
            case CONFIRMACAO_CONTA -> imageParams.put("logo_pet_connect", "pet_connect.png");
            case RECUPERACAO_SENHA -> imageParams.put("logo_pet_connect", "pet_connect_roxo.png");
        }

        mailSender.enviaEmailEspecifico(usuario.getEmail(), tipoEmail, velocityParams, imageParams);
    }

}
