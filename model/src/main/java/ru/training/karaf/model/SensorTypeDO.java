package ru.training.karaf.model;

import ru.training.karaf.converter.CapabilityConverter;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
public class SensorTypeDO implements SensorType {
    @Id
    @GeneratedValue
    private long id;
    @Column(length = 48)
    private String name;
    @Convert (converter = CapabilityConverter.class)
    @Column (columnDefinition = "character varying(255)")
    private Capability capability;
    @Column(name = "min_time")
    private int minTime;
    @OneToMany(mappedBy = "type", fetch = FetchType.EAGER)
    private Set<SensorDO> sensors;
    @ManyToMany
    @JoinTable(name="SENSOR_PARAMETER_SET")
    Set<ClimateParameterDO> parameters;

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }
    @Override
    public String getName() {
        return null;
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
