package ru.training.karaf.model;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
public class SensorTypeDO implements SensorType {
    @Id
    @GeneratedValue
    private Long id;
    @Column(length = 48)
    private String name;
    @Column (length = 48)
    private String range;
    @Column(name = "min_time")
    private int minTime;
    @OneToMany(mappedBy = "type", fetch = FetchType.EAGER)
    private Set<SensorDO> sensors;
    @ManyToMany
    @JoinTable(name="SENSOR_PARAMETER_SET")
    Set<ClimateParameterDO> parameters;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }
    @Override
    public String getName() {
        return null;
    }
    public String getRange() {
        return range;
    }
    public void setRange(String range) {
        this.range = range;
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
    /*public void setSensorSet(Set<SensorDO> sensorSet) {
        this.sensorSet = sensorSet;
    }*/
    public Set<ClimateParameterDO> getParameters() {
        return parameters;
    }
    public void setParameters(Set<ClimateParameterDO> parameters) {
        this.parameters = parameters;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id,name,range, minTime, parameters);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SensorTypeDO)) return false;

        SensorTypeDO that = (SensorTypeDO) o;

        if (minTime != that.minTime) return false;
        if (!id.equals(that.id)) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (range != null ? !range.equals(that.range) : that.range != null) return false;
        return parameters != null ? parameters.equals(that.parameters) : that.parameters == null;
    }

    @Override
    public String toString() {
        String parameterNames = "[" + parameters.stream().map(ClimateParameter::getName).collect(Collectors.joining(",")) + "]";
        String sensorNames = "[" + sensors.stream().map(Sensor::getName).collect(Collectors.joining(",")) + "]";
        return "SensorTypeDO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", range='" + range + '\'' +
                ", minTime=" + minTime +
                ", parameters=" + parameterNames +
                ", sensors=" + sensorNames +
                '}';
    }
}
