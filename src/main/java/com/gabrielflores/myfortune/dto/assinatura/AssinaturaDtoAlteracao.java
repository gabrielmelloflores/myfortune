package com.gabrielflores.myfortune.dto.assinatura;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 *
 * @author Douglas Pasqualin <douglas.pasqualin@gmail.com>
 */
@Setter
@Getter
@Accessors(chain = true)
@Schema(title = "Geração de um novo checkout para uma assinatura", description = "Dados para geração de um novo checkout no provedor de pagamentos")
public class AssinaturaDtoAlteracao {

    @Schema(title = "Url de redirect", description = "Após a confirmação de pagamento no PagSeguro, para onde o User deve ser redirecionado.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    protected String urlRedirect;
}
