package com.gabrielflores.myfortune.repository.assinatura;

import com.gabrielflores.myfortune.dto.assinatura.CupomDtoCadastro;
import com.gabrielflores.myfortune.model.assinatura.CupomDesconto;
import com.gabrielflores.myfortune.model.dto.assinatura.CupomDescontoDto;
import com.gabrielflores.myfortune.repository.BaseRepository;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

/**
 *
 * @author Douglas Pasqualin <douglas.pasqualin@gmail.com>
 */
public interface CupomDescontoRepository extends BaseRepository<CupomDesconto> {

    @Query("select c from #{#entityName} c where c.codigo = :codigo")
    <T> Optional<T> findByCodigo(String codigo, Class<T> type);

    Optional<CupomDtoCadastro> findByCodigoIgnoreCase(String descricao);

    Page<CupomDescontoDto> findByCodigoContainingIgnoreCase(String codigo, Pageable pageable);
}
