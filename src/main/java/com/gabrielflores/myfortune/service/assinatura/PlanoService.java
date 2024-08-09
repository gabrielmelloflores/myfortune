package com.gabrielflores.myfortune.service.assinatura;

import com.gabrielflores.myfortune.model.assinatura.Plano;
import com.gabrielflores.myfortune.model.dto.assinatura.PlanoDtoList;
import com.gabrielflores.myfortune.repository.assinatura.PlanoRepository;
import com.gabrielflores.myfortune.service.BaseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Douglas Pasqualin <douglas.pasqualin@gmail.com>
 */
@Service
public class PlanoService extends BaseService<Plano, PlanoRepository> {

    @Transactional
    @Override
    public Plano save(Plano entity) {
        Plano plano = super.save(entity);
        /* final String idPagSeguro = pagSeguroService.criaNovoPlano(plano);
        plano.setIdExterno(idPagSeguro);
        return super.update(plano);*/
        return plano;
    }

    @Transactional
    @Override
    public Plano update(Plano entity) {
        final Plano plano = super.update(entity);
        // pagSeguroService.atualizaPlano(plano);
        return plano;
    }

    @Transactional
    public Plano reativa(Plano plano) {
        plano.reativa();
        plano = super.update(plano);
        //pagSeguroService.ativaPlano(plano);
        return plano;
    }

    @Transactional
    public Plano inativa(Plano plano) {
        plano.inativa();
        plano = super.update(plano);
        //pagSeguroService.inativaPlano(plano);
        return plano;
    }

    public Page<PlanoDtoList> findAllAtivos(final Pageable pageable) {
        return repository.findByAtivoTrue(pageable);
    }

    public Page<PlanoDtoList> findAllTrials(final Pageable pageable) {
        return repository.findByTrialTrue(pageable);
    }

    public <T> T findPlanoTrial(Class<T> clazz) {
        return repository.findByTrialTrueAndAtivoTrue(clazz).orElse(null);
    }

    public Plano getPlanoTrial() {
        return this.findPlanoTrial(Plano.class);
    }

}
