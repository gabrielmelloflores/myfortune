package com.gabrielflores.myfortune.model.user;

import com.gabrielflores.myfortune.model.entity.EntidadeBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.hibernate.envers.Audited;

/** 
* @Author: Gabriel Flores - gabrielmelloflores@gmail.com  
* @Date: 2024-08-07 09:38:49  
*/
@Data
@NoArgsConstructor
@Accessors(chain = true)
@ToString(callSuper = false, onlyExplicitlyIncluded = true)
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
@Entity
@Audited
@Table(name = "perfis")
public class Perfil extends EntidadeBase {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "perfil_seq")
    @SequenceGenerator(name = "perfil_seq", sequenceName = "seq_perfis", allocationSize = 1)
    private Long id;

    @NotEmpty
    @ToString.Include
    @EqualsAndHashCode.Include
    @Column(name = "nome")
    private String nome;

}
