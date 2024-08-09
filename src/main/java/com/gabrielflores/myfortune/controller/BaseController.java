package com.gabrielflores.myfortune.controller;

import com.gabrielflores.myfortune.exception.EntityNotFoundException;
import com.gabrielflores.myfortune.model.assinatura.Assinatura;
import com.gabrielflores.myfortune.model.user.Usuario;
import com.gabrielflores.myfortune.security.UserPrincipal;
import com.gabrielflores.myfortune.service.user.UsuarioService;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 *
 * @author Douglas Pasqualin <douglas.pasqualin@gmail.com>
 */
public abstract class BaseController {

    @Autowired
    protected UsuarioService usuarioService;

    protected UserPrincipal getUsuarioLogado() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (UserPrincipal) authentication.getPrincipal();
    }

    protected Usuario getUserLogado() {
        final UserPrincipal user = getUsuarioLogado();
        return usuarioService.getById(user.getId());
    }

    protected void validaAssinatura(Assinatura assinatura) {
        final UserPrincipal user = getUsuarioLogado();
        if (assinatura.getUser() == null || !assinatura.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("Acesso não autorizado");
        }
    }

}
