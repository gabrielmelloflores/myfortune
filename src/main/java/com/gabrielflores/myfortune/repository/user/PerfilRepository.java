package com.gabrielflores.myfortune.repository.user;

import com.gabrielflores.myfortune.model.user.Perfil;
import com.gabrielflores.myfortune.repository.BaseRepository;
import java.util.Optional;

/**
 *
 * @author Giuliano Ferreira <giuliano@ufsm.br>
 */
public interface PerfilRepository extends BaseRepository<Perfil> {

    Optional<Perfil> findByNome(String nome);

}
