package ru.training.karaf.rest.validation;

import java.util.ArrayList;
import java.util.List;

public class ErrorsDTO {

    private List<String> errors;

    public ErrorsDTO(List<String> errors) {
        this.errors = errors;
    }

    public ErrorsDTO(String error) {
        errors = new ArrayList<>();
        errors.add(error);
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }
}

