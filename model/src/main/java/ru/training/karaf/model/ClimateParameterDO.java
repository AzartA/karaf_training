package ru.training.karaf.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import ru.training.karaf.serializer.SetOfEntitiesSerializer;

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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@NamedQueries({
        @NamedQuery(name = ClimateParameterDO.GET_ALL, query = "SELECT u FROM ClimateParameterDO AS u"),
        @NamedQuery(name = ClimateParameterDO.GET_BY_NAME, query = "SELECT u FROM ClimateParameterDO AS u WHERE u.name = :name"),
        @NamedQuery(name = ClimateParameterDO.GET_BY_ID, query = "SELECT u FROM ClimateParameterDO AS u WHERE u.id = :id"),
        @NamedQuery(name = ClimateParameterDO.GET_BY_ID_OR_NAME, query = "SELECT u FROM ClimateParameterDO AS u WHERE u.id = :id OR u.name = :name")
})
@Entity
//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class ClimateParameterDO implements ClimateParameter {
    public static final String GET_ALL = "Params.getAll";
    public static final String GET_BY_NAME = "Params.getByName";
    public static final String GET_BY_ID = "Params.getById";
    public static final String GET_BY_ID_OR_NAME = "Params.getByIdOrName";
    @Id
    @GeneratedValue
    private long id;
    @Column(name = "name", length = 48, nullable = false, unique = true)
    private String name;
    //@JsonSerialize(using = SetOfEntitiesSerializer.class)
    //@JsonBackReference
    //@JsonManagedReference
    //@JsonIdentityReference(alwaysAsId = true)
    @ManyToMany(cascade = {CascadeType.MERGE,CascadeType.PERSIST})
    @JoinTable(name = "PARAMETER_UNIT_SET")
    Set<UnitDO> units = new HashSet<>();
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
        this.units = (Set<UnitDO>)units;
    }
    public ClimateParameterDO(ClimateParameter param){
        this.name = param.getName();
        this.units = (Set<UnitDO>)param.getUnits();
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

    public Set<UnitDO> getUnits() {
        return units;
    }

    public void setUnits(Set<UnitDO> units) {
        this.units = units;
    }

    public boolean addUnits(Set<UnitDO> units) {
        boolean unitsAdded = this.units.addAll(units);
        boolean paramAdded = units.stream().map(u -> u.getClimateParameters().add(this)).reduce(true,(a,b) -> a && b);
        return unitsAdded && paramAdded;
    }

    public boolean removeUnits(Set<? extends Unit> units) {
        Set<UnitDO> unitDOSet = (Set<UnitDO>)units;
        boolean unitsRemoved = this.units.removeAll(unitDOSet);
        boolean paramRemoved = unitDOSet.stream().map(u -> u.getClimateParameters().remove(this)).reduce(true,(a,b) -> a && b);
        return unitsRemoved && paramRemoved;
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
