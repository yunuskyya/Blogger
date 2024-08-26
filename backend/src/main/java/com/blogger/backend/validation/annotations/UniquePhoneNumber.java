package com.blogger.backend.validation.annotations;

import com.blogger.backend.validation.validators.UniquePhoneNumberValidator;
import jakarta.validation.Constraint;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = UniquePhoneNumberValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface UniquePhoneNumber {
    String message() default "{blogger.validation.notunique.phonenumber}";

    Class<?>[] groups() default {};

    Class<?>[] payload() default {};
}
