package com.gabrielflores.myfortune.model.dto.user;

import com.gabrielflores.myfortune.model.user.Genero;
import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;

/**
 *
 * @Author: Gabriel Flores - gabrielmelloflores@gmail.com  
 */
public interface UsuarioDto extends UsuarioDtoList {

    String getCpf();

    String getTelefone();

    @Value("#{target.dataNascimento}")
    String getDataNascimento();

    LocalDate getNascimento();

    Genero getGenero();

    List<PerfilDto> getPerfis();

}
