package ru.training.karaf.validation;

import org.hibernate.validator.HibernateValidator;

import javax.validation.ValidationProviderResolver;
import javax.validation.spi.ValidationProvider;
import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.singletonList;

public class OSGIServiceDiscover implements ValidationProviderResolver {
    @Override
    public List<ValidationProvider<?>> getValidationProviders() {
        return singletonList(new HibernateValidator());
    }
}
