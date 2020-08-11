package ru.training.karaf.model;

public interface Location {
    long getId();
    String getName();
    long getPlanOid();
    String getPictureType();
    //ToDo
    //Set<? extends Sensor> getSensorSet();
}
