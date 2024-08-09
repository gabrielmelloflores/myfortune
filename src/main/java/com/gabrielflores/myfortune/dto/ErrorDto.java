package com.gabrielflores.myfortune.dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 *
 * @author Giuliano Ferreira <giuliano@ufsm.br>
 */
@Setter
@Getter
@NoArgsConstructor
@Accessors(chain = true)
public class ErrorDto {

    String message;

    String exception;

    String detail;

    String timestamp;

    public ErrorDto(Exception ex, String detail) {
        this.exception = ex.getClass().getSimpleName();
        this.message = ex.getMessage();
        this.detail = detail;
        this.timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
    }

}
