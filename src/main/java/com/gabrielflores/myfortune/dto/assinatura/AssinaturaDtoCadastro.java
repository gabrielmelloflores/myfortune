package com.gabrielflores.myfortune.dto.assinatura;

import com.gabrielflores.myfortune.validation.Exists;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.util.List;
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
@Schema(title = "Criação de assinatura/compra de planos", description = "Dados para criação ou compra de uma nova assinatura/plano")
@Exists(property = "plano", repository = "planoRepository", message = "Plano de venda não cadastrado!")
public class AssinaturaDtoCadastro extends AssinaturaDtoAlteracao {

    @NotNull
    @Schema(title = "Plano", description = "Plano de assinatura escolhido.", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long plano;

    @Schema(title = "Cupom", description = "Código do cupom utilizado na assinatura.", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String codigoCupom;

}
