package com.gabrielflores.myfortune.exception;

/**
 *
 * @Author: Gabriel Flores - gabrielmelloflores@gmail.com  
 */
public class EntityNotFoundException extends jakarta.persistence.EntityNotFoundException {

    private static final long serialVersionUID = 1351741147470064678L;

    private Long id;

    private Class<?> clazz;

    public EntityNotFoundException(String message) {
        super(message);
    }

    public EntityNotFoundException(Long id, String message) {
        this(message);
        this.id = id;
    }

    public EntityNotFoundException(Class<?> clazz, Long id, String message) {
        this(id, message);
        this.clazz = clazz;
    }

    public Long getId() {
        return id;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    @Override
    public String getMessage() {
        StringBuilder builder = new StringBuilder(super.getMessage());
        if (this.clazz != null) {
            builder.append(" => ");
            builder.append(this.clazz.getSimpleName());
        }
        if (this.id != null) {
            builder.append(": ");
            builder.append(this.id);
        }
        return builder.toString();
    }

}
