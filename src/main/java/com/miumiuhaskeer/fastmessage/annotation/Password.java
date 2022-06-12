package com.miumiuhaskeer.fastmessage.annotation;

import com.miumiuhaskeer.fastmessage.validator.PasswordValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordValidator.class)
public @interface Password {

    String message() default "{error.constraintViolationException.invalid.password}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
