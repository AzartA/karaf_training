package ru.training.karaf.model;

import ru.training.karaf.converter.JsonbCapabilityConverter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
public class SensorTypeDO implements SensorType, Serializable {
    private static final long serialVersionUID = 5474563217896L;

    @Id
    @GeneratedValue
    private long id;

    @Column(length = 48)
    private String name;
    
    @Convert(converter = JsonbCapabilityConverter.class)
    @Column(columnDefinition = "jsonb")
    private CapabilityImpl capability;

    @Column(name = "min_time")
    private int minTime;

    @OneToMany(mappedBy = "type", cascade = {CascadeType.ALL})
    private Set<SensorDO> sensors;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "SENSOR_PARAMETER_SET")
    private Set<ClimateParameterDO> parameters;

    public SensorTypeDO() {
    }

    public SensorTypeDO(String name) {
        this.name = name;
    }

    public SensorTypeDO(long id, String name, CapabilityImpl capability) {
        this.id = id;
        this.name = name;
        this.capability = capability;
    }

    public SensorTypeDO(SensorType type) {
        this.name = type.getName();
        this.minTime = type.getMinTime();
        this.capability = type.getCapability() == null ? null : new CapabilityImpl(type.getCapability());
        parameters = new HashSet<>();
        sensors = new HashSet<>();
    }

    public void update(SensorType type) {
        this.name = type.getName();
        this.minTime = type.getMinTime();
        this.capability = type.getCapability() == null ? null : new CapabilityImpl(type.getCapability());
        parameters = new HashSet<>();
        sensors = new HashSet<>();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMinTime() {
        return minTime;
    }

    public Set<SensorDO> getSensors() {
        return sensors;
    }

    public Set<ClimateParameterDO> getParameters() {
        return parameters;
    }

    public void setParameters(Set<ClimateParameterDO> parameters) {
        this.parameters = parameters;
    }

    public void addParameters(Set<ClimateParameterDO> parameters) {
        this.parameters.addAll(parameters);
        parameters.forEach(u -> u.getSensorTypes().add(this));
    }

    @Override
    public CapabilityImpl getCapability() {
        return capability;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SensorTypeDO)) {
            return false;
        }
        SensorTypeDO that = (SensorTypeDO) o;
        return id == that.id;
    }

    @Override
    public String toString() {
        String parameterNames = "[" + parameters.stream().map(ClimateParameter::getName).collect(Collectors.joining(",")) + "]";
        String sensorNames = "[" + sensors.stream().map(Sensor::getName).collect(Collectors.joining(",")) + "]";
        return "SensorTypeDO{"
                + "id=" + id
                + ", name=" + name
                + ", capability=" + capability.toString()
                + ", minTime=" + minTime
                + ", parameters=" + parameterNames
                + ", sensors=" + sensorNames
                + '}';
    }
}
