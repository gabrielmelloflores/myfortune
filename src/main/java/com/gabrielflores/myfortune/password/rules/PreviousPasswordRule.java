package com.gabrielflores.myfortune.password.rules;

import com.gabrielflores.myfortune.password.PasswordValidationRule;
import com.gabrielflores.myfortune.util.TextUtils;
import java.util.Map;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Regra que avalia se a senha é manjada (bloqueada).
 *
 * Suporta apenas {@link PasswordValidationRule validação} de senhas.
 */
public class PreviousPasswordRule extends AbstractPasswordRule implements PasswordValidationRule {

    private static final long serialVersionUID = -8217508856913825108L;

    private final PasswordEncoder passwordEncoder;

    public PreviousPasswordRule(PasswordEncoder passwordEncoder) {
        super();
        this.passwordEncoder = passwordEncoder;
        setScoreWeigth(4);
    }

    /**
     * {@inheritDoc }
     *
     * Implementa uma regra de validação que bloqueia o uso do login como senha.
     */
    @Override
    public boolean validate(final String password, final Map<String, Object> extraParams) {
        if (TextUtils.isBlankOrNull(password)) {
            return false;
        }
        final String senha = (String) extraParams.get("senha");
        return TextUtils.isBlankOrNull(senha) || !passwordEncoder.matches(password, senha);
    }
}
