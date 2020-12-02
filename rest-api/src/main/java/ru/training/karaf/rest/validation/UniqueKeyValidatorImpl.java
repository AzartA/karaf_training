package ru.training.karaf.rest.validation;

import java.lang.reflect.Field;
import java.util.Optional;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import ru.training.karaf.model.UniqueFieldEntity;

public class UniqueKeyValidatorImpl implements ConstraintValidator<UniqueKey, UniqueFieldEntity> {
    private UniqueKey uniqueKey;

    public UniqueKeyValidatorImpl() {
    }

    @Override
    public void initialize(UniqueKey uniqueKey) {
        this.uniqueKey = uniqueKey;
    }

    @Override
    public boolean isValid(UniqueFieldEntity target, ConstraintValidatorContext context) {
        Optional<Object> result;
        final Class<?> entityClass = target.getClass();

        try {
            String fieldName = uniqueKey.field();
            Field field = entityClass.getDeclaredField(fieldName);
            field.setAccessible(true);
            //ToDo what about Int, Double?
            Object fieldValue = field.get(target);
            return true;
        } catch (Exception e) {
            throw new RuntimeException(
                    "An error occurred when trying to create the jpa predicate for the @UniqueKey '"
                            + uniqueKey.field()
                            + "' on bean "
                            + entityClass + ".", e);
        }
    }
}
