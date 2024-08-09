package com.gabrielflores.myfortune.auditor;

import com.gabrielflores.myfortune.model.entity.AuditRevisionEntity;
import com.gabrielflores.myfortune.security.ObjectHolder;
import org.hibernate.envers.RevisionListener;

/**
 *
 * @author Giuliano Ferreira <giuliano@ufsm.br>
 */
public class AuditRevisionListener implements RevisionListener {

    @Override
    public void newRevision(Object revisionEntity) {
        final Long currentUserId = ObjectHolder.getCurrentUserId();
        final AuditRevisionEntity audit = (AuditRevisionEntity) revisionEntity;
        audit.setUserId(currentUserId);
    }

}
