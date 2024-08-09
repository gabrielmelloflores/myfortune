package com.gabrielflores.myfortune.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.RevisionEntity;
import org.hibernate.envers.RevisionNumber;
import org.hibernate.envers.RevisionTimestamp;

/**
 *
 * @author Giuliano Ferreira <giuliano@ufsm.br>
 */
@Getter
@Setter
@Entity
@Table(name = "revisions", schema = "audit")
@RevisionEntity
public class AuditRevisionEntity {

    @Id
    @RevisionNumber
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "revision_seq")
    @SequenceGenerator(name = "revision_seq", sequenceName = "revisions_seq", schema = "audit", allocationSize = 50)
    private Long id;

    @RevisionTimestamp
    private Long timestamp;

    @Column
    private Long userId;

}
