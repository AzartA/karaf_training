package ru.training.karaf.rest.validation;

import java.util.List;
import javax.validation.ValidationProviderResolver;
import javax.validation.spi.ValidationProvider;

import org.hibernate.validator.HibernateValidator;

import static java.util.Collections.singletonList;

public class HibernateValidationProviderResolver implements ValidationProviderResolver {

    @Override
    public List<ValidationProvider<?>> getValidationProviders() {
        return singletonList(new HibernateValidator());
    }
}
