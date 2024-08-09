package com.gabrielflores.myfortune.repository.user;

import com.gabrielflores.myfortune.model.dto.user.UsuarioDtoList;
import com.gabrielflores.myfortune.model.user.Usuario;
import com.gabrielflores.myfortune.repository.BaseRepository;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;

/**
 *
 * @Author: Gabriel Flores - gabrielmelloflores@gmail.com  
 */
public interface UsuarioRepository extends BaseRepository<Usuario> {

    @EntityGraph(value = "usuarioPerfis", type = EntityGraphType.LOAD)  
    Optional<Usuario> findWithPerfisByEmail(String email);

    Optional<Usuario> findByCpf(String cpf);

    Optional<Usuario> findByEmail(String email);

    <T> Optional<T> findByEmail(String email, Class<T> type);

    Page<UsuarioDtoList> findByEmail(String email, Pageable pageable);

    Page<UsuarioDtoList> findByNomeContainingIgnoreCase(String nome, Pageable pageable);
}
