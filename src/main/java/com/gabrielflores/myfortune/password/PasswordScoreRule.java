package com.gabrielflores.myfortune.password;

import java.util.Map;

/**
 * Interface que define uma regra de cálculo de força.
 *
 */
public interface PasswordScoreRule extends PasswordRule {

    /**
     * Retorna uma frase curta que dá uma dica para melhorar a força da senha no
     * quesito de cálculo abordado por esta regra.
     *
     * @return Uma dica para melhorar a força da senha no quesito abordado por
     * esta regra.
     */
    public String getScoreMessage();

    /**
     * Retorna o peso da regra no cálculo do score global.
     *
     * <b>Deve sempre retornar um inteiro maior que 0!</b>
     *
     * @return O peso da regra no score global.
     */
    public int getScoreWeigth();

    /**
     * Calculador de score de força da senha, referente ao quesito abordado por
     * esta regra.
     *
     * <b>Deve sempre retornar um inteiro positivo entre 0 e 100!</b>
     *
     * @param password A senha a ser avaliada.
     * @param extraParams Parâmetros extras opcionais.
     * @return Um valor positivo entre 0 e 100.
     */
    public int score(final String password, final Map<String, Object> extraParams);
}
