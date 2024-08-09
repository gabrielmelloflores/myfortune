package com.gabrielflores.myfortune.model.entity;

import com.gabrielflores.myfortune.auditor.AuditorDetails;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;
import java.time.LocalDateTime;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/** 
* @Author: Gabriel Flores - gabrielmelloflores@gmail.com  
* @Date: 2024-08-07 09:38:49  
*/
@Getter
@Setter
@ToString(callSuper = true, onlyExplicitlyIncluded = true)
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class EntidadeBase extends Entidade {

    //JPA Optimistic Locking
    @JsonIgnore
    @Version
    @Column(name = "versao")
    private Integer versao;

    //JPA Auditing (data/hora criação/alteração)
    @JsonIgnore
    @CreatedDate
    @LastModifiedDate
    @Column(name = "dh_alteracao")
    private LocalDateTime alteracao;

    //JPA Auditing (operador e ip)
    @JsonIgnore
    @CreatedBy
    @LastModifiedBy
    @Embedded
    private AuditorDetails detalhes;

}
