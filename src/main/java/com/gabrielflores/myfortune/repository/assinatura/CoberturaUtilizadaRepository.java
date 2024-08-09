package com.gabrielflores.myfortune.repository.assinatura;

import com.gabrielflores.myfortune.model.assinatura.CoberturaUtilizada;
import com.gabrielflores.myfortune.model.assinatura.TipoCobertura;
import com.gabrielflores.myfortune.model.dto.assinatura.CoberturaUtilizadaDto;
import com.gabrielflores.myfortune.repository.BaseRepository;
import com.gabrielflores.myfortune.util.AnoMesReferencia;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

/**
 *
 * @author Douglas Pasqualin <douglas.pasqualin@gmail.com>
 */
public interface CoberturaUtilizadaRepository extends BaseRepository<CoberturaUtilizada> {

    // @Query("""
    //     select c from CoberturaUtilizada c join c.cobertura co
    //        where co.tipoCobertura = :tipoCobertura and
    //        c.coleira.id = :idColeira and
    //        c.anoMesReferencia = :anoMesReferencia
    //        order by c.dataUtilizacao
    // """)
    // Page<CoberturaUtilizadaDto> findAllByColeiraAnoMesReferenciaTipoCobertura(Long idColeira, AnoMesReferencia anoMesReferencia, TipoCobertura tipoCobertura, Pageable pageable);

    // @Query("""
    //     select count(c) from CoberturaUtilizada c join c.cobertura co
    //        where co.tipoCobertura = :tipoCobertura and
    //        c.coleira.id = :idColeira and
    //        c.anoMesReferencia = :anoMesReferencia
    // """)
    // Integer countUtilizacaoByColeiraAnoMesReferenciaTipoCobertura(Long idColeira, AnoMesReferencia anoMesReferencia, TipoCobertura tipoCobertura);
}
