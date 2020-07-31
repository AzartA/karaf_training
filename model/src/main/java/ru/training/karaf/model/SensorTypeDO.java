package ru.training.karaf.model;

import ru.training.karaf.converter.JsonbCapabilityConverter;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import java.util.Set;
import java.util.stream.Collectors;

@NamedQueries({
        @NamedQuery(name = UnitDO.GET_ALL, query = "SELECT u FROM UnitDO AS u"),
        @NamedQuery(name = UnitDO.GET_BY_NAME, query = "SELECT u FROM UnitDO AS u WHERE u.name = :name"),
        @NamedQuery(name = UnitDO.GET_BY_ID_OR_NAME, query = "SELECT u FROM UnitDO AS u WHERE u.id = :id OR u.name = :name")
})
@Entity
public class SensorTypeDO implements SensorType {
    public static final String GET_ALL = "SensorType.getAll";
    public static final String GET_BY_NAME = "SensorType.getByName";
    public static final String GET_BY_ID_OR_NAME = "SensorType.getByIdOrName";
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
    @OneToMany(mappedBy = "type")
    private Set<SensorDO> sensors;
    @ManyToMany
    @JoinTable(name = "SENSOR_PARAMETER_SET")
    private Set<ClimateParameterDO> parameters;

    public SensorTypeDO() {
    }

    public SensorTypeDO(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public SensorTypeDO(SensorType type) {
        this.id = type.getId();
        this.name = type.getName();
        this.capability = (CapabilityImpl) type.getCapability();
        this.minTime = type.getMinTime();
        //this.sensors = type.getSensors();
        this.parameters = (Set<ClimateParameterDO>) type.getParameters();
    }

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
    public CapabilityImpl getCapability() {
        return capability;
    }

    public void setCapability(CapabilityImpl capability) {
        this.capability = capability;
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
        return "SensorTypeDO{" +
                "id=" + id +
                ", name=" + name +
                ", capability=" + capability.toString() +
                ", minTime=" + minTime +
                ", parameters=" + parameterNames +
                ", sensors=" + sensorNames +
                '}';
    }
}
