package com.gabrielflores.myfortune.dto;

import java.io.InputStream;
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
public class InputStreamDto {

    private InputStream content;

    private Long length;

    private String name;

    private String type;

}
