package com.gabrielflores.myfortune.controller.assinatura;

import com.gabrielflores.myfortune.annotation.DefaultApiResponses;
import com.gabrielflores.myfortune.annotation.InvalidDataApiResponse;
import com.gabrielflores.myfortune.annotation.NotFoundApiResponse;
import com.gabrielflores.myfortune.controller.BaseController;
import com.gabrielflores.myfortune.dto.assinatura.PlanoDtoCadastro;
import com.gabrielflores.myfortune.exception.InvalidDataException;
import com.gabrielflores.myfortune.model.assinatura.Plano;
import com.gabrielflores.myfortune.model.assinatura.VigenciaPlano;
import com.gabrielflores.myfortune.model.dto.assinatura.PlanoDtoList;
import com.gabrielflores.myfortune.response.MessageResponse;
import com.gabrielflores.myfortune.service.assinatura.PlanoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Douglas Pasqualin <douglas.pasqualin@gmail.com>
 */
@RestController
@RequiredArgsConstructor
@DefaultApiResponses
@RequestMapping(path = "/plano", produces = MediaType.APPLICATION_JSON_VALUE)
@SecurityRequirement(name = "bearer-key")
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Planos", description = "Gerenciamento de planos de assinaturas")
public class PlanoController extends BaseController {

    private final PlanoService planoService;

    @Operation(summary = "Salva um novo plano de assinatura", responses = {
        @ApiResponse(responseCode = "200", description = "Novo plano persistido no banco", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PlanoDtoList.class)))})
    @InvalidDataApiResponse
    @PostMapping
    public ResponseEntity<PlanoDtoList> insertPlano(@Valid @RequestBody PlanoDtoCadastro planoDtoCadastro) {
        Plano plano = new Plano(planoDtoCadastro);
        validaPlano(plano);
        plano = planoService.save(plano);
        return ResponseEntity.ok(planoService.getById(plano.getId(), PlanoDtoList.class));
    }

    @Operation(summary = "Atualiza um plano de assinatura já cadastrado", responses = {
        @ApiResponse(responseCode = "200", description = "Plano de assinatura atualizado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PlanoDtoList.class)))
    })
    @NotFoundApiResponse
    @PutMapping("{id}")
    public ResponseEntity<PlanoDtoList> atualizaPlano(@PathVariable(required = true) Long id,
            @Valid @RequestBody PlanoDtoCadastro planoDtoCadastro) {
        Plano plano = planoService.getById(id);
        plano.atualizaDados(planoDtoCadastro);
        validaPlano(plano);
        plano = planoService.update(plano);
        return ResponseEntity.ok(planoService.getById(plano.getId(), PlanoDtoList.class));
    }

    @Operation(summary = "Inativa um plano de assinatura", responses = {
        @ApiResponse(responseCode = "200", description = "Plano de assinatura inativado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class)))
    })
    @NotFoundApiResponse
    @DeleteMapping("{id}")
    public ResponseEntity<MessageResponse> inativaPlano(@PathVariable(required = true) Long id) {
        Plano plano = planoService.getById(id);
        if (!plano.getAtivo()) {
            return ResponseEntity.ok(new MessageResponse("Plano já está inativo"));
        }
        planoService.inativa(plano);
        return ResponseEntity.ok(new MessageResponse("Plano inativado com sucesso"));
    }

    @Operation(summary = "Reativa um plano de assinatura", responses = {
        @ApiResponse(responseCode = "200", description = "Plano de assinatura reativado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class)))
    })
    @NotFoundApiResponse
    @PatchMapping("{id}")
    public ResponseEntity<MessageResponse> reativaPlano(@PathVariable(required = true) Long id) {
        Plano plano = planoService.getById(id);
        if (plano.getAtivo()) {
            return ResponseEntity.ok(new MessageResponse("Plano já está ativo"));
        }
        validaTrial(plano);
        planoService.reativa(plano);
        return ResponseEntity.ok(new MessageResponse("Plano reativado com sucesso"));
    }

    private void validaPlano(final Plano plano) {
        if (VigenciaPlano.DIAS.equals(plano.getVigencia())) {
            if (plano.getDias() == null || plano.getDias() <= 0) {
                throw new InvalidDataException("Plano com vigência em DIAS, deve ser informado dias maior que zero");
            }
        } else {
            plano.setDias(null);
        }
        validaTrial(plano);
    }

    private void validaTrial(final Plano plano) {
        if (Boolean.TRUE.equals(plano.getTrial())) {
            final Plano planoTrial = planoService.getPlanoTrial();
            if (Optional.ofNullable(planoTrial)
                    .filter(p -> !p.getId().equals(plano.getId()))
                    .isPresent()) {
                throw new InvalidDataException("Só pode existir 1 plano trial ativo");
            }
        }
    }
}
