/**
 * Author:  Douglas Pasqualin <douglas.pasqualin@gmail.com>
 * Created: 25 de nov. de 2023
 */

alter table planos_assinaturas add trial boolean not null default false;

create index ix_plano_assinatura_trial on planos_assinaturas(trial);

alter table audit.planos_assinaturas_revisions add trial boolean;