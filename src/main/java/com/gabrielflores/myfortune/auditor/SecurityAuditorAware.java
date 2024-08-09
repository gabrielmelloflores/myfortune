package com.gabrielflores.myfortune.auditor;

import com.gabrielflores.myfortune.security.ObjectHolder;
import java.util.Optional;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

/**
 *
 * @author Giuliano Ferreira <giuliano@ufsm.br>
 */
@Component
public class SecurityAuditorAware implements AuditorAware<AuditorDetails> {

    @Override
    public Optional<AuditorDetails> getCurrentAuditor() {
        final Long currentUserId = ObjectHolder.getCurrentUserId();
        final String currentUserIp = ObjectHolder.getCurrentIp();
        return Optional.ofNullable(currentUserId).map(id -> new AuditorDetails(id, currentUserIp));
    }
}
