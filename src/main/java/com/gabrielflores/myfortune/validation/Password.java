package com.gabrielflores.myfortune.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Target;

/**
 *
 * @Author: Gabriel Flores - gabrielmelloflores@gmail.com  
 */
@Documented
@Constraint(validatedBy = PasswordValidator.class)
@Target({TYPE, FIELD, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Repeatable(Password.List.class)
public @interface Password {

    String message() default "{constraints.password}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    Action action() default Action.INSERT;

    String passwordField();

    @Documented
    @Target({TYPE, FIELD, ANNOTATION_TYPE})
    @Retention(RUNTIME)
    public @interface List {

        Password[] value();

    }

    public static enum Action {
        INSERT,
        UPDATE,
        RESET
    }
}
