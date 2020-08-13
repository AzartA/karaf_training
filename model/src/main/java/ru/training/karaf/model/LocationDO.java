package ru.training.karaf.model;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

@Entity
@NamedQueries({
        @NamedQuery(name = LocationDO.GET_ALL, query = "SELECT l FROM LocationDO AS l"),
        @NamedQuery(name = LocationDO.GET_BY_NAME, query = "SELECT l FROM LocationDO AS l WHERE l.name = :name"),
        @NamedQuery(name = LocationDO.GET_BY_ID, query = "SELECT l FROM LocationDO AS l WHERE l.id = :id"),
        @NamedQuery(name = LocationDO.GET_BY_ID_OR_NAME, query = "SELECT l FROM LocationDO AS l WHERE l.id = :id OR l.name = :name")
})
public class LocationDO  implements Location {
    public static final String GET_ALL = "Locations.getAll";
    public static final String GET_BY_NAME = "Locations.getByName";
    public static final String GET_BY_ID = "Locations.getById";
    public static final String GET_BY_ID_OR_NAME = "Locations.getByIdOrName";
    @Id
    @GeneratedValue
    private long id;
    @Column(name = "name", length = 48, nullable = false, unique = true)
    private String name;
    @OneToMany(mappedBy = "location", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<SensorDO> sensors;
    @Column(columnDefinition = "oid")
    private long planOid;
    @Column(name = "picturetype", length = 32)
    private String pictureType;

    public LocationDO() {
    }

    public LocationDO(String name) {
        this.name = name;
        sensors = new HashSet<>();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<SensorDO> getSensors() {
        return sensors;
    }

    public void setSensors(Set<SensorDO> sensors) {
        this.sensors = sensors;
    }

    public long getPlanOid() {
        return planOid;
    }

    public void setPlanOid(long planOid) {
        this.planOid = planOid;
    }

    public String getPictureType() {
        return pictureType;
    }

    public void setPictureType(String pictureType) {
        this.pictureType = pictureType;
    }

    public boolean addSensors(Set<SensorDO> sensors) {
        boolean unitsAdded = this.sensors.addAll(sensors);
        sensors.forEach(s -> s.setLocation(this));
        return unitsAdded;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(id);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LocationDO)) {
            return false;
        }
        LocationDO that = (LocationDO) o;
        return id == that.id;
    }

    @Override
    public String toString() {
        String sensorsNames = sensors.stream().map(Sensor::getName).collect(Collectors.joining(","));
        return "UserDO [id=" + id +
                ", name=" + name +
                ", planOid=" + planOid +
                ", pictureType=" + pictureType +
                ", sensorSet=" + sensorsNames +
                "]";
    }
}
