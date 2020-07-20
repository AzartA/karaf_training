package ru.training.karaf.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@NamedQueries({
        @NamedQuery(name = LocationDO.GET_ALL, query = "SELECT l FROM LocationDO AS l"),
        @NamedQuery(name = LocationDO.GET_BY_NAME, query = "SELECT l FROM LocationDO AS l WHERE l.name = :name")
})
public class LocationDO implements Location {
    public static final String GET_ALL = "Locations.getAll";
    public static final String GET_BY_NAME = "Locations.getByName";
    @Id
    @GeneratedValue
    private long id;
    @Column(name = "name", length = 48, nullable = false, unique = true)
    private String name;
    @OneToMany(mappedBy = "location", fetch = FetchType.EAGER)
    //ToDo initialization here?
    private Set<SensorDO> sensorSet;

    public LocationDO() {
    }

    public LocationDO(String name) {
        this.name = name;
        sensorSet = new HashSet<>();
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

    public Set<SensorDO> getSensorSet() {
        return sensorSet;
    }

    public void setSensorSet(Set<SensorDO> sensorSet) {
        this.sensorSet = sensorSet;
    }


    @Override
    public int hashCode() {
        return Long.hashCode(id);
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LocationDO)) return false;
        LocationDO that = (LocationDO) o;
        return id == that.id;
    }

    @Override
    public String toString() {
        String sensorsNames = sensorSet.stream().map(Sensor::getName).collect(Collectors.joining(","));
        return "UserDO [id=" + id + ", name=" + name + ", sensorSet=" + sensorsNames + "]";
    }
}
