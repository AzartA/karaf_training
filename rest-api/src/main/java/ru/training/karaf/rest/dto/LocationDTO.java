package ru.training.karaf.rest.dto;

import org.hibernate.validator.constraints.Length;
import ru.training.karaf.model.Location;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class LocationDTO implements Location {
    private long id;
    @NotNull(message = "Name should be present")
    @Pattern(regexp = "^(\\S+)[A-Za-z0-9_ -]*$", message = "Name must start with 3 letters min; can contain letters, digits, space or _ only.")
    @Length(min = 3, max = 48, message = "Name length must be from 3 to 48 symbols")
    private String name;
    private long planOid;


    public LocationDTO() {
    }

    public LocationDTO(Location location) {
        id = location.getId();
        this.name = location.getName();
        planOid = location.getPlanOid();
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public long getPlanOid() {
        return planOid;
    }

    public void setPlanOid(long planOid) {
        this.planOid = planOid;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LocationDTO)) return false;

        LocationDTO that = (LocationDTO) o;
        return id == that.getId();
    }

    @Override
    public int hashCode() {
        return Long.hashCode(id);
    }

    @Override
    public String toString() {
        return "LocationDTO{" +
                "id=" + id +
                ", name=" + name +
                ", planOid=" + planOid +
                '}';
    }
}
