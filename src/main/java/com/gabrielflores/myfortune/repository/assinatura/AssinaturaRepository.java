package com.gabrielflores.myfortune.repository.assinatura;

import com.gabrielflores.myfortune.model.assinatura.Assinatura;
import com.gabrielflores.myfortune.model.assinatura.Situacao;
import com.gabrielflores.myfortune.model.assinatura.TipoCobertura;
import com.gabrielflores.myfortune.repository.BaseRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author Douglas Pasqualin <douglas.pasqualin@gmail.com>
 */
public interface AssinaturaRepository extends BaseRepository<Assinatura> {

    Optional<Assinatura> findByIdCheckout(String idCheckout);

    Optional<Assinatura> findByIdTransacao(String idTransacao);

    @Query("select a from Assinatura a where a.User.id = :idUser "
            + "and a.situacao = com.gabrielflores.myfortune.model.assinatura.Situacao.QUITADO "
            + "and a.dataCancelamento is null and a.dataProximaCobranca >= current_date "
            + "order by a.dataProximaCobranca ")
    <T> List<T> findVigenteByUser(@Param("idUser") Long idUser, Class<T> clazz);

//     @Query("select a from UsuarioAssinatura c join c.assinatura a where c.coleira.id = :idColeira "
//             + "and a.situacao = com.gabrielflores.myfortune.model.assinatura.Situacao.QUITADO "
//             + "and a.dataCancelamento is null and a.dataProximaCobranca >= current_date "
//             + "order by a.dataProximaCobranca ")
//     <T> List<T> findVigenteByColeira(@Param("idColeira") Long idColeira, Class<T> clazz);

//     @Query("select a from UsuarioAssinatura c join c.assinatura a join fetch a.plano p join fetch p.coberturas cob "
//             + "where c.coleira.id = :idColeira "
//             + "and a.situacao = com.gabrielflores.myfortune.model.assinatura.Situacao.QUITADO "
//             + "and a.dataCancelamento is null and a.dataProximaCobranca >= current_date "
//             + "and cob.tipoCobertura = :tipoCobertura "
//             + "and cob.dataInicio <= current_date and (cob.dataFim is null or cob.dataFim >= current_date) "
//             + "order by a.dataProximaCobranca ")
//     <T> List<T> findVigenteByColeiraCobertura(@Param("idColeira") Long idColeira, final TipoCobertura tipoCobertura, Class<T> clazz);

//     @Query("select a from UsuarioAssinatura c "
//             + "join c.assinatura a join a.plano p "
//             + "where c.coleira.id = :idColeira and "
//             + "p.trial = true ")
//     <T> List<T> findTrialByColeira(@Param("idColeira") Long idColeira, Class<T> clazz);

//     @Query("select a from Assinatura a where a.User.id = :idUser "
//             + "and a.situacao = :situacao "
//             + "and a.dataCancelamento is null and a.dataProximaCobranca >= current_date "
//             + "and exists (select 1 from UsuarioAssinatura c where c.assinatura.id = a.id and "
//             + "             c.coleira.id = :idColeira ) "
//             + "order by a.dataProximaCobranca ")
//     <T> List<T> findByUserSituacaoColeira(@Param("idUser") Long idUser, @Param("situacao") Situacao situacao, @Param("idColeira") Long idColeira, Class<T> clazz);

    @Query(nativeQuery = true, value = "select a.* from assinaturas a  "
            + "where a.situacao in ('P', 'E') and "
            + "extract('EPOCH' from current_timestamp - a.dh_assinatura)/3600 <= :horas ")
    List<Assinatura> findPendentesPagamento(@Param("horas") final Integer horas);

    @EntityGraph(attributePaths = {"User", "plano"})
    @Query("select a from Assinatura a "
            + "where a.situacao not in (com.gabrielflores.myfortune.model.assinatura.Situacao.QUITADO, "
            + "com.gabrielflores.myfortune.model.assinatura.Situacao.CANCELADO_User) and "
            + "date(a.dataAssinatura) = :dataReferencia ")
    List<Assinatura> findNaoPagosByDataReferencia(@Param("dataReferencia") final LocalDate dataReferencia);

//     @EntityGraph(attributePaths = {"User", "plano"})
//     @Query("select distinct a from Assinatura a join a.coleiras c "
//             + "where a.situacao = com.gabrielflores.myfortune.model.assinatura.Situacao.QUITADO and "
//             + "a.dataCancelamento is null and a.dataProximaCobranca IN :datas and "
//             + "not exists (select 1 from UsuarioAssinatura x join x.assinatura y where y.id = a.id and  "
//             + "x.id = c.id and a.id <> y.id and "
//             + "y.situacao = com.gabrielflores.myfortune.model.assinatura.Situacao.QUITADO and "
//             + "y.dataCancelamento is null and y.dataProximaCobranca > current_date)")
//     List<Assinatura> findNaoRenovadasByVencimento(@Param("datas") final List<LocalDate> datas);
}
