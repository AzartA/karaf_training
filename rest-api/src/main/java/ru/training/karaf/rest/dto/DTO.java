package ru.training.karaf.rest.dto;

public class DTO<T> {
    private T value;

    public DTO(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }
}
