package com.gabrielflores.myfortune.repository.assinatura;

import com.gabrielflores.myfortune.model.assinatura.Cobertura;
import com.gabrielflores.myfortune.model.dto.assinatura.CoberturaDto;
import com.gabrielflores.myfortune.repository.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 *
 * @author Douglas Pasqualin <douglas.pasqualin@gmail.com>
 */
public interface CoberturaRepository extends BaseRepository<Cobertura> {

    Page<CoberturaDto> findAllByPlanoId(Long plano, Pageable pageable);
}
