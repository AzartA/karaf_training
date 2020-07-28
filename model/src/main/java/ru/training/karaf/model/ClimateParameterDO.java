package ru.training.karaf.model;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

@NamedQueries({
        @NamedQuery(name = ClimateParameterDO.GET_ALL, query = "SELECT u FROM ClimateParameterDO AS u"),
        @NamedQuery(name = ClimateParameterDO.GET_BY_NAME, query = "SELECT u FROM ClimateParameterDO AS u WHERE u.name = :name"),
        @NamedQuery(name = ClimateParameterDO.GET_BY_ID, query = "SELECT u FROM ClimateParameterDO AS u WHERE u.id = :id"),
        @NamedQuery(name = ClimateParameterDO.GET_BY_ID_OR_NAME, query = "SELECT u FROM ClimateParameterDO AS u WHERE u.id = :id OR u.name = :name")
})
@Entity
public class ClimateParameterDO implements ClimateParameter {
    public static final String GET_ALL = "Params.getAll";
    public static final String GET_BY_NAME = "Params.getByName";
    public static final String GET_BY_ID = "Params.getById";
    public static final String GET_BY_ID_OR_NAME = "Params.getByIdOrName";
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "PARAMETER_UNIT_SET")
    Set<UnitDO> units;
    @Id
    @GeneratedValue
    private long id;
    @Column(name = "name", length = 48, nullable = false, unique = true)
    private String name;
    @ManyToMany(mappedBy = "parameters")
    private Set<SensorTypeDO> sensorTypes;
    @OneToMany(mappedBy = "parameter", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<MeasuringDO> measurings;

    public ClimateParameterDO() {
    }

    public ClimateParameterDO(String name) {
        this.name = name;
    }

    public ClimateParameterDO(String name, Set<? extends Unit> units) {
        this.name = name;
        this.units = (Set<UnitDO>) units;
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

    public Set<SensorTypeDO> getSensorTypes() {
        return sensorTypes;
    }

    public void setSensorTypes(Set<SensorTypeDO> sensorTypes) {
        this.sensorTypes = sensorTypes;
    }

    public Set<? extends Unit> getUnits() {
        return units;
    }

    public void setUnits(Set<? extends Unit> units) {
        this.units = (Set<UnitDO>) units;
    }

    public List<MeasuringDO> getMeasurings() {
        return measurings;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ClimateParameterDO)) {
            return false;
        }
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
