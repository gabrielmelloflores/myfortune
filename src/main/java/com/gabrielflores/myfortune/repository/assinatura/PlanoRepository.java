package com.gabrielflores.myfortune.repository.assinatura;

import com.gabrielflores.myfortune.model.assinatura.Plano;
import com.gabrielflores.myfortune.model.dto.assinatura.PlanoDtoList;
import com.gabrielflores.myfortune.repository.BaseRepository;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 *
 * @author Douglas Pasqualin <douglas.pasqualin@gmail.com>
 */
public interface PlanoRepository extends BaseRepository<Plano> {

    Page<PlanoDtoList> findByAtivoTrue(Pageable pageable);

    Page<PlanoDtoList> findByTrialTrue(Pageable pageable);

    <T> Optional<T> findByTrialTrueAndAtivoTrue(Class<T> clazz);
}
