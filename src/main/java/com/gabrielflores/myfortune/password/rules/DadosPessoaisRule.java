package com.gabrielflores.myfortune.password.rules;

import com.gabrielflores.myfortune.password.PasswordScoreRule;
import com.gabrielflores.myfortune.password.PasswordValidationRule;
import java.util.Map;
import java.util.stream.Stream;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

public class DadosPessoaisRule extends AbstractPasswordRule implements PasswordValidationRule, PasswordScoreRule {

    private static final long serialVersionUID = 2911332076354072856L;

    public DadosPessoaisRule() {
        super();
        setScoreWeigth(5);
    }

    @Override
    public boolean validate(String password, final Map<String, Object> extraParams) {
        if (StringUtils.isBlank(password)) {
            return false;
        }
        return Stream.of(getTokens(extraParams)).noneMatch(parte -> StringUtils.containsIgnoreCase(password, parte));
    }

    @Override
    public int score(String password, final Map<String, Object> extraParams) {
        final int max = StringUtils.length(password);
        int lesser = max;
        for (final String token : getTokens(extraParams)) {
            final String builder = password.replace(token, "");
            if (builder.length() < lesser) {
                lesser = builder.length();
            }
        }
        int pass = 100 / max;
        int value = (max - lesser) * pass;
        return 100 - value;
    }

    private String[] getTokens(final Map<String, Object> extraParams) {
        String[] tokens = new String[]{};
        final String cpf = (String) extraParams.get("cpf");
        if (StringUtils.isNotBlank(cpf)) {
            tokens = ArrayUtils.addAll(tokens, cpf);
            tokens = ArrayUtils.addAll(tokens, cpf.replaceAll("[.-]", ""));
            tokens = ArrayUtils.addAll(tokens, StringUtils.split(cpf, ".-"));
        }
        final String nome = (String) extraParams.get("nome");
        if (StringUtils.isNotBlank(nome)) {
            tokens = ArrayUtils.addAll(tokens, StringUtils.split(nome));
        }
        final String email = (String) extraParams.get("email");
        if (StringUtils.isNotBlank(email)) {
            tokens = ArrayUtils.addAll(tokens, StringUtils.split(email, "@."));
        }
        return tokens;
    }
}
