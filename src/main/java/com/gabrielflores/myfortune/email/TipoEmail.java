package com.gabrielflores.myfortune.email;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 *
 * @author Douglas Pasqualin <douglas.pasqualin@gmail.com>
 */
@Getter
@RequiredArgsConstructor
public enum TipoEmail {
    CONFIRMACAO_CONTA("Pet Connect - Confirmação de Conta", "confirmacao_conta.html", "app.url.confirma.usuario"),
    RECUPERACAO_SENHA("Pet Connect - Recuperação de Senha", "recuperacao_senha.html", "app.url.recupera.senha"),
    CONVITE_COMPARTILHAR_PET("Pet Connect - Compartilhamento de Pet", "compartilhar_pet.html", "app.url.compartilha.pet"),
    CONVITE_PLATAFORMA("Pet Connect - Convite de Membros", "convite_plataforma.html", "app.url.platafoma.download"),
    ASSINATURA_PEDIDO_RECEBIDO("Pet Connect - Pedido recebido", "assinatura/pedido_recebido.html"),
    ASSINATURA_PAGA("Pet Connect - Confirmação de Pagamento", "assinatura/confirmacao_pagamento.html", "app.url.platafoma.download"),
    ASSINATURA_NAO_AUTORIZADO("Pet Connect - Pagamento não autorizado", "assinatura/pagamento_nao_autorizado.html", "app.url.assinatura.revisar"),
    ASSINATURA_NAO_PAGA("Pet Connect - Pagamento não confirmado", "assinatura/pagamento_nao_confirmado.html", "app.url.assinatura.revisar"),
    ASSINATURA_CANCELADA("Pet Connect - Assinatura cancelada", "assinatura/cancelada.html"),
    //ASSINATURA_CANCELADA_User("Pet Connect - Confirmação de cancelamento", ""),
    ASSINATURA_VENCENDO("Pet Connect - Assinatura vencendo", "assinatura/vencendo_nao_renovada.html", "app.url.assinatura.revisar"),
    ASSINATURA_VENCIDA("Pet Connect - Renove sua assinatura", "assinatura/vencida_nao_renovada.html", "app.url.assinatura.revisar"),
    ASSINATURA_TRIAL("Pet Connect - Bem vindo", "assinatura/trial.html", "app.url.platafoma.download");

    private final String assunto;
    private final String modelo;
    private final String link;

    private TipoEmail(final String assunto, final String modelo) {
        this(assunto, modelo, "");
    }

}
