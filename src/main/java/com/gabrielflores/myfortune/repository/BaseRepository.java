package com.gabrielflores.myfortune.repository;

import com.gabrielflores.myfortune.model.entity.Entidade;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;

/**
 *
 * @Author: Gabriel Flores - gabrielmelloflores@gmail.com  
 * @param <E> Entity type
 */
@NoRepositoryBean
public interface BaseRepository<E extends Entidade> extends JpaRepository<E, Long> {

    @Query("select e from #{#entityName} e where e.id = :id")
    <T> Optional<T> findById(Long id, Class<T> type);

    @Query("select e from #{#entityName} e")
    <T> List<T> findAll(Class<T> type);

    @Query("select e from #{#entityName} e")
    <T> Page<T> findAll(Pageable pageable, Class<T> type);

    @Query("select e from #{#entityName} e where e.id in (:ids)")
    <T> List<T> findAllById(List<Long> ids, Class<T> clazz);

}
