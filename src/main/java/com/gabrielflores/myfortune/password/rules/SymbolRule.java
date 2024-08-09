package com.gabrielflores.myfortune.password.rules;

import com.gabrielflores.myfortune.password.PasswordScoreRule;
import com.gabrielflores.myfortune.password.PasswordValidationRule;
import com.gabrielflores.myfortune.util.TextUtils;
import java.util.Map;

/**
 * Regra que avalia a presença de símbolos na senha.
 *
 * Se houver algum símbolo, melhor.
 *
 * Suporta {@link PasswordValidationRule validação} e
 * {@link PasswordScoreRule score} de senhas.
 *
 */
public class SymbolRule extends AbstractPasswordRule implements PasswordValidationRule, PasswordScoreRule {

    private static final long serialVersionUID = 6048346874016769914L;

    /**
     * {@inheritDoc }
     */
    @Override
    public boolean validate(final String password, final Map<String, Object> extraParams) {
        return TextUtils.hasSymbol(password);
    }

    /**
     * {@inheritDoc }
     *
     * Avalia a presença de símbolos na senha.
     *
     * Se houver algum símbolo, melhor.
     */
    @Override
    public int score(final String password, final Map<String, Object> extraParams) {
        return TextUtils.hasSymbol(password) ? 100 : 0;
    }
}
