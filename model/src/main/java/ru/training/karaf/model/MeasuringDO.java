package ru.training.karaf.model;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@Cacheable(value = false)
@Entity
public class MeasuringDO implements Measuring {
    @Transient
    private final SimpleDateFormat DATE_TIME_FORMAT = Measuring.DATE_TIME_FORMAT;//new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");
    @Id
    @GeneratedValue
    private long id;
    @Column(name = "measured_on", insertable = false, updatable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP")
    private java.sql.Timestamp timestamp;
    @ManyToOne(optional = false, fetch= FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "sensor")
    private SensorDO sensor;
    @ManyToOne(optional = false, fetch= FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "parameter")
    private ClimateParameterDO parameter;
    @Column(columnDefinition = "real")
    private float value;

    public MeasuringDO() {
    }

    public MeasuringDO(float value) {
        this.value = value;
    }

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
        if (!(o instanceof MeasuringDO)) {
            return false;
        }
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
        if(timestamp!=null) {
            date.setTime(timestamp.getTime());
        }
        return "MeasuringDO{" +
                "id=" + id +
                ", timestamp=" + DATE_TIME_FORMAT.format(date) +
                ", sensor=" + (sensor==null?"null":sensor.getName()) +
                ", parameter=" + (parameter==null?"null":parameter.getName()) +
                ", value=" + value +
                '}';
    }
}
