package com.gabrielflores.myfortune.annotation;

import com.gabrielflores.myfortune.response.ValidationErrorResponse;
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
    @ApiResponse(responseCode = "404", description = "Entidade não encontrada", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ValidationErrorResponse.class)))
})
public @interface NotFoundApiResponse {

}
