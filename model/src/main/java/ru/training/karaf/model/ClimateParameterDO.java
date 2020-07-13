package ru.training.karaf.model;

import javax.persistence.*;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
public class ClimateParameterDO implements ClimateParameter{

    @Id
    @GeneratedValue
    private long id;
    @Column(name = "name", length = 48)
    private String name;
    @ManyToMany(mappedBy="parameters")
    private Set<SensorTypeDO> sensorTypes;
    @ManyToMany
    @JoinTable(name="PARAMETER_UNIT_SET")
    Set<UnitDO> units;
    @OneToMany(mappedBy = "parameter")
    private List<MeasuringDO> meashurings;

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
        return name;
    }
    public Set<SensorTypeDO> getSensorTypes() {
        return sensorTypes;
    }
    public void setSensorTypes(Set<SensorTypeDO> sensorTypes) {
        this.sensorTypes = sensorTypes;
    }
    public Set<UnitDO> getUnits() {
        return units;
    }
    public void setUnits(Set<UnitDO> units) {
        this.units = units;
    }
    public List<MeasuringDO> getMeashurings() {
        return meashurings;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ClimateParameterDO)) return false;
        ClimateParameterDO that = (ClimateParameterDO) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(id);
    }

    @Override
    public String toString() {
        String sensorTypeNames = "[" + sensorTypes.stream().map(SensorType::getName).collect(Collectors.joining(",")) + "]";
        String unitsNames = "[" + units.stream().map(Unit::getName).collect(Collectors.joining(",")) + "]";

        return "ClimateParameterDO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", sensorTypes=" + sensorTypes +
                ", units=" + units +
                '}';
    }
}
