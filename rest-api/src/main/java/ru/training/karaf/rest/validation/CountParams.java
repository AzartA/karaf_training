package ru.training.karaf.rest.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Constraint(validatedBy = ParamsCountValidatorImpl.class)
@Target({ElementType.METHOD, ElementType.CONSTRUCTOR, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface CountParams {
    String message() default "The count of parameters is different";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
