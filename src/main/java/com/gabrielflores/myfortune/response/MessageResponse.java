package com.gabrielflores.myfortune.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 *
 * @author Giuliano Ferreira <giuliano@ufsm.br>
 */
@Setter
@Getter
@Accessors(chain = true)
@AllArgsConstructor
public class MessageResponse {

    private String message;
}
