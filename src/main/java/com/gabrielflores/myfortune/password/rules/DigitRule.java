package com.gabrielflores.myfortune.password.rules;

import com.gabrielflores.myfortune.password.PasswordScoreRule;
import com.gabrielflores.myfortune.password.PasswordValidationRule;
import com.gabrielflores.myfortune.util.TextUtils;
import java.util.Map;

/**
 * Regra que avalida a presença de dígitos.
 *
 * Se houver algum dígito, melhor.
 *
 * Suporta {@link PasswordValidationRule validação} e
 * {@link PasswordScoreRule score} de senhas.
 */
public class DigitRule extends AbstractPasswordRule implements PasswordValidationRule, PasswordScoreRule {

    private static final long serialVersionUID = -1388944878223910654L;

    /**
     * {@inheritDoc }
     */
    @Override
    public boolean validate(final String password, final Map<String, Object> extraParams) {
        return TextUtils.hasDigit(password);
    }

    /**
     * {@inheritDoc }
     *
     * Avalida a presença de dígitos.
     *
     * Se houver algum dígito, melhor.
     */
    @Override
    public int score(final String password, final Map<String, Object> extraParams) {
        return TextUtils.hasDigit(password) ? 100 : 0;
    }
}
