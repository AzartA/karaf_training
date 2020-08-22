package ru.training.karaf.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;



@Constraint(validatedBy = FilterParamsValidatorImpl.class)
@Target({ ElementType.METHOD, ElementType.CONSTRUCTOR, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface ConformingFilterParams {
    String message() default "The condition is inapplicable for the field and the value";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
