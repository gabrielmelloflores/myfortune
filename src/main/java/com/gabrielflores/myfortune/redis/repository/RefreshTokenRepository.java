package com.gabrielflores.myfortune.redis.repository;

import com.gabrielflores.myfortune.redis.RefreshToken;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @Author: Gabriel Flores - gabrielmelloflores@gmail.com  
 */
public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {

    Optional<RefreshToken> findByToken(String token);

    List<RefreshToken> findByUsername(String username);

    default RefreshToken saveToken(String username, String token) {
        String id = UUID.randomUUID().toString();
        RefreshToken refreshToken = new RefreshToken(id, username, token);
        return save(refreshToken);
    }

    default void deleteTokens(String username) {
        List<RefreshToken> tokens = findByUsername(username);
        deleteAll(tokens);
    }

}
