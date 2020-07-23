package ru.training.karaf.repo;

import java.util.Optional;

public interface UniqueValidationRepo {
    Optional<Object> presentObject(Class<?> entityClass, String filedName, Object fieldValue);
}
