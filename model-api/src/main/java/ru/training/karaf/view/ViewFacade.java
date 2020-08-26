package ru.training.karaf.view;

public interface ViewFacade {
    <T extends ViewType> T getView(Class<T> type);
}
