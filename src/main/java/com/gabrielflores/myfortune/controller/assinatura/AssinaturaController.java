package com.gabrielflores.myfortune.controller.assinatura;

import com.gabrielflores.myfortune.annotation.DefaultApiResponses;
import com.gabrielflores.myfortune.annotation.InvalidDataApiResponse;
import com.gabrielflores.myfortune.annotation.NotFoundApiResponse;
import com.gabrielflores.myfortune.controller.BaseController;
import com.gabrielflores.myfortune.dto.assinatura.AssinaturaDtoAlteracao;
import com.gabrielflores.myfortune.dto.assinatura.AssinaturaDtoCadastro;
import com.gabrielflores.myfortune.dto.pagseguro.PedidoPagSeguro;
import com.gabrielflores.myfortune.dto.pagseguro.pagamento.CheckoutPagSeguro;
import com.gabrielflores.myfortune.exception.EntityNotFoundException;
import com.gabrielflores.myfortune.model.assinatura.Assinatura;
import com.gabrielflores.myfortune.model.dto.assinatura.AssinaturaDto;
import com.gabrielflores.myfortune.model.dto.assinatura.CupomDescontoDtoBasic;
import com.gabrielflores.myfortune.model.dto.assinatura.PlanoDto;
import com.gabrielflores.myfortune.model.dto.assinatura.PlanoDtoList;
import com.gabrielflores.myfortune.model.user.Usuario;
import com.gabrielflores.myfortune.response.PageResponse;
import com.gabrielflores.myfortune.service.assinatura.AssinaturaService;
import com.gabrielflores.myfortune.service.assinatura.CupomDescontoService;
import com.gabrielflores.myfortune.service.assinatura.PlanoService;
import com.gabrielflores.myfortune.service.user.UsuarioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
@RequestMapping(path = "/assinatura", produces = MediaType.APPLICATION_JSON_VALUE)
@SecurityRequirement(name = "bearer-key")
@PreAuthorize("isAuthenticated()")
@Tag(name = "Assinatura", description = "Gerenciamento das assinaturas para as coleiras")
public class AssinaturaController extends BaseController {

    private final AssinaturaService assinaturaService;

    private final PlanoService planoService;

    private final CupomDescontoService cupomDescontoService;

    private final UsuarioService usuarioService;

    @Operation(summary = "Salva uma nova assinatura/compra de um plano", responses = {
        @ApiResponse(responseCode = "200", description = "Nova assinatura gerada e link para checkout disponível", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AssinaturaDto.class)))})
    @InvalidDataApiResponse
    @PostMapping
    public ResponseEntity<AssinaturaDto> criaAssinatura(@Valid @RequestBody AssinaturaDtoCadastro assinaturaDtoCadastro) {
        final Usuario user = getUserLogado();
        final Assinatura assinatura = assinaturaService.criaAssinatura(user, assinaturaDtoCadastro);
        return ResponseEntity.ok(assinaturaService.getById(assinatura.getId(), AssinaturaDto.class));
    } 
    // public ResponseEntity<AssinaturaDto> criaAssinatura(@Valid @RequestBody AssinaturaDtoCadastro assinaturaDtoCadastro) {
    //     final Usuario User = getUserLogado();
    //     //vamos enviar a coleção de Coleiras já validadas por aqui, para evitar de injetar ColeiraService em AssinaturaService
    //     final Usuarios User = new ArrayList<>(assinaturaDtoCadastro.getColeiras().size());
    //     for (Long idColeira : assinaturaDtoCadastro.getColeiras()) {
    //         Coleira coleira = coleiraService.getById(idColeira);
    //         validaColeira(coleira);
    //         coleiras.add(coleira);
    //     }
    //     assinaturaDtoCadastro.setColeirasObj(coleiras);
    //     final Assinatura assinatura = assinaturaService.criaAssinatura(User, assinaturaDtoCadastro);
    //     return ResponseEntity.ok(assinaturaService.getById(assinatura.getId(), AssinaturaDto.class));
    // }

    @Operation(summary = "Gera um novo checkout para assinaturas não autorizadas e expiradas.", responses = {
        @ApiResponse(responseCode = "200", description = "Assinatura atualizada com novo link de checkout", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AssinaturaDto.class)))})
    @InvalidDataApiResponse
    @NotFoundApiResponse
    @PatchMapping("/checkout/{idAssinatura}")
    public ResponseEntity<AssinaturaDto> criaNovoCheckout(@PathVariable Long id, @Valid @RequestBody AssinaturaDtoAlteracao assinaturaDtoAlteracao) {
        Assinatura assinatura = assinaturaService.getById(id);
        validaAssinatura(assinatura);
        assinatura = assinaturaService.geraNovoCheckout(assinatura, assinaturaDtoAlteracao.getUrlRedirect());
        return ResponseEntity.ok(assinaturaService.getById(assinatura.getId(), AssinaturaDto.class));
    }

    // @Operation(summary = "Lista as assinaturas Trial que uma determinada coleira já usufriu", responses = {
    //     @ApiResponse(responseCode = "200", description = "lista de assinaturas trial de uma determinada coleira", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AssinaturaDto.class)))
    // })
    // @PreAuthorize("permitAll")
    // @GetMapping("/trial/{idColeira}")
    // public ResponseEntity<List<AssinaturaDto>> listaTrials(@PathVariable("idColeira") final Long idColeira) {
    //     final List<AssinaturaDto> assinaturasTrial = assinaturaService.findTrialByColeira(idColeira, AssinaturaDto.class);
    //     return ResponseEntity.ok(assinaturasTrial);
    // }

    @Operation(summary = "Lista de planos de assinatura", responses = {
        @ApiResponse(responseCode = "200", description = "lista os plano de assinaturas cadastrados", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PlanoDtoList.class)))
    })
    @PreAuthorize("permitAll")
    @GetMapping("/plano/list")
    public ResponseEntity<PageResponse<PlanoDtoList>> listaPlanos(
            @Parameter(description = "Indica se busca somente os planos ativos (Opcional, padrão 'Sim')") @RequestParam(required = false, defaultValue = "true") Boolean somenteAtivos,
            @ParameterObject Pageable pageable) {
        Page<PlanoDtoList> planos;
        if (somenteAtivos) {
            planos = planoService.findAllAtivos(pageable);
        } else {
            planos = planoService.findAll(pageable, PlanoDtoList.class);
        }
        return ResponseEntity.ok(new PageResponse<>(planos));
    }

    @Operation(summary = "Lista planos de assinatura Trial", responses = {
        @ApiResponse(responseCode = "200", description = "lista os plano de assinaturas trial cadastrados", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PlanoDtoList.class)))
    })
    @PreAuthorize("permitAll")
    @GetMapping("/plano/trial/list")
    public ResponseEntity<PageResponse<PlanoDtoList>> listaPlanosTrial(
            @Parameter(description = "Indica se busca somente o plano trial ativo (Opcional, padrão 'Sim')") @RequestParam(required = false, defaultValue = "true") Boolean somenteAtivos,
            @ParameterObject Pageable pageable) {
        Page<PlanoDtoList> planos;
        if (somenteAtivos) {
            final PlanoDtoList planoTrial = planoService.findPlanoTrial(PlanoDtoList.class);
            planos = planoTrial != null ? new PageImpl<>(List.of(planoTrial)) : Page.empty();
        } else {
            planos = planoService.findAllTrials(pageable);
        }
        return ResponseEntity.ok(new PageResponse<>(planos));
    }

    @Operation(summary = "Lista detalhes de um plano de assinatura", responses = {
        @ApiResponse(responseCode = "200", description = "Detalhes do plano de assinatura", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PlanoDto.class)))
    })
    @NotFoundApiResponse
    @PreAuthorize("permitAll")
    @GetMapping("/plano/{id}")
    public ResponseEntity<PlanoDto> findPlano(@PathVariable(required = true) Long id) {
        final PlanoDto plano = planoService.getById(id, PlanoDto.class);
        return ResponseEntity.ok(plano);
    }

    @Operation(summary = "Localiza um cupom de desconto pelo código", responses = {
        @ApiResponse(responseCode = "200", description = "Dados básicos do cupom de desconto", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CupomDescontoDtoBasic.class)))
    })
    @NotFoundApiResponse
    @GetMapping("/cupom/{codigo}")
    public ResponseEntity<CupomDescontoDtoBasic> getCupomDesconto(
            @PathVariable(required = true) String codigo) {
        final Optional<CupomDescontoDtoBasic> cupom = cupomDescontoService.findByCodigo(codigo, CupomDescontoDtoBasic.class);
        if (cupom.isEmpty()) {
            throw new EntityNotFoundException("Cupom não encontrado com o código " + codigo);
        }
        return ResponseEntity.ok(cupom.get());
    }

    @Operation(summary = "Consulta um checkout no PagSeguro", responses = {
        @ApiResponse(responseCode = "200", description = "Dados do checkout", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CheckoutPagSeguro.class)))
    })
    @GetMapping("/checkout/{idCheckout}")
    public ResponseEntity<CheckoutPagSeguro> getCheckout(@PathVariable(required = true) @Size(min = 41, max = 41) String idCheckout) {
        final Assinatura assinatura = assinaturaService.getByIdCheckout(idCheckout, true);
        final Usuario usuarioLogado = getUserLogado();
        if (!assinatura.getUser().equals(usuarioLogado) && !usuarioLogado.isAdm()) {
            throw new AccessDeniedException("Acesso inválido");
        }
        final CheckoutPagSeguro checkout = assinaturaService.getCheckout(idCheckout);
        return ResponseEntity.ok(checkout);
    }

    @Operation(summary = "Consulta um pedido gerado a partir de um checkout no PagSeguro", responses = {
        @ApiResponse(responseCode = "200", description = "Dados do pedido", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PedidoPagSeguro.class)))
    })
    @GetMapping("/checkout/pedido/{idCheckout}")
    public ResponseEntity<PedidoPagSeguro> getPedidoCheckout(@PathVariable(required = true) @Size(min = 41, max = 41) String idCheckout) {
        final Assinatura assinatura = assinaturaService.getByIdCheckout(idCheckout, true);
        final Usuario usuarioLogado = getUserLogado();
        if (!assinatura.getUser().equals(usuarioLogado) && !usuarioLogado.isAdm()) {
            throw new AccessDeniedException("Acesso inválido");
        }
        final PedidoPagSeguro pedido = assinaturaService.getPedido(idCheckout);
        if (pedido == null) {
            throw new EntityNotFoundException("Checkout ainda não gerou pedido/cobrança");
        }
        return ResponseEntity.ok(pedido);
    }
}
