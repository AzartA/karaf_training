package ru.training.karaf.model;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
public class LocationDO implements Location  {
    @Id
    @GeneratedValue
    private long id;
    @Column(name = "name", length = 48)
    private String name;
    @OneToMany(mappedBy = "location", fetch = FetchType.EAGER)
    private Set<SensorDO> sensorSet;

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
