package com.gabrielflores.myfortune.listeners.event;

import com.gabrielflores.myfortune.email.TipoEmail;
import com.gabrielflores.myfortune.model.dto.user.UsuarioDto;

/**
 *
 * @author Giuliano Ferreira <giuliano@ufsm.br>
 */
public class RecuperacaoSenhaEvent extends EmailUsuarioEvent {

    public RecuperacaoSenhaEvent(UsuarioDto usuario) {
        super(usuario, TipoEmail.RECUPERACAO_SENHA);
    }

}
