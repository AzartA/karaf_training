package ru.training.karaf.rest.dto;

import java.util.Set;
import java.util.stream.Collectors;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonBackReference;
import org.hibernate.validator.constraints.Length;
import ru.training.karaf.model.ClimateParameter;
import ru.training.karaf.model.Entity;
import ru.training.karaf.model.Unit;

public class ClimateParameterDTO implements ClimateParameter {
    private long id;
    @NotNull(message = "Name should be present")
    @Pattern(regexp = "^(\\S+)[A-Za-z0-9_ -]*$", message = "Name must start with 3 letters min; can contain letters, digits, space or _ only.")
    @Length(min = 3, max = 48, message = "Name length must be from 3 to 48 symbols")
    private String name;
    //@JsonBackReference
    private Set<EntityDTO> units;

    public ClimateParameterDTO() {
    }

    public ClimateParameterDTO(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public ClimateParameterDTO(ClimateParameter parameter) {
        this.id = parameter.getId();
        this.name = parameter.getName();
        /*this.units = parameter.getUnits().stream().map(u -> new UnitDTO(u.getId(),u.getName(),u.getNotation())).collect(Collectors.toSet());
         */
        this.units = parameter.getUnits().stream().map(EntityDTO::new).collect(Collectors.toSet());

        //this.units = (Set<UnitDTO>) parameter.getUnits();
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
    public Set<EntityDTO> getUnits() {
        return units;
    }

    public void setUnits(Set<EntityDTO> units) {
        this.units = units;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ClimateParameterDTO)) {
            return false;
        }

        ClimateParameterDTO that = (ClimateParameterDTO) o;

        return id == that.id;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

    @Override
    public String toString() {
        String unitsNames = "[" + units.stream().map(EntityDTO::getName).collect(Collectors.joining(",")) + "]";
        return "ClimateParameterDTO{" +
                "id=" + id +
                ", name=" + name +
                ", units=" + unitsNames +
                '}';
    }
}
