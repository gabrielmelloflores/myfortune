package com.gabrielflores.myfortune.password.rules;

import com.gabrielflores.myfortune.password.PasswordScoreRule;
import com.gabrielflores.myfortune.password.PasswordValidationRule;
import com.gabrielflores.myfortune.util.TextUtils;
import java.util.Map;

/**
 * Regra que avalia a presença de letras maiúsculas na senha.
 *
 * Se houver alguma letra maiúscula, melhor.
 *
 * Suporta {@link PasswordValidationRule validação} e
 * {@link PasswordScoreRule score} de senhas.
 */
public class UppercaseRule extends AbstractPasswordRule implements PasswordValidationRule, PasswordScoreRule {

    private static final long serialVersionUID = -4013716147263077762L;

    /**
     * {@inheritDoc }
     */
    @Override
    public boolean validate(final String password, final Map<String, Object> extraParams) {
        return TextUtils.hasUppercase(password);
    }

    /**
     * {@inheritDoc }
     *
     * Avalia a presençaa de letras maiúsculas na senha.
     *
     * Se houver alguma letra maiúscula, melhor.
     */
    @Override
    public int score(final String password, final Map<String, Object> extraParams) {
        return TextUtils.hasUppercase(password) ? 100 : 0;
    }
}
