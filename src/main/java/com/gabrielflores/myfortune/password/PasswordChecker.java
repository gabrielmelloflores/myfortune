package com.gabrielflores.myfortune.password;

import java.util.Collection;
import java.util.Map;

/**
 * Interface que define o comportamento de um PasswordChecker.
 *
 */
public interface PasswordChecker {

    public static final String GLOBAL_RESULT_KEY = "GlobalResult";

    /**
     * Avalia a senha (validação e calculo da força) em todos os quesitos
     * configurados.
     *
     * @param password A senha a ser avaliada.
     * @param ruleParams Parâmetros extras opcionais.
     * @return O resultado da avaliação.
     */
    public Map<String, PasswordResult> evaluate(final String password, final Map<String, Object> ruleParams);

    /**
     * Retorna o resultado parcial da última avaliação para a regra de id dado.
     *
     * <b>Se ainda não foi feita nenhuma avaliação, o resultado deverá ser
     * inválido e score = 0</b>
     *
     * @param ruleId O id da regra.
     * @return O resultado parcial da última avaliação.
     */
    public PasswordResult getRuleResult(final String ruleId);

    /**
     * Retorna o resultado global da última avaliação efetuada.
     *
     * <b>Se ainda não foi feita nenhuma avaliação, o resultado deverá ser
     * inválido e score = 0</b>
     *
     * @return O resultado global da última avaliação.
     */
    public PasswordResult getGlobalResult();

    /**
     * Retorna os resultados da última avaliação de todas as regras deste
     * checker. Também retorna o resultado global sob a chave
     * {@link #GLOBAL_RESULT_KEY}.
     *
     * <b>Se ainda não foi feita nenhuma avaliação, todos os resultados deverão
     * ser inválidos e score = 0</b>
     *
     * @return O resultado da última avaliação.
     */
    public Map<String, PasswordResult> getResults();

    /**
     * Retorna a coleção completa de regras gerenciadas por este
     * PasswordChecker.
     *
     * @return A coleção de regras gerenciadas por este PasswordChecker.
     */
    public Collection<PasswordRule> getRules();

    /**
     * Retorna a coleção de {@link PasswordValidationRule regras de validação}
     * gerenciadas por este PasswordChecker que estão <b>ativas</b>.
     *
     * @return A coleção de {@link PasswordValidationRule regras de validação}
     * gerenciadas por este PasswordChecker que estão <b>ativas</b>.
     */
    public Collection<PasswordValidationRule> getValidationRules();

    /**
     * Retorna a coleção de {@link PasswordScoreRule regras de score}
     * gerenciadas por este PasswordChecker que estão <b>ativas</b>.
     *
     * @return A coleção de {@link PasswordScoreRule regras de score}
     * gerenciadas por este PasswordChecker que estão <b>ativas</b>.
     */
    public Collection<PasswordScoreRule> getScoreRules();

    /**
     * Reseta todos os resultados, parciais e global, passando-os para inválido
     * e score=0.
     *
     * @return <code>this</code>, para permitir chamadas em cadeia.
     */
    public PasswordChecker reset();
}
