package com.gabrielflores.myfortune.response;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Giuliano Ferreira <giuliano@ufsm.br>
 */
@Setter
@Getter
@AllArgsConstructor
public class ErrorResponse {

    private Integer status;
    private String error;
    private String path;
    private LocalDateTime timestamp;
}
