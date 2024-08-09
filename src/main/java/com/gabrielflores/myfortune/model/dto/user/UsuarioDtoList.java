package com.gabrielflores.myfortune.model.dto.user;

import org.springframework.beans.factory.annotation.Value;

/**
 *
 * @Author: Gabriel Flores - gabrielmelloflores@gmail.com  
 */
public interface UsuarioDtoList {

    Long getId();

    String getNome();

    String getEmail();

    Boolean getAtivo();

    Boolean getConfirmado();

    @Value("#{target.local}")
    Boolean getLocal();

}
