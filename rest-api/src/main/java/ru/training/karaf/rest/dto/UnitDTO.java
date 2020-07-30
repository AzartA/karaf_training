package ru.training.karaf.rest.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonBackReference;
import org.hibernate.validator.constraints.Length;
import ru.training.karaf.model.ClimateParameter;
import ru.training.karaf.model.Unit;

import java.util.Set;
import java.util.stream.Collectors;

public class UnitDTO implements Unit {
    private long id;
    @NotNull(message = "Name should be present")
    @Pattern(regexp = "^(\\S+)[A-Za-z0-9_ -]*$", message = "Name must start with 3 letters min; can contain letters, digits, space or _ only.")
    @Length(min = 3, max = 48, message = "Name length must be from 3 to 48 symbols")
    private String name;
    private String notation;
    //@JsonBackReference
    private Set<EntityDTO> climateParameters;

    public UnitDTO() {
    }

    public UnitDTO(long id, String name, String notation) {
        this.id = id;
        this.name = name;
        this.notation = notation;
    }

    public UnitDTO(Unit unit) {
        this.id = unit.getId();
        this.name = unit.getName();
        this.notation = unit.getNotation();
        /*this.climateParameters = unit.getClimateParameters().stream()
                .map(p -> new ClimateParameterDTO(p.getId(), p.getName())).collect(Collectors.toSet());
         */
        this.climateParameters = unit.getClimateParameters().stream()
                .map(EntityDTO::new).collect(Collectors.toSet());

        //this.climateParameters = (Set<ClimateParameterDTO>) unit.getClimateParameters();
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
    public String getNotation() {
        return notation;
    }

    public void setNotation(String notation) {
        this.notation = notation;
    }

    @Override
    public Set<EntityDTO> getClimateParameters() {
        return climateParameters;
    }

    public void setClimateParameters(Set<EntityDTO> climateParameters) {
        this.climateParameters = climateParameters;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UnitDTO)) {
            return false;
        }

        UnitDTO unitDTO = (UnitDTO) o;

        return id == unitDTO.id;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

    @Override
    public String toString() {
        return "UnitDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", notation='" + notation + '\'' +
                '}';
    }
}
