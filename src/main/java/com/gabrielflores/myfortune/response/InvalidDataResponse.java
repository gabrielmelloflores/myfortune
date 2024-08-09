package com.gabrielflores.myfortune.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(chain = true)
public class InvalidDataResponse extends ErrorResponse {

    private Map<String, List<String>> errors;

    public InvalidDataResponse(Integer status, String error, String path, LocalDateTime timestamp) {
        super(status, error, path, timestamp);
    }

    public InvalidDataResponse(Integer status, String error, String path) {
        super(status, error, path, LocalDateTime.now());
    }
}
