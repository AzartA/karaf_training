package ru.training.karaf.view;

import ru.training.karaf.model.Entity;

public interface ViewType {
    Class<? extends Entity> getType();
    Class <? extends ViewType> getServiceClass();

}
