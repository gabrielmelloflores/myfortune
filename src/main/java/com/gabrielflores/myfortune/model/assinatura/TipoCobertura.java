package com.gabrielflores.myfortune.model.assinatura;

import java.util.Map;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 *
 * @author Douglas Pasqualin <douglas.pasqualin@gmail.com>
 */
@Getter
@RequiredArgsConstructor
public enum TipoCobertura {

    //TODO: ver coberturas
    SMS("Troca de informações via SMS em caso de emergência");

    private final String descricao;

    public Map<String, String> toMap() {
        return Map.of("name", name(), "descricao", getDescricao());
    }

}
