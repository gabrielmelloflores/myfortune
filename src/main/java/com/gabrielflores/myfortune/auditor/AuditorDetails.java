package com.gabrielflores.myfortune.auditor;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/* 
* @Author: Gabriel Flores - gabrielmelloflores@gmail.com  
* @Date: 2024-08-07 09:38:49  
*/
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class AuditorDetails {

    @Column(name = "operador")
    private Long operador;

    @Column(name = "endereco_ip")
    private String endereco;
}
