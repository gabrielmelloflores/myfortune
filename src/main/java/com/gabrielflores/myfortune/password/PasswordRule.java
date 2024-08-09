package com.gabrielflores.myfortune.password;

import java.io.Serializable;
import java.util.Map;

/**
 * Interface base para todas as regras de senha.
 *
 */
public interface PasswordRule extends Serializable {

    /**
     * Retorna um id único, para identificar o tipo da regra.
     *
     * @return O id único que identifica o tipo da regra.
     */
    public String getId();

    /**
     * Indica se esta regra suporta validação.
     *
     * @return true, se a regra implementa validação. false caso contrário.
     */
    public boolean isValidationSupported();

    /**
     * Indica se esta regra suporta score.
     *
     * @return true, se a regra implementa score. false caso contrário.
     */
    public boolean isScoreSupported();

    /**
     * Indica se a validação está ativa.
     *
     * @return true, se a validação implementada na regra (se houver) deverá ser
     * chamada pelo checker. false, se for para ignorar a validação.
     */
    public boolean isValidationActive();

    /**
     * Indica se o cálculo de score está ativo.
     *
     * @return true, se o cálculo de score implementado na regra (se houver)
     * deverá ser chamado pelo checker. false, se for para ignorar o cálculo.
     */
    public boolean isScoreActive();

    /**
     * Avalia a senha no quesito da regra corrente.
     *
     * @param password A senha a ser avaliada.
     * @param extraParams Parâmetros extras opcionais.
     * @return O resultado da avaliação.
     */
    public PasswordResult evaluate(final String password, final Map<String, Object> extraParams);
}
