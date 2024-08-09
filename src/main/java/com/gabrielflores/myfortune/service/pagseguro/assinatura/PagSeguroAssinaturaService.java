package com.gabrielflores.myfortune.service.pagseguro.assinatura;

import com.gabrielflores.myfortune.dto.pagseguro.PagSeguroObject;
import com.gabrielflores.myfortune.dto.pagseguro.assinatura.CupomDescontoPagSeguro;
import com.gabrielflores.myfortune.dto.pagseguro.assinatura.PlanoPagSeguro;
import com.gabrielflores.myfortune.exception.PagSeguroException;
import com.gabrielflores.myfortune.model.assinatura.CupomDesconto;
import com.gabrielflores.myfortune.model.assinatura.Plano;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 *
 * @author Douglas Pasqualin <douglas.pasqualin@gmail.com>
 */
//@Service
//vai ser utilizada a API de Checkout/Pedidos
@Deprecated
public class PagSeguroAssinaturaService {

    private final WebClient webClient;

    private final String urlBaseAssinatura;

    private final String token;

    public PagSeguroAssinaturaService(@Value("${gateway.pagamentos.pagseguro.assinaturas.endpoint}") String urlBaseAssinatura,
            @Value("${gateway.pagamentos.pagseguro.token}") String token) {
        this.urlBaseAssinatura = urlBaseAssinatura;
        this.token = token;
        final ExchangeFilterFunction errorResponseFilter = ExchangeFilterFunction.ofResponseProcessor(this::exchangeFilterResponseProcessor);
        this.webClient = WebClient.builder()
                .baseUrl(this.urlBaseAssinatura)
                .defaultHeaders(header -> {
                    header.setBearerAuth(this.token);
                    header.set(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
                    header.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
                })
                .filter(errorResponseFilter)
                .build();
    }

    // <editor-fold desc="Planos de assinaturas">
    public String criaNovoPlano(final Plano plano) {
        final PlanoPagSeguro planoPagSeguro = new PlanoPagSeguro(plano);
        return criaNovoObjeto("/plans", planoPagSeguro);
    }

    public void atualizaPlano(final Plano plano) {
        final PlanoPagSeguro planoPagSeguro = new PlanoPagSeguro(plano);
        atualizaObjeto("/plans/{plan_id}", plano.getIdExterno(), planoPagSeguro);
    }

    public void ativaPlano(final Plano plano) {
        atualizaSemBody("/plans/{plan_id}/activate", plano.getIdExterno());
    }

    public void inativaPlano(final Plano plano) {
        atualizaSemBody("/plans/{plan_id}/inactivate", plano.getIdExterno());
    }
    //</editor-fold>

    // <editor-fold desc="Cupons de desconto">
    public String criaNovoCupom(final CupomDesconto cupomDesconto) {
        final CupomDescontoPagSeguro cupomDescontoPagSeguro = new CupomDescontoPagSeguro(cupomDesconto);
        return criaNovoObjeto("/coupons", cupomDescontoPagSeguro);
    }

    public String atualizaCupom(final CupomDesconto cupomDesconto) {
        final CupomDescontoPagSeguro cupomDescontoPagSeguro = new CupomDescontoPagSeguro(cupomDesconto);
        //Não existe uma rota pra atualizar cupom. Precisa inativar e criar outro
        inativaCupom(cupomDesconto);
        return criaNovoObjeto("/coupons", cupomDescontoPagSeguro);
    }

    public void inativaCupom(final CupomDesconto cupomDesconto) {
        atualizaSemBody("/coupons/{coupon_id}/inactivate", cupomDesconto.getIdExterno());
    }
    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Métodos privados">
    private void atualizaSemBody(final String uri, final String id) {
        webClient
                .put()
                .uri(uri, id)
                .retrieve()
                .toBodilessEntity()
                .block();
    }

    private String criaNovoObjeto(final String uri, final PagSeguroObject objetoSalvar) {
        final PagSeguroObject pagSeguroObject = webClient
                .post()
                .uri(uri)
                .headers(h -> h.add("x-idempotency-key", objetoSalvar.getMD5()))
                .bodyValue(objetoSalvar)
                .retrieve()
                .bodyToMono(PagSeguroObject.class)
                .block();
        return pagSeguroObject.getId();
    }

    private void atualizaObjeto(final String uri, final String id, final PagSeguroObject objetoAtualizar) {
        webClient
                .put()
                .uri(uri, id)
                .bodyValue(objetoAtualizar)
                .retrieve()
                .bodyToMono(String.class)
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
