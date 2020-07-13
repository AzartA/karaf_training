package ru.training.karaf.model;

import javax.persistence.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.stream.Collectors;

@Entity
public class MeasuringDO implements  Measuring{
    @Transient
    private final SimpleDateFormat DATE_TIME_FORMAT = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");

    @Id
    @GeneratedValue
    private long id;
    @Column(name = "measured_on", columnDefinition = "TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP")
    private java.sql.Timestamp timestamp;
    @ManyToOne (optional = false)
    @JoinColumn(name = "sensor")
    private SensorDO sensor;
    @ManyToOne (optional = false)
    @JoinColumn(name = "parameter")
    private ClimateParameterDO parameter;
    @Column(columnDefinition = "real")
    float value;

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    @Override
    public Timestamp getTimestamp() {
        return timestamp;
    }
    public SensorDO getSensor() {
        return sensor;
    }
    public void setSensor(SensorDO sensor) {
        this.sensor = sensor;
    }
    public ClimateParameterDO getParameter() {
        return parameter;
    }
    public void setParameter(ClimateParameterDO parameter) {
        this.parameter = parameter;
    }
    public void setValue(float value) {
        this.value = value;
    }
    @Override
    public float getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MeasuringDO)) return false;
        MeasuringDO that = (MeasuringDO) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(id);
    }

    @Override
    public String toString() {
        Date date = new Date();
        date.setTime(timestamp.getTime());
        return "MeasuringDO{" +
                "id=" + id +
                ", timestamp=" + DATE_TIME_FORMAT.format(date) +
                ", sensor=" + sensor.getName() +
                ", parameter=" + parameter.getName() +
                ", value=" + value +
                '}';
    }
}
