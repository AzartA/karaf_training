package ru.training.karaf.rest.dto;

import org.hibernate.validator.constraints.Length;
import ru.training.karaf.model.ClimateParameter;
import ru.training.karaf.model.Unit;

import javax.validation.constraints.Pattern;
import java.util.Set;
import java.util.stream.Collectors;

public class ClimateParameterDTO implements ClimateParameter {
    private long id;
    @Pattern(regexp = "^(\\S+)[A-Za-z0-9_ -]*$", message = "Name must start with 3 letters min; can contain letters, digits, space or _ only.")
    @Length(min = 3, max = 48, message = "Name length must be from 3 to 48 symbols")
    private String name;
    private Set<? extends Unit> units;

    public ClimateParameterDTO() {
    }

    public ClimateParameterDTO(ClimateParameter parameter) {
        this.id = parameter.getId();
        this.name = parameter.getName();
        this.units = parameter.getUnits();
    }

    @Override
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
    public Set<? extends Unit> getUnits() {
        return units;
    }

    public void setUnits(Set<? extends Unit> units) {
        this.units = units;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof ClimateParameterDTO))
            return false;

        ClimateParameterDTO that = (ClimateParameterDTO) o;

        return id == that.id;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

    @Override
    public String toString() {
        String unitsNames = "[" + units.stream().map(Unit::getName).collect(Collectors.joining(","))+"]";
        return "ClimateParameterDTO{" +
                "id=" + id +
                ", name=" + name +
                ", units=" + unitsNames +
                '}';
    }
}
