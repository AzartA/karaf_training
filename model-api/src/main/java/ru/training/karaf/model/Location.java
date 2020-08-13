package ru.training.karaf.model;

public interface Location extends Entity {
    long getPlanOid();
    String getPictureType();
    //ToDo
    //Set<? extends Sensor> getSensorSet();
}
