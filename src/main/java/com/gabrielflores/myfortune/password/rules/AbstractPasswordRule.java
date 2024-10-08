package com.gabrielflores.myfortune.password.rules;

import com.gabrielflores.myfortune.password.PasswordResult;
import com.gabrielflores.myfortune.password.PasswordRule;
import com.gabrielflores.myfortune.password.PasswordScoreRule;
import com.gabrielflores.myfortune.password.PasswordValidationRule;
import com.gabrielflores.myfortune.util.TextBuilder;
import com.gabrielflores.myfortune.util.TextUtils;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import org.springframework.core.io.ClassPathResource;

/**
 * Base para todas as regras.
 *
 */
public abstract class AbstractPasswordRule implements PasswordRule {

    private static final long serialVersionUID = -3675446752729964593L;
    protected Properties messages;
    /**
     * Indica se a validação implementada na regra (se houver) deverá ser
     * chamada pelo checker.
     */
    private boolean validationActive;
    /**
     * Indica se o cálculo de score implementado na regra (se houver) deverá ser
     * chamado pelo checker.
     */
    private boolean scoreActive;
    /**
     * O peso do score dessa regra no score global.
     */
    protected int scoreWeigth;

    /**
     * Construtor padrão.
     *
     * Por default, validação e score estao ativos e o peso do score é 1.
     */
    public AbstractPasswordRule() {
        this.validationActive = true;
        this.scoreActive = true;
        this.scoreWeigth = 1;
        this.messages = new Properties();
        try {
            this.messages.load(new ClassPathResource("/password/PasswordRule.properties").getInputStream());
        } catch (IOException ex) {
            throw new UncheckedIOException("Erro carregando PasswordRule", ex);
        }
    }

    /**
     * {@inheritDoc }
     *
     * Por padrão, usa o nome simples da classe como id.
     */
    @Override
    public String getId() {
        return getClass().getSimpleName();
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public boolean isValidationSupported() {
        return PasswordValidationRule.class.isAssignableFrom(this.getClass());
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public boolean isValidationActive() {
        return validationActive;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public boolean isScoreSupported() {
        return PasswordScoreRule.class.isAssignableFrom(this.getClass());
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public boolean isScoreActive() {
        return scoreActive;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public PasswordResult evaluate(final String password, final Map<String, Object> extraParams) {
        final PasswordResult ruleResult = new PasswordResult(this);
        // efetua a validação se a regra for de validação
        if (isValidationSupported() && isValidationActive()) {
            final PasswordValidationRule tmp = (PasswordValidationRule) this;
            ruleResult.setValid(tmp.validate(password, extraParams));
        } else {
            // nao é regra de validação ou esta configurado para nao validar. assume sempre true
            ruleResult.setValid(true);
        }
        // efetua o calculo do score se a regra for de score
        if (ruleResult.isValid() && isScoreSupported() && isScoreActive()) {
            final PasswordScoreRule tmp = (PasswordScoreRule) this;
            // normaliza o score e passa para o resultado
            int score = tmp.score(password, extraParams);
            score = Math.max(score, 0);
            score = Math.min(score, 100);
            ruleResult.setScore(score);
            // repassa o peso para o resultado
            ruleResult.setScoreWeigth(tmp.getScoreWeigth());
        } else {
            // se invalido ou nao suportar score ou score inativo, zeramos os campos de score
            ruleResult.setScore(0);
            ruleResult.setScoreWeigth(0);
        }
        return ruleResult;
    }

    /**
     * Indica se a validação está ativa.
     *
     * @param validationActive Indicador se a validação implementada na regra
     * (se houver) deverá ser chamada pelo checker.
     */
    public void setValidationActive(final boolean validationActive) {
        this.validationActive = validationActive;
    }

    /**
     * Indica se o cálculo de score está ativo.
     *
     * @param scoreActive Indicador se o cálculo de score implementado na regra
     * (se houver) deverá ser chamado pelo checker.
     */
    public void setScoreActive(final boolean scoreActive) {
        this.scoreActive = scoreActive;
    }

    /**
     * Retorna o peso do score dessa regra no score global.
     *
     * @return O peso do score dessa regra no score global.
     */
    public int getScoreWeigth() {
        return scoreWeigth;
    }

    /**
     * Indica o peso do score dessa regra no score global.
     *
     * @param scoreWeigth O peso do score dessa regra no score global.
     */
    public final void setScoreWeigth(final int scoreWeigth) {
        this.scoreWeigth = scoreWeigth;
    }

    public String getValidationMessage() {
        return this.messages.getProperty(getId() + ".validationMessage");
    }

    public String getValidationError() {
        return this.messages.getProperty(getId() + ".validationError");
    }

    public String getScoreMessage() {
        return this.messages.getProperty(getId() + ".scoreMessage");
    }

    /**
     * Calcula um score para um chain de qquer tipo.
     *
     * Por exemplo: Para obter o score de uma sequÊncia de dígitos para
     * "teste001", chame:
     *
     * calculateChain("teste001", 3); // o 3 se refere a qtdd de dégitos em
     * sequencia neste caso ("001").
     *
     * A senha possui um length de 8 neste caso ("teste001"). mas passamos a
     * pontuar apenas a partir do segundo char de uma chain, logo, o max será
     * (length - 1).
     *
     * Sendo assim, teremos 100/(length - 1) pontos por cada char da chain.
     *
     * No caso de "teste001", temos uma chain de 3 digitos. contando a partir do
     * segundo, 2 deles contarão pontos.
     *
     * Logo, a pontuação será 2 * (100/(length - 1)), que nesse caso será 2 *
     * (100/7) = 2 * 14 = 28.
     *
     * A partir do resultado obtido, dá para calcular regras em que o chain será
     * bonus ou penalidade.
     *
     * Para bonus, use o retorno direto. Para penalidades, use 100 - retorno.
     *
     * @param password O password alvo.
     * @param count O length do chain em questão.
     * @return O valor do score, entre 0 e 100.
     */
    protected int calculateChain(final String password, final int count) {
        int max = TextUtils.length(password) - 1;
        int pass = max > 0 ? 100 / max : 0;
        return count > 0 ? (count - 1) * pass : 0;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + Objects.hashCode(this.getId());
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final AbstractPasswordRule other = (AbstractPasswordRule) obj;
        return Objects.equals(this.getId(), other.getId());
    }

    @Override
    public String toString() {
        final TextBuilder builder = new TextBuilder();
        builder.append(getId()).
                append(" { validationSupported? ").append(isValidationSupported(), () -> "yes", () -> "no").
                append(" | validationActive? ").append(isValidationActive(), () -> "yes", () -> "no").
                append(" | scoreSupported? ").append(isScoreSupported(), () -> "yes", () -> "no").
                append(" | scoreActive? ").append(isScoreActive(), () -> "yes", () -> "no").
                append(" | scoreWeigth: ").append(getScoreWeigth()).
                append(" }");
        return builder.toString();
    }
}
