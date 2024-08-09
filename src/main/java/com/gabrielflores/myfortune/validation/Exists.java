package com.gabrielflores.myfortune.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import static java.lang.annotation.ElementType.*;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = ExistsValidator.class)
@Target({TYPE, FIELD, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Repeatable(Exists.List.class)
public @interface Exists {

    String message() default "{constraints.exists}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String property();

    String repository();

    @Documented
    @Target({TYPE, FIELD, ANNOTATION_TYPE})
    @Retention(RUNTIME)
    public @interface List {

        Exists[] value();
    }
}
