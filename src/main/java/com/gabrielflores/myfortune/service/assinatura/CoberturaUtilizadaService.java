package com.gabrielflores.myfortune.service.assinatura;

import com.gabrielflores.myfortune.dto.AnoMesReferenciaDto;
import com.gabrielflores.myfortune.exception.InvalidDataException;
import com.gabrielflores.myfortune.model.assinatura.Assinatura;
import com.gabrielflores.myfortune.model.assinatura.Cobertura;
import com.gabrielflores.myfortune.model.assinatura.CoberturaUtilizada;
import com.gabrielflores.myfortune.model.assinatura.TipoCobertura;
import com.gabrielflores.myfortune.model.dto.assinatura.CoberturaUtilizadaDto;
import com.gabrielflores.myfortune.repository.assinatura.CoberturaUtilizadaRepository;
import com.gabrielflores.myfortune.service.BaseService;
import com.gabrielflores.myfortune.util.AnoMesReferencia;
import com.gabrielflores.myfortune.util.CollectionUtils;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 *
 * @author Douglas Pasqualin <douglas.pasqualin@gmail.com>
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CoberturaUtilizadaService extends BaseService<CoberturaUtilizada, CoberturaUtilizadaRepository> {

    private final AssinaturaService assinaturaService;

    // @Transactional
    // public CoberturaUtilizada utilizaCobertura(final Long idColeira, final TipoCobertura tipoCobertura) {
    //     final List<Assinatura> assinaturas = assinaturaService.findVigenteByColeiraCobertura(idColeira, tipoCobertura);
    //     final Assinatura assinatura = CollectionUtils.getFirst(assinaturas);
    //     if (assinatura == null) {
    //         throw new InvalidDataException("Nenhuma assinatura vigente encontrada que possui a cobertura " + tipoCobertura);
    //     }
    //     final Cobertura cobertura = CollectionUtils.streamOf(assinatura.getPlano().getCoberturas())
    //             .filter(c -> c.getTipoCobertura().equals(tipoCobertura))
    //             .findFirst().orElse(null);

    //     final Integer totalUtilizadas = countByColeiraAnoMesTipoCobertura(idColeira, new AnoMesReferencia(LocalDate.now()), tipoCobertura);
    //     final Integer quantidadePlano = Optional.ofNullable(cobertura).map(Cobertura::getQuantidade).orElse(0);
    //     if (quantidadePlano > 0 && totalUtilizadas >= quantidadePlano && !cobertura.permiteExcederQuantidade()) {
    //         throw new InvalidDataException("Cobertura não permite quantidades excedentes. Já foi utilizado todo o limite mensal. Quantidade utilizada: " + totalUtilizadas);
    //     }
    //     CoberturaUtilizada coberturaUtilizada = new CoberturaUtilizada(cobertura, assinatura);
    //     coberturaUtilizada = save(coberturaUtilizada);
    //     return coberturaUtilizada;
    // }

    // public Page<CoberturaUtilizadaDto> findAllByColeiraAnoMesTipoCobertura(final Long idColeira, final AnoMesReferenciaDto anoMesReferenciaDto, final TipoCobertura tipoCobertura, final Pageable pageable) {
    //     return findAllByColeiraAnoMesTipoCobertura(idColeira, new AnoMesReferencia(anoMesReferenciaDto), tipoCobertura, pageable);
    // }

    // public Page<CoberturaUtilizadaDto> findAllByColeiraAnoMesTipoCobertura(final Long idColeira, final AnoMesReferencia anoMesReferencia, final TipoCobertura tipoCobertura, final Pageable pageable) {
    //     return repository.findAllByColeiraAnoMesReferenciaTipoCobertura(idColeira, anoMesReferencia, tipoCobertura, pageable);
    // }

    // public Integer countByColeiraAnoMesTipoCobertura(final Long idColeira, final AnoMesReferencia anoMesReferencia, final TipoCobertura tipoCobertura) {
    //     return repository.countUtilizacaoByColeiraAnoMesReferenciaTipoCobertura(idColeira, anoMesReferencia, tipoCobertura);
    // }
}
