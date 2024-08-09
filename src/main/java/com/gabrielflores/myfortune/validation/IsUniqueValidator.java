package com.gabrielflores.myfortune.validation;

import com.gabrielflores.myfortune.model.entity.Entidade;
import com.gabrielflores.myfortune.repository.BaseRepository;
import com.gabrielflores.myfortune.util.TextUtils;
import com.gabrielflores.myfortune.validation.IsUnique.Action;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.lang.reflect.InvocationTargetException;
import java.util.Optional;
import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class IsUniqueValidator implements ConstraintValidator<IsUnique, Object> {

    private Action action;
    private String propertyName;
    private String repositoryName;

    private final ApplicationContext applicationContext;

    public IsUniqueValidator(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public void initialize(final IsUnique constraintAnnotation) {
        action = constraintAnnotation.action();
        propertyName = constraintAnnotation.property();
        repositoryName = constraintAnnotation.repository();
    }

    @Override
    public boolean isValid(final Object objectToValidate, final ConstraintValidatorContext context) {
        context.disableDefaultConstraintViolation();
        try {

            Object propertyValue = PropertyUtils.getProperty(objectToValidate, propertyName);
            if (propertyValue == null) {
                return true;
            }

            BaseRepository<? extends Entidade> repository = (BaseRepository<? extends Entidade>) applicationContext.getBean(repositoryName);
            Object result = repository.getClass().getMethod("findBy" + TextUtils.capitalize(propertyName), propertyValue.getClass()).invoke(repository, propertyValue);

            if (result instanceof Optional<?> opt) {
                result = opt.orElse(null);
            }

            boolean valid;
            if (result == null) {
                valid = true;
            } else if (action == Action.INSERT) {
                valid = false;
            } else {
                Long resultId = ((Entidade) result).getId();
                Long objId = (Long) PropertyUtils.getProperty(objectToValidate, "id");
                valid = resultId.equals(objId);
            }
            if (!valid) {
                context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                        .addPropertyNode(propertyName).addConstraintViolation();
            }
            return valid;
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | BeansException e) {
            context.buildConstraintViolationWithTemplate(e.getLocalizedMessage())
                    .addPropertyNode(propertyName).addConstraintViolation();
            return false;
        }
    }
}
