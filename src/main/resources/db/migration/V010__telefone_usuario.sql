/**
 * Author:  Giuliano Ferreira <giuliano@ufsm.br>
 * Created: 4 de set. de 2023
 */

alter table usuarios add column telefone varchar(20);

alter table audit.usuarios_revisions add column telefone varchar(20);