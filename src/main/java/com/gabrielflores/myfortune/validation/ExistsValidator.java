package com.gabrielflores.myfortune.validation;

import com.gabrielflores.myfortune.model.entity.Entidade;
import com.gabrielflores.myfortune.repository.BaseRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.lang.reflect.InvocationTargetException;
import java.util.Optional;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class ExistsValidator implements ConstraintValidator<Exists, Object> {

    private String propertyName;
    private String repositoryName;

    private final ApplicationContext applicationContext;

    public ExistsValidator(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public void initialize(final Exists constraintAnnotation) {
        propertyName = constraintAnnotation.property();
        repositoryName = constraintAnnotation.repository();
    }

    @Override
    public boolean isValid(final Object objectToValidate, final ConstraintValidatorContext context) {
        context.disableDefaultConstraintViolation();
        try {
            final String propertyValue = BeanUtils.getProperty(objectToValidate, propertyName);
            if (propertyValue == null) {
                return true;
            }

            BaseRepository<? extends Entidade> repository = (BaseRepository<? extends Entidade>) applicationContext.getBean(repositoryName);
            Optional<? extends Entidade> result = repository.findById(Long.valueOf(propertyValue));

            boolean valid = result != null && result.isPresent();
            if (!valid) {
                context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                        .addPropertyNode(propertyName).addConstraintViolation();
            }
            return valid;
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | NumberFormatException | BeansException e) {
            context.buildConstraintViolationWithTemplate(e.getLocalizedMessage())
                    .addPropertyNode(propertyName).addConstraintViolation();
            return false;
        }
    }
}
