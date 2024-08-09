package com.gabrielflores.myfortune.password;

import java.util.Map;

/**
 * Interface que define uma regra de validação.
 *
 */
public interface PasswordValidationRule extends PasswordRule {

    /**
     * Retorna uma frase curta que representa o quesito de validação abordado
     * por esta regra.
     *
     * @return Uma frase representando o quesito de validação abordado por esta
     * regra.
     */
    public String getValidationMessage();

    /**
     * Retorna uma frase de erro da validação da regra.
     *
     * @return Uma frase de erro da validação da regra.
     */
    public String getValidationError();

    /**
     * Valida a senha, no quesito abordado por esta regra.
     *
     * @param password A senha a ser avaliada.
     * @param extraParams Parâmetros extras opcionais.
     * @return true, se a senha for válida. false, caso contrário.
     */
    public boolean validate(final String password, final Map<String, Object> extraParams);
}
