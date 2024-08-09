/**
 * Author:  Giuliano Ferreira <giuliano@ufsm.br>
 * Created: 16 de jul. de 2023
 */

/* perfis */
create table perfis (
    id              integer not null,
    nome            varchar(20) not null,

    versao          integer not null,
    operador        integer not null,
    dh_alteracao    timestamp not null,
    endereco_ip     character(15) not null
);
alter table perfis add constraint pk_perfis primary key (id);
create sequence seq_perfis start with 1 increment by 1;
create unique index uk_perfis on perfis(nome);


/* usuarios */
create table usuarios (
    id              integer not null,
    nome            varchar(255) not null,
    email           varchar(255) not null,
    cpf             character(14),
    dt_nascimento   date,
    senha           varchar(128) not null,
    ativo           boolean not null,
    url_foto        varchar(255),

    versao          integer not null,
    operador        integer not null,
    dh_alteracao    timestamp not null,
    endereco_ip     character(15) not null
);
alter table usuarios add constraint pk_usuarios primary key (id);
create sequence seq_usuarios start with 1 increment by 1;
create unique index uk_usuarios on usuarios(email);
create index ix_usuarios_cpf on usuarios(cpf);
create index ix_usuarios_nome on usuarios(nome);


/* perfis usuarios */
create table usuarios_perfis (
    id_usuario      integer not null,
    id_perfil       integer not null
);
alter table usuarios_perfis add constraint pk_usuarios_perfis primary key (id_usuario, id_perfil);
alter table usuarios_perfis add constraint fk_usuario foreign key (id_usuario) references usuarios on delete cascade;
alter table usuarios_perfis add constraint fk_perfil foreign key (id_perfil) references perfis on delete cascade;


/* auditorias */

create table audit.perfis_revisions (
    id              integer not null,
    nome            varchar(20),

    revision_id     integer not null,
    revision_type   smallint not null,
    constraint perfis_rev_pk primary key (id, revision_id)
);

create table audit.usuarios_revisions (
    id              integer not null,
    nome            varchar(255),
    email           varchar(255),
    cpf             varchar(14),
    dt_nascimento   date,
    senha           varchar(128),
    ativo           boolean,
    url_foto        varchar(255),

    revision_id     integer not null,
    revision_type   smallint not null,
    constraint usuarios_rev_pk primary key (id, revision_id)
);

create table audit.usuarios_perfis_revisions (
    id_usuario      integer not null,
    id_perfil       integer not null,

    revision_id     integer not null,
    revision_type   smallint not null,
    constraint usuarios_perfis_rev_pk primary key (id_usuario, id_perfil, revision_id)
);
