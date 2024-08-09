package com.gabrielflores.myfortune.password;

import com.gabrielflores.myfortune.util.CollectionUtils;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;

/**
 * Um checador de senhas configurável.
 *
 */
@Slf4j
public class ConfigurablePasswordChecker implements PasswordChecker {

    /**
     * Coleção de regras que serão avaliadas pelo checador.
     */
    private final Map<String, PasswordRule> rules;
    private final Map<String, PasswordResult> results;
    private boolean debugMode = false;

    /**
     * Construtor padrão.
     */
    public ConfigurablePasswordChecker() {
        this(10);
    }

    /**
     * Construtor que aloca previamente a capacidade de regras, para agilizar a
     * adição.
     *
     * @param initialCapacity Capacidade inicial de regras.
     */
    public ConfigurablePasswordChecker(final int initialCapacity) {
        this.rules = new LinkedHashMap<>(initialCapacity);
        this.results = new LinkedHashMap<>(initialCapacity + 1);
        this.results.put(GLOBAL_RESULT_KEY, new PasswordResult());
    }

    /**
     * Indica a avaliação das regras deverá sair no log a cada execução.
     *
     * @param debugMode Indicador se a avaliação das regras deverá sair no log a
     * cada execução.
     */
    public void setDebugMode(final boolean debugMode) {
        this.debugMode = debugMode;
    }

    /**
     * Adiciona uma regra ao checador.
     *
     * @param rule A regra a ser adicionada.
     * @return <code>this</code>, para permitir chamadas em cadeia.
     */
    public ConfigurablePasswordChecker addRule(final PasswordRule rule) {
        this.rules.put(rule.getId(), rule);
        this.results.put(rule.getId(), new PasswordResult(rule));
        return this;
    }

    /**
     * Adiciona ao checador todas as regras da coleção dada.
     *
     * @param rules As regras a serem adicionadas.
     */
    public void setRules(final Collection<PasswordRule> rules) {
        this.rules.clear();
        this.results.clear();
        this.results.put(GLOBAL_RESULT_KEY, new PasswordResult());
        rules.forEach(rule -> addRule(rule));
    }

    /**
     * Remove a regra de id dado.
     *
     * @param ruleId O id da regra a ser removida.
     * @return A regra que foi removida, ou null se não hover regra com o id
     * dado.
     */
    public PasswordRule removeRule(final String ruleId) {
        if (GLOBAL_RESULT_KEY.equals(ruleId)) {
            throw new IllegalArgumentException("Global result cannot be removed!");
        }
        final PasswordRule removed = this.rules.remove(ruleId);
        this.results.remove(ruleId);
        return removed;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Map<String, PasswordResult> evaluate(final String password, final Map<String, Object> ruleParams) {
        if (CollectionUtils.isEmpty(this.rules)) {
            throw new IllegalStateException("No rules configured!");
        }
        getGlobalResult().reset();
        boolean isValid = true;
        int totalScore = 0;
        int totalWeigth = 0;
        for (final PasswordRule rule : this.rules.values()) {
            final PasswordResult ruleResult = rule.evaluate(password, ruleParams);
            isValid = isValid && ruleResult.isValid();
            totalScore += (ruleResult.getScore() * ruleResult.getScoreWeigth());
            totalWeigth += ruleResult.getScoreWeigth();
            this.results.put(rule.getId(), ruleResult);
            if (debugMode) {
                log.info("{} : {} -->> {}", password, rule.toString(), ruleResult.toString());
            }
        }
        getGlobalResult().setValid(isValid);
        log.info("totalScore: {} | totalWeigth {}", totalScore, totalWeigth);
        getGlobalResult().setScore(isValid && totalWeigth > 0 ? totalScore / totalWeigth : 0);
        getGlobalResult().setScoreWeigth(1);
        log.info("{} : {}", password, getGlobalResult().toString());
        return getResults();
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public PasswordResult getRuleResult(final String ruleId) {
        return results.get(ruleId);
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public PasswordResult getGlobalResult() {
        return this.results.get(GLOBAL_RESULT_KEY);
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Map<String, PasswordResult> getResults() {
        return Collections.unmodifiableMap(this.results);
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Collection<PasswordRule> getRules() {
        return Collections.unmodifiableCollection(rules.values());
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Collection<PasswordValidationRule> getValidationRules() {
        return this.rules.values().stream()
                .filter(rule -> rule.isValidationSupported() && rule.isValidationActive())
                .map(rule -> (PasswordValidationRule) rule)
                .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Collection<PasswordScoreRule> getScoreRules() {
        return this.rules.values().stream()
                .filter(rule -> rule.isScoreSupported() && rule.isScoreActive())
                .map(rule -> (PasswordScoreRule) rule)
                .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public PasswordChecker reset() {
        this.results.values().forEach(PasswordResult::reset);
        return this;
    }
}
