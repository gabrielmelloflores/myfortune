package com.gabrielflores.myfortune.service.assinatura;

import com.gabrielflores.myfortune.exception.EntityNotFoundException;
import com.gabrielflores.myfortune.exception.InvalidDataException;
import com.gabrielflores.myfortune.model.assinatura.CupomDesconto;
import com.gabrielflores.myfortune.model.dto.assinatura.CupomDescontoDto;
import com.gabrielflores.myfortune.repository.assinatura.CupomDescontoRepository;
import com.gabrielflores.myfortune.service.BaseService;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Douglas Pasqualin <douglas.pasqualin@gmail.com>
 */
@Service
public class CupomDescontoService extends BaseService<CupomDesconto, CupomDescontoRepository> {

    public Optional<CupomDesconto> findByCodigo(String codigo) {
        return findByCodigo(codigo, CupomDesconto.class);
    }

    public <T> Optional<T> findByCodigo(String codigo, Class<T> clazz) {
        return repository.findByCodigo(codigo.toUpperCase().trim(), clazz);
    }

    public <T> T getByCodigo(String codigo, Class<T> clazz) throws EntityNotFoundException {
        return repository.findByCodigo(codigo, clazz).orElseThrow(() -> new InvalidDataException("Cupom inválido: " + codigo));
    }

    public CupomDesconto getByCodigo(String codigo) throws EntityNotFoundException {
        return getByCodigo(codigo, CupomDesconto.class);
    }

    public Page<CupomDescontoDto> findAllByCodigo(final String codigo, final Pageable pageable) {
        return repository.findByCodigoContainingIgnoreCase(codigo, pageable);
    }

    @Transactional
    public synchronized CupomDesconto utilizaCupom(CupomDesconto cupom) {
        cupom.utiliza();
        //Para garantir, vamos ver se utilizando o cupom ele ainda segue váido (não ficou negativa a quantidade)
        if (cupom.getDisponivel() != null && cupom.getUtilizado() > cupom.getDisponivel()) {
            throw new InvalidDataException("Erro ao utilizar o cupom: quantidade não disponível");
        }
        return super.update(cupom);
    }

    @Transactional
    @Override
    public CupomDesconto update(CupomDesconto cupom) {
        validaCupom(cupom);
        /*final String idPagSeguro = pagSeguroAssinaturaService.atualizaCupom(cupom);
        cupom.setIdExterno(idPagSeguro);*/
        return super.update(cupom);
    }

    @Transactional
    @Override
    public CupomDesconto save(CupomDesconto entity) {
        validaCupom(entity);
        final CupomDesconto cupom = super.save(entity);
        /*final String idPagSeguro = pagSeguroAssinaturaService.criaNovoCupom(cupom);
        cupom.setIdExterno(idPagSeguro);
        return super.update(cupom);*/
        return cupom;
    }

    @Transactional
    @Override
    public void delete(CupomDesconto entity) {
        super.delete(entity);
        //pagSeguroAssinaturaService.inativaCupom(entity);
    }

    private void validaCupom(final CupomDesconto cupom) {
        final Optional<CupomDesconto> cupomSalvo = findByCodigo(cupom.getCodigo());
        if (cupomSalvo.isPresent() && !cupomSalvo.get().getId().equals(cupom.getId())) {
            throw new InvalidDataException("Código " + cupom.getCodigo() + " já cadastrado");
        }
    }

}
