package com.gabrielflores.myfortune.controller.assinatura;

import com.gabrielflores.myfortune.annotation.DefaultApiResponses;
import com.gabrielflores.myfortune.annotation.InvalidDataApiResponse;
import com.gabrielflores.myfortune.annotation.NotFoundApiResponse;
import com.gabrielflores.myfortune.controller.BaseController;
import com.gabrielflores.myfortune.dto.assinatura.CupomDtoCadastro;
import com.gabrielflores.myfortune.exception.EntityNotFoundException;
import com.gabrielflores.myfortune.exception.InvalidDataException;
import com.gabrielflores.myfortune.model.assinatura.CupomDesconto;
import com.gabrielflores.myfortune.model.dto.assinatura.CupomDescontoDto;
import com.gabrielflores.myfortune.response.MessageResponse;
import com.gabrielflores.myfortune.response.PageResponse;
import com.gabrielflores.myfortune.service.assinatura.CupomDescontoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Douglas Pasqualin <douglas.pasqualin@gmail.com>
 */
@RestController
@RequiredArgsConstructor
@DefaultApiResponses
@RequestMapping(path = "/plano/cupom", produces = MediaType.APPLICATION_JSON_VALUE)
@SecurityRequirement(name = "bearer-key")
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Cupons de desconto", description = "Gerenciamento de cupons de desconto nos planos de assinaturas")
public class CupomDescontoController extends BaseController {

    private final CupomDescontoService cupomDescontoService;

    @Operation(summary = "Lista de cupons de desconto", responses = {
        @ApiResponse(responseCode = "200", description = "lista de cupons de desconto cadastrados", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CupomDescontoDto.class)))
    })
    @GetMapping("/list")
    public ResponseEntity<PageResponse<CupomDescontoDto>> listaCupons(
            @Parameter(description = "Busca pelo código do cupom (like) ignorando case (Opcional)") @RequestParam(required = false) @Size(min = 3, max = 20) String codigo,
            @ParameterObject Pageable pageable) {
        Page<CupomDescontoDto> cupons;
        if (StringUtils.isNotBlank(codigo)) {
            cupons = cupomDescontoService.findAllByCodigo(codigo, pageable);
        } else {
            cupons = cupomDescontoService.findAll(pageable, CupomDescontoDto.class);
        }
        return ResponseEntity.ok(new PageResponse<>(cupons));
    }

    @Operation(summary = "Salva um novo cupom de desconto", responses = {
        @ApiResponse(responseCode = "200", description = "Novo cupom de desconto persistido no banco", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CupomDescontoDto.class)))})
    @InvalidDataApiResponse
    @PostMapping
    public ResponseEntity<CupomDescontoDto> insertCupom(@Valid @RequestBody CupomDtoCadastro cupomDtoCadastro) {
        CupomDesconto cupomDesconto = new CupomDesconto(cupomDtoCadastro);
        cupomDesconto = cupomDescontoService.save(cupomDesconto);
        return ResponseEntity.ok(cupomDescontoService.getById(cupomDesconto.getId(), CupomDescontoDto.class));
    }

    @Operation(summary = "Atualiza um cupom de desconto já cadastrado", responses = {
        @ApiResponse(responseCode = "200", description = "Cupom de desconto atualizado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CupomDescontoDto.class)))
    })
    @NotFoundApiResponse
    @PutMapping("{id}")
    public ResponseEntity<CupomDescontoDto> atualizaCupom(@PathVariable(required = true) Long id,
            @Valid @RequestBody CupomDtoCadastro cupomDtoCadastro) {
        CupomDesconto cupomDesconto = cupomDescontoService.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(CupomDesconto.class, id, "Cupom não encontrado"));
        //TODO: Validar quantidades utilizados X disponíveis? Ex: diminiu a quantidade de disponíveis e ficou menor que os utilizados?
        cupomDesconto.atualizaDados(cupomDtoCadastro);
        cupomDesconto = cupomDescontoService.update(cupomDesconto);
        return ResponseEntity.ok(cupomDescontoService.getById(cupomDesconto.getId(), CupomDescontoDto.class));
    }

    @Operation(summary = "Exclui um cupom de desconto, desde que ainda não tenha sido utilizado", responses = {
        @ApiResponse(responseCode = "200", description = "Cupom de desconto excluído", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class)))
    })
    @NotFoundApiResponse
    @InvalidDataApiResponse
    @DeleteMapping("{id}")
    public ResponseEntity<MessageResponse> excluiCupom(@PathVariable Long id) {
        CupomDesconto cupomDesconto = cupomDescontoService.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(CupomDesconto.class, id, "Cupom não encontrado"));
        if (Optional.ofNullable(cupomDesconto.getUtilizado()).orElse(0) > 0) {
            throw new InvalidDataException("Cupom já foi utilizado, atualize a data de validade ao invés de excluir.");
        }
        cupomDescontoService.delete(cupomDesconto);
        return ResponseEntity.ok(new MessageResponse("Cupom de desconto excluído"));
    }

}
