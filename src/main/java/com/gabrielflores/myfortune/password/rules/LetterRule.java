package com.gabrielflores.myfortune.password.rules;

import com.gabrielflores.myfortune.password.PasswordScoreRule;
import com.gabrielflores.myfortune.password.PasswordValidationRule;
import com.gabrielflores.myfortune.util.TextUtils;
import java.util.Map;

/**
 * Regra que avalia a presença de letras na senha.
 *
 * Se houver alguma letra, melhor.
 *
 * Suporta {@link PasswordValidationRule validação} e
 * {@link PasswordScoreRule score} de senhas.
 *
 */
public class LetterRule extends AbstractPasswordRule implements PasswordValidationRule, PasswordScoreRule {

    private static final long serialVersionUID = 1503980381703305344L;

    /**
     * {@inheritDoc }
     */
    @Override
    public boolean validate(final String password, final Map<String, Object> extraParams) {
        return TextUtils.hasLetter(password);
    }

    /**
     * {@inheritDoc }
     *
     * Avalia a presença de letras na senha.
     *
     * Se houver alguma letra, melhor.
     */
    @Override
    public int score(final String password, final Map<String, Object> extraParams) {
        return TextUtils.hasLetter(password) ? 100 : 0;
    }
}
