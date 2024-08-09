/**
 * Author:  Douglas Pasqualin <douglas.pasqualin@gmail.com>
 * Created: 11 de ago. de 2023
 */

-- Plano de venda
create table planos_assinaturas (
    id              integer not null,
    descricao       varchar(30) not null,
    valor           numeric(10,2) not null,
    vigencia        character(1) not null, -- M (Mensal), A (Anual), D (Dias)
    num_dias        integer, --Se tipo = D
    ativo           boolean not null default true,

    versao          integer not null,
    operador        integer not null,
    dh_alteracao    timestamp not null,
    endereco_ip     character(15) not null
);
alter table planos_assinaturas add constraint pk_plano_assinatura primary key (id);
create sequence seq_planos_assinaturas start with 1 increment by 1;

create table planos_coberturas (
    id              integer not null,
    id_plano        integer not null,
    cobertura       integer not null, --enum ordinal
    dt_inicio       date not null, --quando iniciou essa cobertura, default data de salvar
    dt_fim          date, --se saiu fora do plano

    versao          integer not null,
    operador        integer not null,
    dh_alteracao    timestamp not null,
    endereco_ip     character(15) not null
);
alter table planos_coberturas add constraint pk_plano_cobertura primary key (id);
alter table planos_coberturas add constraint fk_plano_cobertura foreign key (id_plano) references planos_assinaturas;
create index ix_planos_coberturas on planos_coberturas(id_plano);
create sequence seq_planos_coberturas start with 1 increment by 1;

--Cupons de desconto (já prever)
create table cupons_descontos (
    id              integer not null,
    codigo          varchar(20) not null,
    descricao       varchar(60), --Se quiser detalhar a origem do cupom, ex: parceira com loja x
    tipo_desconto   character(1) not null, --P (percentual), V (valor fixo)
    valor           numeric(10,2), --valor que o cupom abate
    vl_minimo       numeric(10,2), --valor minimo para ativar o cupom (opcional)
    dt_validade     date, --Se existe data para expiração
    qt_disponivel   integer, --Quantos cupons podem ser usados (opcional)
    qt_utilizada    integer, --Para controle de quantos já foram utilizados

    versao          integer not null,
    operador        integer not null,
    dh_alteracao    timestamp not null,
    endereco_ip     character(15) not null
);
alter table cupons_descontos add constraint pk_cupom_desconto primary key (id);
create unique index uk_cupom_descricao on cupons_descontos(codigo);
create sequence seq_cupons_descontos start with 1 increment by 1;

-- Assinatura (Aqui seria um "resumo" do plano contratado, nas parcelas vai debitando mês a mês (se for mensal))
create table assinaturas (
    id			integer not null,
    id_usuario          integer not null, --User que fez o plano
    id_plano		integer not null,
    id_cupom		integer, --se usou algum cupom
    vl_desconto		numeric(10,2), --que o cupom gerou
    vl_total 		numeric(10,2) not null, --valor original do plano, sem abatimentos
    vl_pago		numeric(10,2) not null, --com o tempo pode ser reajustado o valor do plano, salvar aqui o valor pago originalmente (com desconto já)
    vl_atual		numeric(10,2) not null, --quanto está atualmente (com reajustes e tudo mais), vai ser o valor que vai pra parcela
    dh_assinatura	timestamp not null,
    situacao		character(1) not null, --controlar por aqui se tá ativo. A(ativo), C(cancelado pelo User), N(não pago a parcela)
    dh_cancelamento	timestamp, --se foi cancelado, quando
    motivo		varchar(1024), --motivo do cancelamento
    dt_proxima_cobranca	date, --Para facilitar quando gerar a parcela/enviar para cobrança

    versao              integer not null,
    operador            integer not null,
    dh_alteracao        timestamp not null,
    endereco_ip         character(15) not null
);
alter table assinaturas add constraint pk_assinatura primary key (id);
alter table assinaturas add constraint fk_assinatura_usuario foreign key (id_usuario) references usuarios;
create index ix_assinatura_usuario on assinaturas(id_usuario);
alter table assinaturas add constraint fk_assinatura_plano foreign key (id_plano) references planos_assinaturas;
create index ix_assinatura_plano on assinaturas(id_plano);
create index ix_assinatura_proxima_cobranca on assinaturas(dt_proxima_cobranca, situacao); --útil para gerar as próximas parcelas/cobranças
alter table assinaturas add constraint fk_assinatura_cupom foreign key (id_cupom) references cupons_descontos;
create sequence seq_assinaturas start with 1 increment by 1;

--Parcelas, para planos mensais todo o mês gerar uma linha nova, em caso de pagamento, anual 1x por ano, etc
create table parcelas_assinaturas (
    id              integer not null,
    id_assinatura   integer not null,
    parcela         integer not null, --número da parcela (sequencial)
    vl_pago         numeric(10,2) not null,
    dh_geracao      timestamp not null,
    dh_pagamento    timestamp,
    situacao        character(1) not null, -- Q(quitado/pago), P(pendente/aguardando) , N(não autorizado)
    identificador   varchar(256), --o identifica essa cobrança como única
    autorizacao     varchar(256), --hash da transação/confirmação do meio de pagamento (ver se é necessário)

    versao          integer not null,
    operador        integer not null,
    dh_alteracao    timestamp not null,
    endereco_ip     character(15) not null
);
alter table parcelas_assinaturas add constraint pk_parcela_assinatura primary key (id);
alter table parcelas_assinaturas add constraint fk_parcela_assinatura foreign key (id_assinatura) references assinaturas;
create index ix_parcela_assinatura on parcelas_assinaturas(id_assinatura, parcela);
create index ix_parcela_situacao on parcelas_assinaturas(situacao);
create index uk_parcela_identificador on parcelas_assinaturas(identificador);
create sequence seq_parcelas_assinaturas start with 1 increment by 1;


-- Usuarios da Assinatura (inicialmente 1x1, mas tabela separada pra prever 1xN)
create table usuarios_assinaturas (
    id              integer not null,
    id_assinatura   integer not null,
    id_usuario      integer not null,
    dh_inclusao     timestamp not null, --se outra usuario for adicionada em outra data na mesma assinatura
    dh_remocao      timestamp, --pra manter histórico

    versao          integer not null,
    operador        integer not null,
    dh_alteracao    timestamp not null,
    endereco_ip     character(15) not null
);
alter table usuarios_assinaturas add constraint pk_usuario_assinatura primary key (id);
alter table usuarios_assinaturas add constraint fk_usuario_assinatura foreign key (id_assinatura) references assinaturas;
create index ix_usuario_assinatura_01 on usuarios_assinaturas(id_assinatura, id_usuario);
create index ix_usuario_assinatura_02 on usuarios_assinaturas(id_usuario, id_assinatura);
alter table usuarios_assinaturas add constraint fk_usuario_assinatura_usuario foreign key (id_usuario) references usuarios;
create sequence seq_usuarios_assinaturas start with 1 increment by 1;


/* auditorias */

create table audit.planos_assinaturas_revisions (
    id              integer not null,
    descricao       varchar(30),
    valor           numeric(10,2),
    vigencia        character(1),
    num_dias        integer,
    ativo           boolean,

    revision_id     integer not null,
    revision_type   smallint not null,
    constraint planos_assinaturas_rev_pk primary key (id, revision_id)
);

create table audit.planos_coberturas_revisions (
    id              integer not null,
    id_plano        integer,
    cobertura       integer,
    dt_inicio       date,
    dt_fim          date,

    revision_id     integer not null,
    revision_type   smallint not null,
    constraint planos_coberturas_rev_pk primary key (id, revision_id)
);

create table audit.cupons_descontos_revisions (
    id              integer not null,
    codigo          varchar(20),
    descricao       varchar(60),
    tipo_desconto   character(1),
    valor           numeric(10,2),
    vl_minimo       numeric(10,2),
    dt_validade     date,
    qt_disponivel   integer,
    qt_utilizada    integer,

    revision_id     integer not null,
    revision_type   smallint not null,
    constraint cupons_descontos_rev_pk primary key (id, revision_id)
);

create table audit.assinaturas_revisions (
    id                  integer not null,
    id_usuario          integer,
    id_plano            integer,
    id_cupom            integer,
    vl_desconto         numeric(10,2),
    vl_total            numeric(10,2),
    vl_pago             numeric(10,2),
    vl_atual            numeric(10,2),
    dh_assinatura       timestamp,
    situacao            character(1),
    dh_cancelamento     timestamp,
    motivo              varchar(1024),
    dt_proxima_cobranca	date,

    revision_id         integer not null,
    revision_type       smallint not null,
    constraint assinaturas_rev_pk primary key (id, revision_id)
);

create table audit.parcelas_assinaturas_revisions (
    id              integer not null,
    id_assinatura   integer,
    parcela         integer,
    vl_pago         numeric(10,2),
    dh_pagamento    timestamp,
    situacao        character(1),
    identificador   varchar(256),
    autorizacao     varchar(256),

    revision_id     integer not null,
    revision_type   smallint not null,
    constraint parcelas_assinaturas_rev_pk primary key (id, revision_id)
);

-- create table audit.usuarios_assinaturas_revisions (
--     id              integer not null,
--     id_assinatura   integer,
--     id_usuario      integer,
--     dh_inclusao     timestamp,
--     dh_remocao      timestamp,

--     revision_id     integer not null,
--     revision_type   smallint not null,
--     constraint usuarios_assinaturas_rev_pk primary key (id, revision_id)
-- );