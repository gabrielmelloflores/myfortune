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
@Constraint(validatedBy = IsUniqueValidator.class)
@Target({TYPE, FIELD, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Repeatable(IsUnique.List.class)
public @interface IsUnique {

    String message() default "{constraints.is-unique}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String property();

    String repository();

    Action action() default Action.INSERT;

    @Documented
    @Target({TYPE, FIELD, ANNOTATION_TYPE})
    @Retention(RUNTIME)
    public @interface List {

        IsUnique[] value();

    }

    public static enum Action {
        INSERT,
        UPDATE
    }
}
