package com.gabrielflores.myfortune.redis.repository;

import com.gabrielflores.myfortune.redis.UsuarioToken;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @Author: Gabriel Flores - gabrielmelloflores@gmail.com  
 */
public interface UsuarioTokenRepository extends CrudRepository<UsuarioToken, Long> {

    Optional<UsuarioToken> findByToken(String token);

}
