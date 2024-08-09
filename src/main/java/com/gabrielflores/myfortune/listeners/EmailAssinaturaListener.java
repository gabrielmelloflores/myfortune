package com.gabrielflores.myfortune.listeners;

import com.gabrielflores.myfortune.email.EmailSender;
import com.gabrielflores.myfortune.email.TipoEmail;
import com.gabrielflores.myfortune.listeners.event.AssinaturaEvent;
import com.gabrielflores.myfortune.model.assinatura.Assinatura;
import com.gabrielflores.myfortune.model.user.Usuario;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 *
 * @author Douglas Pasqualin <douglas.pasqualin@gmail.com>
 */
@Component
@RequiredArgsConstructor
public class EmailAssinaturaListener implements ApplicationListener<AssinaturaEvent> {

    private final Locale locale;

    private final EmailSender mailSender;

    @Qualifier("localDateFormat")
    private final DateTimeFormatter localDateFormat;

    @Override
    public void onApplicationEvent(AssinaturaEvent event) {
        final TipoEmail tipoEmail = event.getTipo();
        final Assinatura assinatura = event.getAssinatura();
        final Usuario usuario = assinatura.getUser();

        final Map<String, Object> velocityParams = new HashMap<>(6);
        velocityParams.put("nome", usuario.getNome());
        velocityParams.put("email", usuario.getEmail());
        velocityParams.put("urlCheckout", assinatura.getUrlCheckout());
        velocityParams.put("valor", NumberFormat.getCurrencyInstance(locale).format(assinatura.getValor()));
        velocityParams.put("nomePlano", assinatura.getPlano().getDescricao());
        velocityParams.put("dataEncerramento", localDateFormat.format(assinatura.getDataProximaCobranca()));

        final Map<String, String> imageParams = new HashMap<>(3);
        imageParams.put("logo_avmb", "avmb.png");
        imageParams.put("logo_pet_connect", "pet_connect.png");
        switch (tipoEmail) {
            // case ASSINATURA_PEDIDO_RECEBIDO, ASSINATURA_PAGA, ASSINATURA_VENCENDO ->
            case ASSINATURA_NAO_PAGA, ASSINATURA_CANCELADA, ASSINATURA_NAO_AUTORIZADO, ASSINATURA_VENCIDA -> {
                imageParams.put("alerta", "alert.png");
            }
        }
        mailSender.enviaEmailPadrao(usuario.getEmail(), tipoEmail, velocityParams, imageParams);
    }

}
