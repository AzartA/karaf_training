package ru.training.karaf.model;

import java.text.SimpleDateFormat;

public interface Measuring extends Entity {
    SimpleDateFormat DATE_TIME_FORMAT = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");

    java.sql.Timestamp getTimestamp();

    Entity getSensor();

    Entity getParameter();

    float getValue();
}
