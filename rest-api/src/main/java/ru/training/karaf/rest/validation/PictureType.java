package ru.training.karaf.rest.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {PictureTypeValidatorImpl.class})
public @interface PictureType {
    String message() default "The pictureType is unknown";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}