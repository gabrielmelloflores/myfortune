package com.gabrielflores.myfortune.annotation;

import com.gabrielflores.myfortune.response.InvalidDataResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * @author Giuliano Ferreira <giuliano@ufsm.br>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
@ApiResponses({
    @ApiResponse(responseCode = "422", description = "Dados inválidos", content = @Content(mediaType = "application/json", schema = @Schema(implementation = InvalidDataResponse.class)))
})
public @interface InvalidDataApiResponse {

}
