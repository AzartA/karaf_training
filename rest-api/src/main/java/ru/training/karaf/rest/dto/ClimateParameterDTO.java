package ru.training.karaf.rest.dto;

import org.hibernate.validator.constraints.Length;
import ru.training.karaf.model.ClimateParameter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Set;
import java.util.stream.Collectors;

public class ClimateParameterDTO implements ClimateParameter {
    private long id;
    @NotNull(message = "Name should be present")
    @Pattern(regexp = "^(\\S+)[A-Za-z0-9_ -]*$", message = "Name must start with 3 letters min; can contain letters, digits, space or _ only.")
    @Length(min = 3, max = 48, message = "Name length must be from 3 to 48 symbols")
    private String name;
    //    @JsonSerialize(using = SetOfEntSerializer.class)
    private Set<EntityDTO> units;
    private Set<EntityDTO> sensorTypes;

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
        this.sensorTypes = parameter.getSensorTypes().stream().map(EntityDTO::new).collect(Collectors.toSet());
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
    public Set<EntityDTO> getSensorTypes() {
        return sensorTypes;
    }

    public void setSensorTypes(Set<EntityDTO> sensorTypes) {
        this.sensorTypes = sensorTypes;
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
        String typesNames = "[" + sensorTypes.stream().map(EntityDTO::getName).collect(Collectors.joining(",")) + "]";
        return "ClimateParameterDTO{" +
                "id=" + id +
                ", name=" + name +
                ", units=" + unitsNames +
                ", sensorTypes=" + typesNames +
                '}';
    }
}
