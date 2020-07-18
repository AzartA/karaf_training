package ru.training.karaf.model;

import ru.training.karaf.converter.JsonbCapabilityConverter;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
public class SensorTypeDO implements SensorType {

    @Id
    @GeneratedValue
    private long id;
    @Column(length = 48)
    private String name;
    @Convert(converter = JsonbCapabilityConverter.class)
    @Column(columnDefinition = "jsonb")
    private Capability capability;
    @Column(name = "min_time")
    private int minTime;
    @OneToMany(mappedBy = "type", fetch = FetchType.EAGER)
    private Set<SensorDO> sensors;
    @ManyToMany
    @JoinTable(name = "SENSOR_PARAMETER_SET")
    Set<ClimateParameterDO> parameters;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return null;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMinTime() {
        return minTime;
    }

    public void setMinTime(int minTime) {
        this.minTime = minTime;
    }

    public Set<SensorDO> getSensors() {
        return sensors;
    }

    public void setSensors(Set<SensorDO> sensors) {
        this.sensors = sensors;
    }

    public Set<ClimateParameterDO> getParameters() {
        return parameters;
    }

    public void setParameters(Set<ClimateParameterDO> parameters) {
        this.parameters = parameters;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SensorTypeDO)) return false;
        SensorTypeDO that = (SensorTypeDO) o;
        return id == that.id;
    }

    @Override
    public String toString() {
        String parameterNames = "[" + parameters.stream().map(ClimateParameter::getName).collect(Collectors.joining(",")) + "]";
        String sensorNames = "[" + sensors.stream().map(Sensor::getName).collect(Collectors.joining(",")) + "]";
        return "SensorTypeDO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", minTime=" + minTime +
                ", parameters=" + parameterNames +
                ", sensors=" + sensorNames +
                '}';
    }
}
