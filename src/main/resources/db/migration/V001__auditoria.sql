/**
 * Author:  Giuliano Ferreira <giuliano@ufsm.br>
 * Created: 16 de jul. de 2023
 */

create schema if not exists audit;

create table audit.revisions (
    id             integer not null,
    timestamp      bigint not null,
    user_id        integer
);

alter table audit.revisions add constraint revisions_pk primary key (id);

create sequence audit.revisions_seq start with 1 increment by 50;
