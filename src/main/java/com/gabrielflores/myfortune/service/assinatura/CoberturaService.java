package com.gabrielflores.myfortune.service.assinatura;

import com.gabrielflores.myfortune.model.assinatura.Cobertura;
import com.gabrielflores.myfortune.model.dto.assinatura.CoberturaDto;
import com.gabrielflores.myfortune.repository.assinatura.CoberturaRepository;
import com.gabrielflores.myfortune.service.BaseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 *
 * @author Douglas Pasqualin <douglas.pasqualin@gmail.com>
 */
@Service
public class CoberturaService extends BaseService<Cobertura, CoberturaRepository> {

    public Page<CoberturaDto> findByPlano(Long plano, Pageable pageable) {
        return repository.findAllByPlanoId(plano, pageable);
    }
}
