package com.example.BoatRegistry.validators;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = NotBlankIfPresentValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface NotBlankIfPresent {

    String message() default "Must not be blank if provided";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
