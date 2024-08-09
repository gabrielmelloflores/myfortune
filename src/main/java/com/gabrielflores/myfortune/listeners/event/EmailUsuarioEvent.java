package com.gabrielflores.myfortune.listeners.event;

import com.gabrielflores.myfortune.email.TipoEmail;
import com.gabrielflores.myfortune.model.dto.user.UsuarioDto;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 *
 * @author Giuliano Ferreira <giuliano@ufsm.br>
 */
public class EmailUsuarioEvent extends ApplicationEvent {

    @Getter
    private final TipoEmail tipo;

    protected EmailUsuarioEvent(UsuarioDto usuario, TipoEmail tipo) {
        super(usuario);
        this.tipo = tipo;
    }

    public UsuarioDto getUsuario() {
        return (UsuarioDto) getSource();
    }

}
