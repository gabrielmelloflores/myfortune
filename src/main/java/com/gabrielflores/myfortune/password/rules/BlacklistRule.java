package com.gabrielflores.myfortune.password.rules;

import com.gabrielflores.myfortune.password.PasswordScoreRule;
import com.gabrielflores.myfortune.password.PasswordValidationRule;
import com.gabrielflores.myfortune.util.TextUtils;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;
import java.util.regex.Pattern;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

/**
 * Regra que avalia se a senha é manjada (bloqueada).
 *
 * Suporta apenas {@link PasswordValidationRule validção} de senhas.
 *
 */
public class BlacklistRule extends AbstractPasswordRule implements PasswordValidationRule, PasswordScoreRule {

    private static final long serialVersionUID = -2541051644488137322L;
    protected static final String DEFAULT_BLACKLIST = "/password/blacklist.txt";
    /**
     * Uma lista de regexes que definem senhas proibidas.
     */
    private Collection<String> blacklist;

    /**
     * retorna a lista de regexes que definem senhas proibidas.
     *
     * @return A lista de regexes que definem senhas proibidas.
     */
    public Collection<String> getBlacklist() {
        return blacklist;
    }

    /**
     * Construtor padrão.
     *
     * Utiliza a lista padrão de proibições.
     */
    public BlacklistRule() {
        this(readBlacklistResource(DEFAULT_BLACKLIST));
    }

    /**
     * Construtor que indica o recurso contendo a lista.
     *
     * @param blacklistResource O recurso no classpath que contém a lista de
     * regexes que definem senhas proibidas.
     */
    public BlacklistRule(final String blacklistResource) {
        this(readBlacklistResource(blacklistResource));
    }

    /**
     * Construtor com lista pronta.
     *
     * @param blacklist A lista de regexes que definem senhas proibidas.
     */
    public BlacklistRule(final Collection<String> blacklist) {
        super();
        this.blacklist = blacklist;
        setScoreWeigth(6); // aumenta a importancia desse score.
    }

    /**
     * Setter para a lista de regexes que definem senhas proibidas.
     *
     * @param blacklist A lista de regexes que definem senhas proibidas.
     */
    public void setBlacklist(final Collection<String> blacklist) {
        this.blacklist = CollectionUtils.isNotEmpty(blacklist) ? blacklist : null;
    }

    /**
     * Setter que indica o recurso contendo a lista de regexes que definem
     * senhas proibidas.
     *
     * @param blacklistResource O recurso no classpath que contém a lista de
     * regexes que definem senhas proibidas.
     */
    public void setBlacklistResource(final String blacklistResource) {
        setBlacklist(readBlacklistResource(blacklistResource));
    }

    /**
     * {@inheritDoc }
     * <p/>
     * Implementa uma regra de validação que bloqueia o uso de senhas manjadas.
     */
    @Override
    public boolean validate(final String password, final Map<String, Object> extraParams) {
        // a validação é case-insensitive
        return TextUtils.isNotBlank(password) && getBlacklist().stream().noneMatch(regex -> TextUtils.matches(regex, password, Pattern.CASE_INSENSITIVE));
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public int score(final String password, final Map<String, Object> extraParams) {
        int max = TextUtils.length(password);
        int lesser = max;
        for (String pattern : blacklist) {
            String builder = password.replaceAll(pattern, "");
            if (builder.length() < lesser) {
                lesser = builder.length();
            }
        }
        int pass = 100 / max;
        int value = (max - lesser) * pass;
        return 100 - value;
    }

    /**
     * Lê um arquivo de texto no classpath, onde cada linha do arquivo
     * representa uma regex.
     *
     * @param blacklistResource caminho para o recurso.
     * @return A coleção de regexes lida do recurso.
     */
    private static Collection<String> readBlacklistResource(final String blacklistResource) {
        try {
            final Collection<String> blacklist = new LinkedList<>();
            final Resource resource = new ClassPathResource(blacklistResource);
            try (BufferedReader br = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
                String line;
                while ((line = br.readLine()) != null) {
                    if (TextUtils.isNotBlank(line)) {
                        blacklist.add(line);
                    }
                }
            }
            return blacklist;
        } catch (IOException ex) {
            throw new UncheckedIOException(ex); // turn exception unchecked
        }
    }
}
