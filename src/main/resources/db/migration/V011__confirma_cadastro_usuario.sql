/**
 * Author:  Giuliano Ferreira <giuliano@ufsm.br>
 * Created: 10 de set. de 2023
 */

alter table usuarios add column confirmado boolean not null default true;

alter table audit.usuarios_revisions add column confirmado boolean;
