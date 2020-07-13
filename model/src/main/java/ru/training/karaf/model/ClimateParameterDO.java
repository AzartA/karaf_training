package ru.training.karaf.model;

import javax.persistence.*;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
public class ClimateParameterDO implements ClimateParameter{

    @Id
    @GeneratedValue
    private Long id;
    @Column(name = "name", length = 48)
    private String name;
    @ManyToMany(mappedBy="parameters")
    private Set<SensorTypeDO> sensorTypes;
    @ManyToMany
    @JoinTable(name="PARAMETER_UNIT_SET")
    Set<UnitDO> units;
    @OneToMany(mappedBy = "parameter")
    private Set<MeasuringDO> meashurings;

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
    public Set<MeasuringDO> getMeashurings() {
        return meashurings;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ClimateParameterDO)) return false;

        ClimateParameterDO that = (ClimateParameterDO) o;

        if (!id.equals(that.id)) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (sensorTypes != null ? !sensorTypes.equals(that.sensorTypes) : that.sensorTypes != null)
            return false;
        return units != null ? units.equals(that.units) : that.units == null;
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (sensorTypes != null ? sensorTypes.hashCode() : 0);
        result = 31 * result + (units != null ? units.hashCode() : 0);
        return result;
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
