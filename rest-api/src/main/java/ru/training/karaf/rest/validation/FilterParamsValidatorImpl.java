package ru.training.karaf.rest.validation;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.sql.Timestamp;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.constraintvalidation.SupportedValidationTarget;
import javax.validation.constraintvalidation.ValidationTarget;

@SupportedValidationTarget(ValidationTarget.PARAMETERS)
public class FilterParamsValidatorImpl implements ConstraintValidator<ConformingParams, Object[]> {
    @Override
    public void initialize(ConformingParams conformingParams) {

    }

    @Override
    public boolean isValid(Object[] strings, ConstraintValidatorContext constraintValidatorContext) {
        Class<?> type;
        //leave null-checking to @NotNull on individual parameters
        if (strings.length == 4) {
            if (strings[0] == null || strings[1] == null || strings[2] == null) {
                return true;
            }
            type = (Class<?>) strings[3];
        } else {
            if (strings[0] == null || strings[1] == null) {
                return true;
            }
            type = (Class<?>) strings[2];
        }

        String[] fieldParts = strings[0].toString().split("\\.");
        String value = strings[2].toString();
        String cond = strings[1].toString();
        for (String fieldPart : fieldParts) {
            //verification
            try {
                if (fieldPart.endsWith("s")) {
                    Field fld = type.getDeclaredField(fieldPart);
                    ParameterizedType pType = (ParameterizedType) fld.getGenericType();
                    type = (Class<?>) pType.getActualTypeArguments()[0];
                } else {
                    type = type.getDeclaredField(fieldPart).getType();
                }
            } catch (NoSuchFieldException e) {
                return false;
            }
        }
        if (strings.length == 4) {
            if (cond.matches("!?contains") && !type.equals(String.class)) {
                return false;
            }
            return type.equals(String.class) || type.equals(Timestamp.class) || value.matches("^-?\\d+(?:\\.\\d+)?$");
        }

        return true;
    }
}
