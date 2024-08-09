package com.gabrielflores.myfortune.service.user;

import com.gabrielflores.myfortune.exception.EntityNotFoundException;
import com.gabrielflores.myfortune.model.user.Perfil;
import com.gabrielflores.myfortune.repository.user.PerfilRepository;
import com.gabrielflores.myfortune.service.BaseService;
import java.util.Optional;
import org.springframework.stereotype.Service;

/**
 *
 * @author Giuliano Ferreira <giuliano@ufsm.br>
 */
@Service
public class PerfilService extends BaseService<Perfil, PerfilRepository> {

    public Optional<Perfil> findByNome(String nome) {
        return repository.findByNome(nome);
    }

    public Perfil getPerfilUser() {
        return findByNome("User").orElseThrow(() -> new EntityNotFoundException("Perfil de User não cadastrado"));
    }
}
