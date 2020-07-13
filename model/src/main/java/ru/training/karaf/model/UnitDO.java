package ru.training.karaf.model;

import javax.persistence.*;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
public class UnitDO implements  Unit{
    @Id
    @GeneratedValue
    private long id;
    @Column(name = "name", length = 48)
    private String name;
    @ManyToMany(mappedBy="units")
    private Set<ClimateParameterDO> climateParameters;

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
    public Set<ClimateParameterDO> getClimateParameters() {
        return climateParameters;
    }
    public void setClimateParameters(Set<ClimateParameterDO> climateParameters) {
        this.climateParameters = climateParameters;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UnitDO)) return false;
        UnitDO that = (UnitDO) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(id);
    }

    @Override
    public String toString() {
        String parametersNames = "[" + climateParameters.stream().map(ClimateParameter::getName).collect(Collectors.joining(",")) + "]";
        return "UnitDO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", climateParameters=" + parametersNames +
                '}';
    }
}
