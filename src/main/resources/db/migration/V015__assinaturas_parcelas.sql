/**
 * Author:  Douglas Pasqualin <douglas.pasqualin@gmail.com>
 * Created: 12 de out. de 2023
 */
/* remove modelo antigo */
drop table assinaturas cascade;
-- delete from usuarios_assinaturas;
-- delete from audit.usuarios_assinaturas_revisions;
drop table if exists parcelas_assinaturas cascade;
drop table audit.assinaturas_revisions;
drop table if exists audit.parcelas_assinaturas_revisions;

drop sequence if exists seq_parcelas_assinaturas;
drop sequence seq_assinaturas;

-- Modelo novo, sem parcelas. Cada pagamento vai ser uma "assinatura" nova
create table assinaturas (
    id			integer not null,
    id_usuario          integer not null, --User que fez o plano
    id_plano		integer not null,
    id_cupom		integer, --se usou algum cupom
    valor 		numeric(10,2) not null, --valor original do plano, sem abatimentos
    vl_desconto		numeric(10,2), --que o cupom gerou
    valor_final		numeric(10,2) not null, --(vl_desconto - vl_total)
    dh_assinatura	timestamp not null,
    situacao		character(1) not null, --Enum (tentar syncar com a situações de pagamento do PagSeguro)
    dt_proxima_cobranca	date, --Para facilitar quando gerar a parcela/enviar para cobrança
    --cancelamento pelo User (será que vai poder? deixar previsto)
    dh_cancelamento	timestamp, --se foi cancelado, quando
    motivo		varchar(1024), --motivo do cancelamento
    --Integração PagSeguro
    url_checkout        text, --url para pagar no PagSeguro
    id_checkout         character(41), --ID no PagSeguro
    checkout_valido     boolean, --webhook do PagSeguro vai informar se expirou ou não
    forma_pagamento     character(1), -- C(Cartão Crédito), P(PIX) , B(Boleto), D(Cartão Débito)
    dh_pagamento        timestamp,

    versao              integer not null,
    operador            integer not null,
    dh_alteracao        timestamp not null,
    endereco_ip         character(15) not null
);
alter table assinaturas add constraint pk_assinatura primary key (id);
alter table assinaturas add constraint fk_assinatura_usuario foreign key (id_usuario) references usuarios;
create index ix_assinatura_usuario on assinaturas(id_usuario);
alter table assinaturas add constraint fk_assinatura_plano foreign key (id_plano) references planos_assinaturas;
create index ix_assinatura_User on assinaturas(id_usuario, situacao);
create index ix_assinatura_plano on assinaturas(id_plano);
create index ix_assinatura_situacao on assinaturas(situacao, forma_pagamento);
create index ix_assinatura_proxima_cobranca on assinaturas(dt_proxima_cobranca, situacao); --útil para gerar as próximas parcelas/cobranças
create index ix_assinatura_data on assinaturas(dh_assinatura, situacao); --scheduled task, ver pendentes
alter table assinaturas add constraint fk_assinatura_cupom foreign key (id_cupom) references cupons_descontos;
create index uk_assinatura_checkout on assinaturas(id_checkout);
create sequence seq_assinaturas start with 1 increment by 1;

--recria FK que foi apagada no drop cascade
-- alter table usuarios_assinaturas add constraint fk_coleira_assinatura foreign key (id_assinatura) references assinaturas;

--Quantidade na cobertura do plano
alter table planos_coberturas add column if not exists quantidade integer;

--máximo de parcelas para pagamento do plano
alter table planos_assinaturas add column if not exists limite_parcelas integer;

--Auditoria
create table audit.assinaturas_revisions (
    id                  integer not null,
    id_usuario          integer,
    id_plano		integer,
    id_cupom		integer,
    valor 		numeric(10,2),
    vl_desconto		numeric(10,2),
    valor_final		numeric(10,2),
    dh_assinatura	timestamp,
    situacao		character(1),
    dt_proxima_cobranca	date,
    dh_cancelamento	timestamp,
    motivo		varchar(1024),
    url_checkout        text,
    id_checkout         character(41),
    checkout_valido     boolean,
    forma_pagamento     character(1),
    dh_pagamento        timestamp,

    revision_id         integer not null,
    revision_type       smallint not null,
    constraint assinaturas_rev_pk primary key (id, revision_id)
);

alter table audit.planos_coberturas_revisions add column if not exists quantidade integer;

alter table audit.planos_assinaturas_revisions add column if not exists limite_parcelas integer;