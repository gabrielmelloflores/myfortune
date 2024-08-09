/**
 * Author:  Giuliano Ferreira <giuliano@ufsm.br>
 * Created: 21 de set. de 2023
 */

alter table usuarios add column genero character;

alter table audit.usuarios_revisions add column genero character;
