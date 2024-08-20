package com.blogger.backend.validation.annotations;

import com.blogger.backend.validation.validators.UniqueEmailValidator;
import jakarta.validation.Constraint;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = UniqueEmailValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueEmail {
    String message() default "{blogger.validation.notunique.email}";

    Class<?>[] groups() default {};

    Class<?>[] payload() default {};
}
