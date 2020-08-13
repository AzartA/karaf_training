package ru.training.karaf.rest.dto;

import org.hibernate.validator.constraints.Length;
import ru.training.karaf.model.Location;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class LocationDTO extends EntityDTO implements Location {

    private long planOid;
    private String pictureType;


    public LocationDTO() {
    }

    public LocationDTO(Location location) {
        super(location);
        planOid = location.getPlanOid();
        pictureType = location.getPictureType();
    }

    @Override
    public long getPlanOid() {
        return planOid;
    }

    public void setPlanOid(long planOid) {
        this.planOid = planOid;
    }

    @Override
    public String getPictureType() {
        return pictureType;
    }

    public void setPictureType(String pictureType) {
        this.pictureType = pictureType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LocationDTO)) return false;

        LocationDTO that = (LocationDTO) o;
        return super.equals(that);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public String toString() {
        return "LocationDTO{" +
                "id=" + super.getId() +
                ", name='" + super.getName() + '\'' +
                ", planOid=" + planOid +
                ", pictureType='" + pictureType + '\'' +
                '}';
    }
}
