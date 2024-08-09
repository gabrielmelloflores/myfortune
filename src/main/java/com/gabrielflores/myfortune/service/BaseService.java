package com.gabrielflores.myfortune.service;

import com.gabrielflores.myfortune.exception.EntityNotFoundException;
import com.gabrielflores.myfortune.model.entity.Entidade;
import com.gabrielflores.myfortune.repository.BaseRepository;
import jakarta.transaction.Transactional;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 *
 * @Author: Gabriel Flores - gabrielmelloflores@gmail.com  
 * @param <E> Entity type
 * @param <R> Repository type
 */
public abstract class BaseService<E extends Entidade, R extends BaseRepository<E>> {

    @Autowired
    protected R repository;

    protected final Class<E> persistentClass;

    @SuppressWarnings("unchecked")
    protected BaseService() {
        final Type genericSuperclass = getClass().getGenericSuperclass();
        if (genericSuperclass instanceof ParameterizedType parameterizedType) {
            final Type[] types = parameterizedType.getActualTypeArguments();
            final Type typeE = types[0];
            this.persistentClass = typeE instanceof ParameterizedType pTypeE ? (Class<E>) pTypeE.getRawType() : (Class<E>) typeE;
        } else {
            this.persistentClass = null;
        }
    }

    public E getById(Long id) throws EntityNotFoundException {
        return findById(id).orElseThrow(() -> new EntityNotFoundException(persistentClass, id, "Entidade não encontrada"));
    }

    public <T> T getById(Long id, Class<T> clazz) throws EntityNotFoundException {
        return findById(id, clazz).orElseThrow(() -> new EntityNotFoundException(persistentClass, id, "Entidade não encontrada"));
    }

    public Optional<E> findById(Long id) {
        return repository.findById(id);
    }

    public <T> Optional<T> findById(Long id, Class<T> clazz) {
        return repository.findById(id, clazz);
    }

    public List<E> findAll() {
        return repository.findAll();
    }

    public Page<E> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public <T> Page<T> findAll(Pageable pageable, Class<T> clazz) {
        return repository.findAll(pageable, clazz);
    }

    public List<E> findAllById(List<Long> ids) {
        return repository.findAllById(ids);
    }

    public <T> List<T> findAllById(List<Long> ids, Class<T> clazz) {
        return repository.findAllById(ids, clazz);
    }

    @Transactional
    public E save(E entity) {
        return repository.save(entity);
    }

    @Transactional
    public List<E> saveAll(List<E> entities) {
        return repository.saveAll(entities);
    }

    @Transactional
    public E update(E entity) {
        return repository.save(entity);
    }

    @Transactional
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Transactional
    public void delete(E entity) {
        repository.delete(entity);
    }
}
