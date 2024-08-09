package com.gabrielflores.myfortune.service.pagseguro;

import com.gabrielflores.myfortune.dto.pagseguro.transacao.Transacao;
import com.gabrielflores.myfortune.exception.PagSeguroException;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import java.io.StringReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * Trata o recebimento das notificações do tipo "transaction" em XML
 *
 * @author Douglas Pasqualin <douglas.pasqualin@gmail.com>
 */
@Service
public class PagSeguroTransactionService {

    private final WebClient webClient;

    private final String urlBase;

    private final String token;

    private final String email;

    public PagSeguroTransactionService(
        @Value("${gateway.pagamentos.pagseguro.transaction.endpoint:http://default-url-base}") final String urlBase,
        @Value("${gateway.pagamentos.pagseguro.token:default-token}") final String token,
        @Value("${gateway.pagamentos.pagseguro.email:default-email}") final String email) {
    this.urlBase = urlBase;
        this.token = token;
        this.email = email;
        final ExchangeFilterFunction errorResponseFilter = ExchangeFilterFunction.ofResponseProcessor(this::exchangeFilterResponseProcessor);
        this.webClient = WebClient.builder()
                .baseUrl(this.urlBase)
                .filter(errorResponseFilter)
                .build();
    }

    public Transacao getNotificacao(final String codigo) {
        final String xml = webClient
                .get()
                .uri(uriBuilder -> uriBuilder
                .path("/notifications/{codigo}")
                .queryParam("email", email)
                .queryParam("token", token)
                .build(codigo))
                .retrieve()
                .bodyToMono(String.class)
                .block();
        try {
            JAXBContext context = JAXBContext.newInstance(Transacao.class);
            Unmarshaller um = context.createUnmarshaller();
            return (Transacao) um.unmarshal(new StringReader(xml));
        } catch (JAXBException ex) {
            throw new PagSeguroException("Erro convertendo o conteúdo da transação para o XML de retorno", ex);
        }
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

}
