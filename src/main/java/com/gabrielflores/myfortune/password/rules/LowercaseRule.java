package com.gabrielflores.myfortune.password.rules;

import com.gabrielflores.myfortune.password.PasswordScoreRule;
import com.gabrielflores.myfortune.password.PasswordValidationRule;
import com.gabrielflores.myfortune.util.TextUtils;
import java.util.Map;

/**
 * Regra que avalia a presença de letras minúsculas na senha.
 *
 * Se houver alguma letra minúscula, melhor.
 *
 * Suporta {@link PasswordValidationRule validação} e
 * {@link PasswordScoreRule score} de senhas.
 */
public class LowercaseRule extends AbstractPasswordRule implements PasswordValidationRule, PasswordScoreRule {

    private static final long serialVersionUID = -578875518867655699L;

    /**
     * {@inheritDoc }
     */
    @Override
    public boolean validate(final String password, final Map<String, Object> extraParams) {
        return TextUtils.hasLowercase(password);
    }

    /**
     * {@inheritDoc }
     *
     * Avalia a presença de letras minúsculas na senha.
     *
     * Se houver alguma letra minúscula, melhor.
     */
    @Override
    public int score(final String password, final Map<String, Object> extraParams) {
        return TextUtils.hasLowercase(password) ? 100 : 0;
    }
}
