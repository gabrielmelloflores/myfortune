package com.gabrielflores.myfortune.redis;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

/**
 *
 * @Author: Gabriel Flores - gabrielmelloflores@gmail.com  
 */
@Setter
@Getter
@Accessors(chain = true)
@AllArgsConstructor
@RedisHash(value = "usuarioToken", timeToLive = 2 * 60 * 60)
public class UsuarioToken {

    @Id
    private Long id;

    @Indexed
    private String token;

}
