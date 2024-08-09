package com.gabrielflores.myfortune.service.assinatura;

import com.gabrielflores.myfortune.dto.assinatura.AssinaturaDtoCadastro;
import com.gabrielflores.myfortune.dto.pagseguro.PedidoPagSeguro;
import com.gabrielflores.myfortune.dto.pagseguro.pagamento.CheckoutPagSeguro;
import com.gabrielflores.myfortune.email.TipoEmail;
import com.gabrielflores.myfortune.exception.EntityNotFoundException;
import com.gabrielflores.myfortune.exception.InvalidDataException;
import com.gabrielflores.myfortune.listeners.event.AssinaturaEvent;
import com.gabrielflores.myfortune.model.assinatura.Assinatura;
import com.gabrielflores.myfortune.model.assinatura.UsuarioAssinatura;
import com.gabrielflores.myfortune.model.assinatura.CupomDesconto;
import com.gabrielflores.myfortune.model.assinatura.Plano;
import com.gabrielflores.myfortune.model.assinatura.Situacao;
import static com.gabrielflores.myfortune.model.assinatura.Situacao.CANCELADO;
import static com.gabrielflores.myfortune.model.assinatura.Situacao.NAO_AUTORIZADO;
import static com.gabrielflores.myfortune.model.assinatura.Situacao.QUITADO;
import com.gabrielflores.myfortune.model.assinatura.TipoCobertura;
import com.gabrielflores.myfortune.model.dto.assinatura.AssinaturaDto;
import com.gabrielflores.myfortune.model.user.Usuario;
import com.gabrielflores.myfortune.repository.assinatura.AssinaturaRepository;
import com.gabrielflores.myfortune.service.BaseService;
import com.gabrielflores.myfortune.service.pagseguro.PagSeguroService;
import com.gabrielflores.myfortune.util.CollectionUtils;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

/**
 *
 * @author Douglas Pasqualin <douglas.pasqualin@gmail.com>
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AssinaturaService extends BaseService<Assinatura, AssinaturaRepository> {

    @Value("${gateway.pagamentos.pagseguro.webhook:defaultWebhookValue}")
    private final String webhook;

    @Value("${gateway.pagamentos.pagseguro.webhook.checkout:}")
    private final String webhookCheckout;

    private final PlanoService planoService;

    private final CupomDescontoService cupomDescontoService;

    private final PagSeguroService pagSeguroService;

    private final ApplicationEventPublisher eventPublisher;

    public Assinatura getByIdCheckout(final String idCheckout, final boolean throwIfNotFind) {
        final Assinatura assinatura = repository.findByIdCheckout(idCheckout).orElse(null);
        if (assinatura == null && throwIfNotFind) {
            throw new EntityNotFoundException("Assinatura não encontrada a partir do checkout");
        }
        return assinatura;
    }

    public Assinatura getByIdCheckout(final String idCheckout) {
        return getByIdCheckout(idCheckout, false);
    }

    public Assinatura getByIdTransacao(final String idTransacao, final boolean throwIfNotFind) {
        final Assinatura assinatura = repository.findByIdTransacao(idTransacao).orElse(null);
        if (assinatura == null && throwIfNotFind) {
            throw new EntityNotFoundException("Assinatura não encontrada a partir da transação do PagSeguro");
        }
        return assinatura;
    }

    public Assinatura getByIdTransacao(final String idTransacao) {
        return getByIdTransacao(idTransacao, false);
    }

    public List<Assinatura> findVigenteByUser(final Long idUser) {
        return repository.findVigenteByUser(idUser, Assinatura.class);
    }

    public List<AssinaturaDto> listVigenteByUser(final Long idUser) {
        return repository.findVigenteByUser(idUser, AssinaturaDto.class);
    }

    // public List<Assinatura> findVigenteByColeira(final Long idColeira) {
    //     return repository.findVigenteByColeira(idColeira, Assinatura.class);
    // }

    // public List<Assinatura> findVigenteByColeiraCobertura(final Long idColeira, final TipoCobertura tipoCobertura) {
    //     return repository.findVigenteByColeiraCobertura(idColeira, tipoCobertura, Assinatura.class);
    // }

    // public List<AssinaturaDto> listVigenteByColeira(final Long idColeira) {
    //     return repository.findVigenteByColeira(idColeira, AssinaturaDto.class);
    // }

    // public List<Assinatura> findVigenteByUserColeira(final Long idUser, final Long idColeira) {
    //     return repository.findByUserSituacaoColeira(idUser, Situacao.QUITADO, idColeira, Assinatura.class);
    // }

    // public List<AssinaturaDto> listVigenteByUserColeira(final Long idUser, final Long idColeira) {
    //     return repository.findByUserSituacaoColeira(idUser, Situacao.QUITADO, idColeira, AssinaturaDto.class);
    // }

    public Boolean hasVigenteByUser(final Long idUser) {
        final List<Assinatura> assinaturas = findVigenteByUser(idUser);
        return !CollectionUtils.isEmptyOrNull(assinaturas);
    }

    // public Boolean hasVigenteByUserColeira(final Long idUser, final Long idColeira) {
    //     final List<Assinatura> assinaturas = findVigenteByUserColeira(idUser, idColeira);
    //     return !CollectionUtils.isEmptyOrNull(assinaturas);
    // }

    public List<Assinatura> findPendentesPagamento(final Integer limiteHoras) {
        return repository.findPendentesPagamento(limiteHoras);
    }

    public List<Assinatura> findNaoPagosByDataReferencia(final LocalDate dataReferencia) {
        return repository.findNaoPagosByDataReferencia(dataReferencia);
    }

    // public List<Assinatura> findNaoRenovadasByVencimento(final List<LocalDate> datasVencimento) {
    //     return repository.findNaoRenovadasByVencimento(datasVencimento);
    // }

    // public List<Assinatura> findNaoRenovadasByVencimento(final LocalDate dataVencimento) {
    //     return findNaoRenovadasByVencimento(List.of(dataVencimento));
    // }

    // public <T> List<T> findTrialByColeira(final Long idColeira, Class<T> clazz) {
    //     return repository.findTrialByColeira(idColeira, clazz);
    // }

    @Transactional
    public Assinatura criaAssinatura(final Usuario tutor, final AssinaturaDtoCadastro assinaturaDtoCadastro) {
        final Plano planoVenda = planoService.getById(assinaturaDtoCadastro.getPlano());
        validaPlano(planoVenda);

        CupomDesconto cupom = null;
        if (StringUtils.isNotEmpty(assinaturaDtoCadastro.getCodigoCupom())) {
            cupom = cupomDescontoService.getByCodigo(assinaturaDtoCadastro.getCodigoCupom());
            validaCupom(cupom, planoVenda.getValor());
            cupomDescontoService.utilizaCupom(cupom);
        }

        Assinatura assinatura = new Assinatura(tutor, planoVenda, cupom);
        final CheckoutPagSeguro checkoutPagSeguro = geraCheckout(assinatura, assinaturaDtoCadastro.getUrlRedirect());
        assinatura.setIdCheckout(checkoutPagSeguro.getId())
                .setUrlCheckout(checkoutPagSeguro.getLinkCheckout())
                .setCheckoutValido(checkoutPagSeguro.getLinkCheckout() != null);
        assinatura = save(assinatura);
        eventPublisher.publishEvent(new AssinaturaEvent(assinatura, TipoEmail.ASSINATURA_PEDIDO_RECEBIDO));
        return assinatura;
    }

    // public Assinatura criaAssinatura(final Usuario User, final AssinaturaDtoCadastro assinaturaDtoCadastro) {
    //     //Futuramente podem haver planos de venda que permitam mais de 1 coleira. Por enquanto é 1x1
    //     // if (assinaturaDtoCadastro.getColeiras().size() > 1) {
    //     //     throw new InvalidDataException("Planos atuais só permitem 1 coleira por assinatura");
    //     // }
    //     final Plano planoVenda = planoService.getById(assinaturaDtoCadastro.getPlano());
    //     validaPlano(planoVenda);
    //     //Ver se já existe algum checkout pendente de pagamento.
    //     // for (Long idColeira : assinaturaDtoCadastro.getColeiras()) {
    //     //     final List<Assinatura> pendentes = repository.findByUserSituacaoColeira(User.getId(), Situacao.PENDENTE, idColeira, Assinatura.class);
    //     //     if (!CollectionUtils.isEmptyOrNull(pendentes)) {
    //     //         throw new InvalidDataException("Já existe uma assinatura pendente de pagamento para essa coleira. Utilize o link de pagamento gerado anteriormente");
    //     //     }
    //     // }
    //     CupomDesconto cupom = null;
    //     //Validações de cupom de desconto
    //     if (StringUtils.isNotEmpty(assinaturaDtoCadastro.getCodigoCupom())) {
    //         cupom = cupomDescontoService.getByCodigo(assinaturaDtoCadastro.getCodigoCupom());
    //         validaCupom(cupom, planoVenda.getValor());
    //         cupomDescontoService.utilizaCupom(cupom);
    //     }

    //     //Criar a assinatura
    //     Assinatura assinatura = new Assinatura(User, planoVenda, cupom);
    //     //Coleiras pertencentes a assinatura
    //     // for (Coleira coleira : assinaturaDtoCadastro.getColeirasObj()) {
    //     //     assinatura.addColeira(new UsuarioAssinatura(assinatura, coleira));
    //     // }
    //     //Checkout no pagSeguro para o User efetuar o pagamento
    //     final CheckoutPagSeguro checkoutPagSeguro = geraCheckout(assinatura, assinaturaDtoCadastro.getUrlRedirect());
    //     assinatura.setIdCheckout(checkoutPagSeguro.getId())
    //             .setUrlCheckout(checkoutPagSeguro.getLinkCheckout())
    //             .setCheckoutValido(checkoutPagSeguro.getLinkCheckout() != null);
    //     assinatura = save(assinatura);
    //     eventPublisher.publishEvent(new AssinaturaEvent(assinatura, TipoEmail.ASSINATURA_PEDIDO_RECEBIDO));
    //     return assinatura;
    // }

    @Transactional
    public Assinatura geraNovoCheckout(final Assinatura assinatura, final String urlRedirect) {
        final Plano planoVenda = planoService.getById(assinatura.getPlano().getId());
        if (!planoVenda.getAtivo() || !assinatura.getValor().equals(planoVenda.getValor())) {
            throw new InvalidDataException("O plano de venda da assinatura não é mais válido. Escolha outro plano de assinatura criando uma nova assinatura");
        }
        if (!assinatura.getSituacao().permiteNovoCheckout() || assinatura.isCancelada()) {
            throw new InvalidDataException("Situação da assinatura não permite gerar novo checkout. Crie uma nova assinatura");
        }
        assinatura.atualizaDadosParaNovoCheckout();
        //Gera novo checkout no PagSeguro
        final CheckoutPagSeguro checkoutPagSeguro = geraCheckout(assinatura, urlRedirect);
        assinatura.setIdCheckout(checkoutPagSeguro.getId())
                .setUrlCheckout(checkoutPagSeguro.getLinkCheckout())
                .setCheckoutValido(checkoutPagSeguro.getLinkCheckout() != null);
        final Assinatura assinaturaUpdate = super.update(assinatura);
        eventPublisher.publishEvent(new AssinaturaEvent(assinaturaUpdate, TipoEmail.ASSINATURA_PEDIDO_RECEBIDO));
        return assinaturaUpdate;
    }

    @Transactional
    public Assinatura geraAssinaturaTrial(final Usuario User) {
        final Plano planoTrial = planoService.getPlanoTrial();
        if (planoTrial != null) {
            // final List<Assinatura> trials = findTrialByColeira(coleira.getId(), Assinatura.class);
            // if (!CollectionUtils.isEmptyOrNull(trials)) {
            //     log.warn("Não foi possível criar um plano trial para essa coleira: coleira já usufruiu de um plano trial anteriormente.");
            //     return null;
            // }
            Assinatura assinatura = new Assinatura(User, planoTrial, null);
            assinatura.setSituacao(Situacao.QUITADO);
            // assinatura.addColeira(new UsuarioAssinatura(assinatura, coleira));
            assinatura = save(assinatura);
            return assinatura;
        }
        log.info("Nenhum plano trial ativo. Ignorando a geração de assinatura trial ....");
        return null;
    }

    @Transactional
    public Assinatura update(final Assinatura entity, final boolean notifica) {
        final Assinatura assinatura = super.update(entity);
        if (notifica) {
            switch (assinatura.getSituacao()) {
                case QUITADO ->
                    eventPublisher.publishEvent(new AssinaturaEvent(assinatura, TipoEmail.ASSINATURA_PAGA));
                case NAO_AUTORIZADO ->
                    //Endpoint Patch /checkout/{idAssinatura} para gerar um novo link
                    eventPublisher.publishEvent(new AssinaturaEvent(assinatura, TipoEmail.ASSINATURA_NAO_AUTORIZADO));
                case CANCELADO ->
                    //Meio de pagamento cancelou. E-mail avisando que foi cancelado e terá que fazer outra
                    eventPublisher.publishEvent(new AssinaturaEvent(assinatura, TipoEmail.ASSINATURA_CANCELADA));
            }
        }
        return assinatura;
    }

    public CheckoutPagSeguro getCheckout(final String checkout) {
        return pagSeguroService.getCheckout(checkout);
    }

    public PedidoPagSeguro getPedido(final String checkout) {
        final CheckoutPagSeguro checkoutPagSeguro = pagSeguroService.getCheckout(checkout);
        final PedidoPagSeguro pedido = checkoutPagSeguro.getPedido();
        if (Optional.ofNullable(pedido).map(PedidoPagSeguro::getId).isPresent()) {
            return pagSeguroService.getPedido(pedido.getId());
        }
        return null;
    }

    private CheckoutPagSeguro geraCheckout(final Assinatura assinatura, final String urlRetorno) {
        final CheckoutPagSeguro checkoutPagSeguro = new CheckoutPagSeguro(assinatura, urlRetorno, webhook, webhookCheckout);
        return pagSeguroService.criaCheckout(checkoutPagSeguro);
    }

    private void validaCupom(final CupomDesconto cupom, final BigDecimal valorPlano) {
        if (!cupom.isValido()) {
            throw new InvalidDataException("Cupom inválido");
        }
        if (cupom.possuiValorMinimo() && valorPlano.compareTo(cupom.getValorMinimo()) < 0) {
            throw new InvalidDataException("Plano não atingiu valor minimo para esse cupom");
        }
    }

    private void validaPlano(final Plano plano) {
        if (!plano.getAtivo()) {
            throw new InvalidDataException("Plano inativo");
        }
        if (plano.getTrial()) {
            throw new InvalidDataException("Plano Trial não pode ser escolhido para assinatura");
        }
    }

}
