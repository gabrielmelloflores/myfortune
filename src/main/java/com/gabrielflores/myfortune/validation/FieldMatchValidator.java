package com.gabrielflores.myfortune.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Objects;
import org.apache.commons.beanutils.PropertyUtils;

public class FieldMatchValidator implements ConstraintValidator<FieldMatch, Object> {

    private String firstFieldName;
    private String secondFieldName;

    @Override
    public void initialize(final FieldMatch constraintAnnotation) {
        firstFieldName = constraintAnnotation.first();
        secondFieldName = constraintAnnotation.second();
    }

    @Override
    public boolean isValid(final Object objectToValidate, final ConstraintValidatorContext context) {
        context.disableDefaultConstraintViolation();
        try {
            final Object firstObj = PropertyUtils.getProperty(objectToValidate, firstFieldName);
            final Object secondObj = PropertyUtils.getProperty(objectToValidate, secondFieldName);
            final boolean valid = Objects.equals(firstObj, secondObj);
            if (!valid) {
                context
                        .buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                        .addPropertyNode(firstFieldName).addConstraintViolation()
                        .buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                        .addPropertyNode(secondFieldName).addConstraintViolation();
            }
            return valid;
        } catch (Exception ignore) {
        }
        return true;
    }
}
