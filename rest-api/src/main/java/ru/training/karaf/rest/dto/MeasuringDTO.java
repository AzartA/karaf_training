package ru.training.karaf.rest.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ru.training.karaf.model.Measuring;

import javax.validation.constraints.Digits;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MeasuringDTO implements Measuring {
    @JsonIgnore
    private final SimpleDateFormat DATE_TIME_FORMAT = Measuring.DATE_TIME_FORMAT;//new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");
    @JsonIgnore
    private String name;
    private long id;
    private java.sql.Timestamp timestamp;
    private EntityDTO sensor;
    private EntityDTO parameter;
    private float value;

    public MeasuringDTO() {
    }

    public MeasuringDTO(Measuring measuring) {
        id = measuring.getId();
        timestamp = measuring.getTimestamp();
        sensor = measuring.getSensor() == null ? null : new EntityDTO(measuring.getSensor());
        parameter = measuring.getParameter() == null ? null : new EntityDTO(measuring.getParameter());
        value = measuring.getValue();
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public String getName() {
        return Long.toString(id);
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public EntityDTO getSensor() {
        return sensor;
    }

    public void setSensor(EntityDTO sensor) {
        this.sensor = sensor;
    }

    @Override
    public EntityDTO getParameter() {
        return parameter;
    }

    public void setParameter(EntityDTO parameter) {
        this.parameter = parameter;
    }

    @Override
    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MeasuringDTO)) {
            return false;
        }

        MeasuringDTO that = (MeasuringDTO) o;

        return id == that.id;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

    @Override
    public String toString() {
        Date date = new Date();
        if (timestamp != null) {
            date.setTime(timestamp.getTime());
        }
        return "MeasuringDTO{" +
                "id=" + id +
                ", timestamp=" + DATE_TIME_FORMAT.format(date) +
                ", sensor=" + (sensor == null ? "null" : sensor.getName()) +
                ", parameter=" + (parameter == null ? "null" : parameter.getName()) +
                ", value=" + value +
                '}';
    }
}
