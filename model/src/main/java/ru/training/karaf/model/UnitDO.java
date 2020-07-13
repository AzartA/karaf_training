package ru.training.karaf.model;

import javax.persistence.*;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
public class UnitDO implements  Unit{
    @Id
    @GeneratedValue
    private Long id;
    @Column(name = "name", length = 48)
    private String name;
    @ManyToMany(mappedBy="units")
    private Set<ClimateParameterDO> climateParameters;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
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
    /* public void setClimateParameters(Set<ClimateParameterDO> climateParameters) {
        this.climateParameters = climateParameters;
    }*/


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UnitDO)) return false;

        UnitDO unitDO = (UnitDO) o;

        if (!id.equals(unitDO.id)) return false;
        if (name != null ? !name.equals(unitDO.name) : unitDO.name != null) return false;
        return climateParameters != null ? climateParameters.equals(unitDO.climateParameters) : unitDO.climateParameters == null;
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (climateParameters != null ? climateParameters.hashCode() : 0);
        return result;
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
