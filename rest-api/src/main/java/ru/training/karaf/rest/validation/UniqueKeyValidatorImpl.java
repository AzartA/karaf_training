package ru.training.karaf.rest.validation;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import ru.training.karaf.model.UniqueFieldEntity;
import ru.training.karaf.repo.UniqueValidationRepo;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import java.lang.reflect.Field;
import java.util.Optional;

public class UniqueKeyValidatorImpl implements ConstraintValidator<UniqueKey, UniqueFieldEntity> {
    private UniqueKey uniqueKey;
    private UniqueValidationRepo repo;
    //private BundleContext bundleContext;

    public UniqueKeyValidatorImpl(){
    }


    public void setRepo(UniqueValidationRepo repo) {
        this.repo = repo;
    }

    @Override
    public void initialize(UniqueKey uniqueKey) {
        this.uniqueKey = uniqueKey;
       // this.bundleContext = bundleContext;
       // ServiceReference<UniqueValidationRepo> sr = bundleContext.getServiceReference(UniqueValidationRepo.class);
       // repo = bundleContext.getService(sr);

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

            result = repo.presentObject(entityClass, fieldName, fieldValue);
            if(result.isPresent()){
                Field id = entityClass.getField("id");
                id.setAccessible(true);
                long idValue = id.getLong(target);

                Object entry = result.get();
                final Class<?> resultClass = entry.getClass();
                Field entryId = resultClass.getDeclaredField("id");
                entryId.setAccessible(true);
                long entryIdValue = entryId.getLong(entry);

                if(entryIdValue==idValue) return true;

                context.buildConstraintViolationWithTemplate(uniqueKey.message())
                        .addPropertyNode(uniqueKey.field())
                        .addConstraintViolation()
                        .disableDefaultConstraintViolation();
                return false;
            }
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
