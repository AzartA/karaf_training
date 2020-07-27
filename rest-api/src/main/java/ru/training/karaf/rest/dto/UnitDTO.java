package ru.training.karaf.rest.dto;

import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;
import ru.training.karaf.model.Unit;

public class UnitDTO implements Unit {
    private long id;
    @Pattern(regexp = "^(\\S+)[A-Za-z0-9_ -]*$", message = "Name must start with 3 letters min; can contain letters, digits, space or _ only.")
    @Length(min = 3, max = 48, message = "Name length must be from 3 to 48 symbols")
    private String name;
    private String notation;

    public UnitDTO() {
    }

    public UnitDTO(Unit unit) {
        this.id = unit.getId();
        this.name = unit.getName();
        this.notation = unit.getNotation();
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
