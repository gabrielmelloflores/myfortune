package com.gabrielflores.myfortune.service.pagseguro;

import com.gabrielflores.myfortune.dto.pagseguro.PagSeguroObject;
import com.gabrielflores.myfortune.dto.pagseguro.PedidoPagSeguro;
import com.gabrielflores.myfortune.dto.pagseguro.pagamento.CheckoutPagSeguro;
import com.gabrielflores.myfortune.dto.pagseguro.pagamento.CobrancaPagSeguro;
import com.gabrielflores.myfortune.exception.PagSeguroException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 *
 * @author Douglas Pasqualin <douglas.pasqualin@gmail.com>
 */
@Service
@Slf4j
public class PagSeguroService {

    private final WebClient webClient;

    private final String urlBase;

    private final String token;

    public PagSeguroService(
            @Value("${gateway.pagamentos.pagseguro.endpoint:http://default-url-base}") final String urlBase,
            @Value("${gateway.pagamentos.pagseguro.token:default-token}") final String token) {
        this.urlBase = urlBase;
        this.token = token;
        final ExchangeFilterFunction errorResponseFilter = ExchangeFilterFunction.ofResponseProcessor(this::exchangeFilterResponseProcessor);
        this.webClient = WebClient.builder()
                .baseUrl(this.urlBase)
                .defaultHeaders(header -> {
                    header.setBearerAuth(this.token);
                    header.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
                    header.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
                })
                .filter(errorResponseFilter)
                .build();
    }

    /* https://dev.pagbank.uol.com.br/reference/criar-um-checkout-1 */
    public CheckoutPagSeguro criaCheckout(final CheckoutPagSeguro checkoutPagSeguro) {
        return postNewObject("/checkouts", checkoutPagSeguro, CheckoutPagSeguro.class);
    }

    /* https://dev.pagbank.uol.com.br/reference/consultar-um-checkout */
    public CheckoutPagSeguro getCheckout(final String idCheckout) {
        return getObject("/checkouts/{checkout_id}", idCheckout, CheckoutPagSeguro.class);
    }

    /* https://dev.pagbank.uol.com.br/reference/consultar-pedido */
    public PedidoPagSeguro getPedido(final String idPedido) {
        return getObject("/orders/{order_id}", idPedido, PedidoPagSeguro.class);
    }

    @Deprecated
    /* https://dev.pagbank.uol.com.br/reference/criar-pedido */
    public PedidoPagSeguro criaNovoPedido(final PedidoPagSeguro pedidoPagSeguro) {
        return postNewObject("/orders", pedidoPagSeguro, PedidoPagSeguro.class);
    }

    @Deprecated
    /* https://dev.pagbank.uol.com.br/reference/pagar-pedido */
    public CobrancaPagSeguro pagaPedido(final String idPedido, final CobrancaPagSeguro cobrancaPagSeguro) {
        return postNewIdObject("/orders/{order_id}/pay", idPedido, cobrancaPagSeguro, CobrancaPagSeguro.class);
    }

    // <editor-fold defaultstate="collapsed" desc="Métodos privados">
    private <T extends PagSeguroObject> T postNewObject(final String uri, final T objectPost, final Class<T> clazz) {
        final T pagSeguroObject = webClient
                .post()
                .uri(uri)
                .headers(h -> h.add("x-idempotency-key", objectPost.getMD5()))
                .bodyValue(objectPost)
                .retrieve()
                .bodyToMono(clazz)
                .block();
        return pagSeguroObject;
    }

    private <T extends PagSeguroObject> T postNewIdObject(final String uri, final String id, final T objectPost, final Class<T> clazz) {
        final T pagSeguroObject = webClient
                .post()
                .uri(uri, id)
                .headers(h -> h.add("x-idempotency-key", objectPost.getMD5()))
                .bodyValue(objectPost)
                .retrieve()
                .bodyToMono(clazz)
                .block();
        return pagSeguroObject;
    }

    public <T extends PagSeguroObject> T getObject(final String uri, final String id, Class<T> clazz) {
        return webClient
                .get()
                .uri(uriBuilder -> uriBuilder
                .path(uri)
                .build(id))
                .retrieve()
                .bodyToMono(clazz)
                .block();
    }

    private Mono<ClientResponse> exchangeFilterResponseProcessor(ClientResponse response) {
        HttpStatusCode status = response.statusCode();
        if (status.isError()) {
            return response.bodyToMono(String.class)
                    .defaultIfEmpty(((HttpStatus) response.statusCode()).getReasonPhrase())
                    .flatMap(body -> Mono.error(new PagSeguroException(body)));
        }
        return Mono.just(response);
    }
    //</editor-fold>
}
