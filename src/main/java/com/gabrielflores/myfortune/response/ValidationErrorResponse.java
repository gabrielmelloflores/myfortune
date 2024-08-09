package com.gabrielflores.myfortune.response;

import java.time.LocalDateTime;
import java.util.Map;
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
public class ValidationErrorResponse extends ErrorResponse {

    private Map<String, String> errors;

    public ValidationErrorResponse(Integer status, String error, String path, LocalDateTime timestamp) {
        super(status, error, path, timestamp);
    }
}
