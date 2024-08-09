package com.gabrielflores.myfortune.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.MappedSuperclass;
import java.util.Comparator;
import java.util.Optional;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/** 
* @Author: Gabriel Flores - gabrielmelloflores@gmail.com  
* @Date: 2024-08-07 09:38:49  
*/
@ToString(callSuper = false, onlyExplicitlyIncluded = true)
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
@MappedSuperclass
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public abstract class Entidade {

    @ToString.Include
    @EqualsAndHashCode.Include
    public abstract Long getId();

    @JsonIgnore
    public boolean isNew() {
        return getId() == null;
    }

    public static int hashCodeById(Entidade e) {
        return Optional.ofNullable(e).map(Entidade::getId).map(id -> id.hashCode()).orElse(0);
    }

    public static boolean equalsById(Entidade e1, Entidade e2) {
        if (e1 == e2) {
            return true;
        }
        if (e1 == null || e2 == null) {
            return false;
        }
        return e1.getClass().isAssignableFrom(e2.getClass())
                && e1.getId() != null
                && e1.getId().equals(e2.getId());
    }

    public static int compareById(Entidade e1, Entidade e2) {
        return EntidadeComparator.compare(e1, e2);
    }

    public static Comparator<Entidade> EntidadeComparator = Comparator
            .nullsFirst(Comparator
                    .comparing(Entidade::getId, Comparator
                            .nullsFirst(Long::compare)));
}

