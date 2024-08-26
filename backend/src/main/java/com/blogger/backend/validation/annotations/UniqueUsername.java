package com.blogger.backend.validation.annotations;

import com.blogger.backend.validation.validators.UniqueUsernameValidator;
import jakarta.validation.Constraint;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = UniqueUsernameValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueUsername {
    String message() default "{blogger.validation.notunique.username}";

    Class<?>[] groups() default {};

    Class<?>[] payload() default {};
}
