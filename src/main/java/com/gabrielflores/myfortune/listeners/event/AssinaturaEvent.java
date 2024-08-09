package com.gabrielflores.myfortune.listeners.event;

import com.gabrielflores.myfortune.email.TipoEmail;
import com.gabrielflores.myfortune.model.assinatura.Assinatura;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 *
 * @author Douglas Pasqualin <douglas.pasqualin@gmail.com>
 */
public class AssinaturaEvent extends ApplicationEvent {

    @Getter
    private final TipoEmail tipo;

    public AssinaturaEvent(final Assinatura assinatura, final TipoEmail tipo) {
        super(assinatura);
        this.tipo = tipo;
    }

    public Assinatura getAssinatura() {
        return (Assinatura) source;
    }

}
