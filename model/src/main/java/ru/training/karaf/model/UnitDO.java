package ru.training.karaf.model;

import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

//@JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property="id")
@NamedQueries({
        @NamedQuery(name = UnitDO.GET_ALL, query = "SELECT u FROM UnitDO AS u"),
        @NamedQuery(name = UnitDO.GET_BY_NAME, query = "SELECT u FROM UnitDO AS u WHERE u.name = :name"),
        @NamedQuery(name = UnitDO.GET_BY_ID, query = "SELECT u FROM UnitDO AS u WHERE u.id = :id"),
        @NamedQuery(name = UnitDO.GET_BY_ID_OR_NAME, query = "SELECT u FROM UnitDO AS u WHERE u.id = :id OR u.name = :name")
})
@Entity
public class UnitDO implements Unit {
    public static final String GET_ALL = "Units.getAll";
    public static final String GET_BY_NAME = "Units.getByName";
    public static final String GET_BY_ID = "Units.getById";
    public static final String GET_BY_ID_OR_NAME = "Units.getByIdOrName";
    @Id
    @GeneratedValue
    private long id;
    @Column(name = "name", length = 48, nullable = false, unique = true)
    private String name;
    @Column(name = "notation", length = 32)
    private String notation;
    // @JsonBackReference
    // @JsonIdentityReference(alwaysAsId = true)
    //@JsonSerialize(using = SetOfEntitiesSerializer.class)
    @ManyToMany(mappedBy = "units", cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private Set<ClimateParameterDO> climateParameters;

    public UnitDO() {
    }

    public UnitDO(String name, String notation) {
        this.name = name;
        this.notation = notation;
    }

    public UnitDO(Unit unit) {
        this.name = unit.getName();
        this.notation = unit.getNotation();
        this.climateParameters = (Set<ClimateParameterDO>) unit.getClimateParameters();
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

    @Override
    public String getNotation() {
        return notation;
    }

    public void setNotation(String notation) {
        this.notation = notation;
    }

    public Set<ClimateParameterDO> getClimateParameters() {
        return climateParameters;
    }

    public void setClimateParameters(Set<ClimateParameterDO> climateParameters) {
        this.climateParameters = climateParameters;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UnitDO)) {
            return false;
        }
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
                ", notation='" + notation + '\'' +
                ", climateParameters=" + parametersNames +
                '}';
    }
}
