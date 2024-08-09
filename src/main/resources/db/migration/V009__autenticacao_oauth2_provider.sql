/**
 * Author:  Douglas Pasqualin <douglas.pasqualin@gmail.com>
 * Created: 2 de set. de 2023
 */
alter table usuarios add provider varchar(10) not null default 'local';

alter table audit.usuarios_revisions add provider varchar(10);

update audit.usuarios_revisions set provider = 'local';