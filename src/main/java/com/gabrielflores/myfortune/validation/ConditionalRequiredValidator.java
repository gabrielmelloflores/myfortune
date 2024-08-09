package com.gabrielflores.myfortune.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Objects;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.ObjectUtils;

/**
 *
 * @Author: Gabriel Flores - gabrielmelloflores@gmail.com  
 */
public class ConditionalRequiredValidator implements ConstraintValidator<ConditionalRequired, Object> {

    private String fieldName;
    private String fieldValue;
    private String[] requiredFields;

    @Override
    public void initialize(ConditionalRequired constraintAnnotation) {
        this.fieldName = constraintAnnotation.field();
        this.fieldValue = constraintAnnotation.value();
        this.requiredFields = constraintAnnotation.required();
    }

    @Override
    public boolean isValid(Object objectToValidate, ConstraintValidatorContext context) {
        context.disableDefaultConstraintViolation();
        boolean valid = true;
        try {
            String actualValue = BeanUtils.getProperty(objectToValidate, this.fieldName);
            if (Objects.equals(actualValue, this.fieldValue)) {
                for (String requiredField : this.requiredFields) {
                    Object requiredValue = PropertyUtils.getProperty(objectToValidate, requiredField);
                    if (ObjectUtils.isEmpty(requiredValue)) {
                        valid = false;
                        context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                                .addPropertyNode(requiredField).addConstraintViolation();
                    }
                }
            }
        } catch (Exception ex) {
            context.buildConstraintViolationWithTemplate(ex.getLocalizedMessage())
                    .addPropertyNode(fieldName).addConstraintViolation();
            valid = false;
        }
        return valid;
    }
}
