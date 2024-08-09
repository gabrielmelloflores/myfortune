package com.gabrielflores.myfortune.controller.assinatura;

import com.gabrielflores.myfortune.annotation.DefaultApiResponses;
import com.gabrielflores.myfortune.annotation.InvalidDataApiResponse;
import com.gabrielflores.myfortune.annotation.NotFoundApiResponse;
import com.gabrielflores.myfortune.controller.BaseController;
import com.gabrielflores.myfortune.dto.assinatura.CoberturaDtoAlteracao;
import com.gabrielflores.myfortune.dto.assinatura.CoberturaDtoCadastro;
import com.gabrielflores.myfortune.exception.InvalidDataException;
import com.gabrielflores.myfortune.model.assinatura.Cobertura;
import com.gabrielflores.myfortune.model.assinatura.Plano;
import com.gabrielflores.myfortune.model.assinatura.TipoCobertura;
import com.gabrielflores.myfortune.model.dto.assinatura.CoberturaDto;
import com.gabrielflores.myfortune.model.dto.assinatura.CoberturaUtilizadaDto;
import com.gabrielflores.myfortune.response.PageResponse;
import com.gabrielflores.myfortune.service.assinatura.CoberturaService;
import com.gabrielflores.myfortune.service.assinatura.CoberturaUtilizadaService;
import com.gabrielflores.myfortune.service.assinatura.PlanoService;
import com.gabrielflores.myfortune.util.AnoMesReferencia;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
@RequestMapping(path = "/plano/cobertura", produces = MediaType.APPLICATION_JSON_VALUE)
@SecurityRequirement(name = "bearer-key")
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Coberturas", description = "Gerenciamento de coberturas dos planos de assinaturas")
public class CoberturaController extends BaseController {

    private final CoberturaService coberturaService;

    private final CoberturaUtilizadaService coberturaUtilizadaService;

    private final PlanoService planoService;

    @Operation(summary = "Salva uma nova cobertura de um plano de assinatura", responses = {
        @ApiResponse(responseCode = "200", description = "Nova cobertura persistida no banco", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CoberturaDto.class)))})
    @InvalidDataApiResponse
    @PostMapping
    public ResponseEntity<CoberturaDto> insertCobertura(@Valid @RequestBody CoberturaDtoCadastro coberturaDtoCadastro) {
        final Plano plano = planoService.getById(coberturaDtoCadastro.getPlano());
        Cobertura cobertura = new Cobertura(coberturaDtoCadastro, plano);
        cobertura = coberturaService.save(cobertura);
        return ResponseEntity.ok(coberturaService.getById(cobertura.getId(), CoberturaDto.class));
    }

    @Operation(summary = "Atualiza uma cobertura de um plano de assinatura", responses = {
        @ApiResponse(responseCode = "200", description = "Cobertura atualizada", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CoberturaDto.class)))
    })
    @NotFoundApiResponse
    @InvalidDataApiResponse
    @PutMapping("{id}")
    public ResponseEntity<CoberturaDto> atualizaCobertura(@PathVariable(required = true) Long id,
            @Valid @RequestBody CoberturaDtoAlteracao coberturaDtoAlteracao) {
        Cobertura cobertura = coberturaService.getById(id);
        if (!coberturaDtoAlteracao.isPeriodoValido()) {
            throw new InvalidDataException("Período inválido");
        }
        cobertura.setDataInicio(coberturaDtoAlteracao.getDataInicio());
        cobertura.setDataFim(coberturaDtoAlteracao.getDataFim());
        cobertura.setQuantidade(coberturaDtoAlteracao.getQuantidade());
        cobertura.setValorExcedente(cobertura.getQuantidade() != null ? coberturaDtoAlteracao.getValorExcedente() : null);
        cobertura = coberturaService.update(cobertura);
        return ResponseEntity.ok(coberturaService.getById(cobertura.getId(), CoberturaDto.class));
    }

    // @Operation(summary = "Lista as coberturas utilizadas por uma coleira", responses = {
    //     @ApiResponse(responseCode = "200", description = "lista as coberturas utilizadas de uma coleira, relativa ao seu plano de assinatura", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CoberturaUtilizadaDto.class)))
    // })
    // @PreAuthorize("isAuthenticated()")
    // @GetMapping("/utilizada/{idColeira}")
    // public ResponseEntity<PageResponse<CoberturaUtilizadaDto>> listaCoberturasUtilizadas(@PathVariable("idColeira") final Long idColeira,
    //         @RequestParam(value = "tipoCobertura", required = true) final TipoCobertura tipoCobertura,
    //         @RequestParam(value = "ano", required = true) @Min(2023) final Integer ano,
    //         @RequestParam(value = "mes", required = true) @Min(1) @Max(12) final Integer mes,
    //         @ParameterObject Pageable pageable) {
    //     final Coleira coleira = coleiraService.getById(idColeira);
    //     validaColeira(coleira);
    //     final Page<CoberturaUtilizadaDto> coberturasUtilizadas = coberturaUtilizadaService.findAllByColeiraAnoMesTipoCobertura(idColeira, new AnoMesReferencia(ano, mes), tipoCobertura, pageable);
    //     return ResponseEntity.ok(new PageResponse<>(coberturasUtilizadas));
    // }
}
