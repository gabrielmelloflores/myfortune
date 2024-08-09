package com.gabrielflores.myfortune.service.pagseguro;

import com.gabrielflores.myfortune.dto.pagseguro.PedidoPagSeguro;
import com.gabrielflores.myfortune.dto.pagseguro.pagamento.CheckoutPagSeguro;
import com.gabrielflores.myfortune.dto.pagseguro.transacao.Transacao;
import com.gabrielflores.myfortune.exception.PagSeguroException;
import com.gabrielflores.myfortune.exception.RegraNegocioException;
import com.gabrielflores.myfortune.model.assinatura.Assinatura;
import com.gabrielflores.myfortune.model.assinatura.NotificacaoPagamento;
import com.gabrielflores.myfortune.model.assinatura.Situacao;
import com.gabrielflores.myfortune.service.assinatura.AssinaturaService;
import com.gabrielflores.myfortune.service.assinatura.NotificacaoPagamentoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.Charset;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * https://dev.pagbank.uol.com.br/reference/webhooks <br>
 * https://dev.pagbank.uol.com.br/reference/webhooks-2
 *
 * @author Douglas Pasqualin <douglas.pasqualin@gmail.com>
 */
@Controller
@RequiredArgsConstructor
@Slf4j
public class PagSeguroWebHook {

    private static final String ORIGEM_CHECKOUT = "CHECKOUT";

    @Value("${gateway.pagamentos.pagseguro.token:default-token}")
    private final String token;

    private final AssinaturaService assinaturaService;

    private final NotificacaoPagamentoService notificacaoPagamentoService;

    private final PagSeguroTransactionService transactionService;

    private final ObjectMapper objectMapper;

    @PostMapping(value = "/webhook/pagseguro/pagamento")
    public ResponseEntity<String> pagSeguroPagamento(@RequestHeader(name = "x-authenticity-token", required = false) final String hashAutenticidade,
            @RequestHeader(name = "x-product-id", required = false) final String id,
            @RequestHeader(name = "x-product-origin", required = false) final String origem,
            @RequestBody final String payload) throws Exception {
        if (payload.contains("notificationType=transaction")) {
            //Pelos meus testes, notificações de disputa ou cancelamento só vieram por XML, precisa tratar ....
            final List<NameValuePair> parse = URLEncodedUtils.parse(payload, Charset.forName("UTF-8"));
            final String codigoNotificacao = parse.stream()
                    .filter(p -> p.getName().equals("notificationCode"))
                    .findFirst().map(p -> p.getValue())
                    .orElse("");
            log.info("Notificação pós-transacional em XML, code {}", codigoNotificacao);
            final Transacao transacao = transactionService.getNotificacao(codigoNotificacao);
            log.info("Código do pagamento notificado via XML: {}", transacao.getCodigo());
            final Assinatura assinatura = assinaturaService.getByIdTransacao(transacao.getCodigo());
            final Situacao situacaoTransacao = transacao.getSituacaoAssinatura();
            NotificacaoPagamento notificacao = new NotificacaoPagamento(null, codigoNotificacao, situacaoTransacao);
            notificacao = notificacaoPagamentoService.save(notificacao);
            if (assinatura != null && !assinatura.getSituacao().equals(situacaoTransacao)) {
                assinatura.setSituacao(situacaoTransacao);
                assinatura.setNotificacaoPagamento(notificacao);
                assinaturaService.update(assinatura, true);
            }
            return ResponseEntity.ok().build();
        }
        if (!validaAutenticidadeNotificacao(hashAutenticidade, payload)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        if (ORIGEM_CHECKOUT.equals(origem)) {
            log.info("Webhook chamado, código checkout {}", id);
            final Assinatura assinatura = assinaturaService.getByIdCheckout(id);
            if (assinatura != null) {
                final PedidoPagSeguro pedido = objectMapper.readValue(payload, PedidoPagSeguro.class);
                final Situacao situacao = pedido.getCobranca().getSituacaoAssinatura();
                NotificacaoPagamento notificacao = new NotificacaoPagamento(id, null, situacao);
                notificacao = notificacaoPagamentoService.save(notificacao);
                //Só vamos atualizar se a situação mudou (webhook pode ter sido chamado mais de uma vez pelo PagSeguro)
                if (!situacao.equals(assinatura.getSituacao())) {
                    assinatura.setSituacao(situacao);
                    assinatura.setNotificacaoPagamento(notificacao);
                    assinatura.setIdTransacao(pedido.getCobranca().getIdTransacao());
                    if (situacao.isPago()) {
                        assinatura.setFormaPagamento(pedido.getCobranca().getFormaPagamento());
                        assinatura.setNumeroParcelas(pedido.getCobranca().getFormaPagamentoPagSeguro().getParcelas());
                        assinatura.setDataPagamento(pedido.getCobranca().getDataPagamento());
                    }
                    assinaturaService.update(assinatura, true);
                }
            } else {
                //Pelo que entendi, só vai cair nesse método do webhook se a pessoa pagou/gerou cobrança.
                //Se não achou no nosso banco de dados o checkout, acho que precisa enviar e-mail avisando o ADM do sistema
                log.error("Checkout não existente: " + id);
                throw new RegraNegocioException("Assinatura não encontrada para o checkout: " + id);
            }
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/webhook/pagseguro/checkout")
    public ResponseEntity<String> pagSeguroCheckout(@RequestHeader(name = "x-authenticity-token", required = false) final String hashAutenticidade,
            @RequestBody final String payload) throws Exception {
        if (!validaAutenticidadeNotificacao(hashAutenticidade, payload)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        final CheckoutPagSeguro checkoutPagSeguro = objectMapper.readValue(payload, CheckoutPagSeguro.class);
        final Assinatura assinatura = assinaturaService.getByIdCheckout(checkoutPagSeguro.getId());
        NotificacaoPagamento notificacao = new NotificacaoPagamento(checkoutPagSeguro.getId(), null, null);
        notificacao = notificacaoPagamentoService.save(notificacao);
        if (assinatura != null && checkoutPagSeguro.isExpirado()) {
            assinatura.setCheckoutValido(Boolean.FALSE);
            //Só seta como encerrado se a situação ainda for a original. Se já pagou deixa a situação PAGO
            if (assinatura.getSituacao().isPendente()) {
                assinatura.setNotificacaoPagamento(notificacao);
                assinatura.setSituacao(Situacao.ENCERRADO);
            }
            //TODO: liberar cupom de desconto?
            assinaturaService.update(assinatura);
        }
        return ResponseEntity.ok().build();
    }

    /* https://dev.pagbank.uol.com.br/reference/confirmar-autenticidade-da-notificacao */
    private boolean validaAutenticidadeNotificacao(final String hashAutenticidade, final String payload) {
        if (hashAutenticidade == null) {
            log.warn("Notificação sem header de autenticidade, ignorando ....");
            return false;
        }
        final String hashCalculado = DigestUtils.sha256Hex(token + "-" + payload);
        if (!hashCalculado.equals(hashAutenticidade)) {
            throw new PagSeguroException("Não foi possível confirmar a autenticidade da notificação pagSeguro: " + payload);
        }
        return true;
    }

}
