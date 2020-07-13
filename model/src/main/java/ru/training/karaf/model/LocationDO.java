package ru.training.karaf.model;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
public class LocationDO implements Location  {
    @Id
    @GeneratedValue
    private Long id;
    @Column(name = "name", length = 48)
    private String name;
    @OneToMany(mappedBy = "location", fetch = FetchType.EAGER)
    private Set<SensorDO> sensorSet;

    public LocationDO() {
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
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
        return Objects.hash(id,name); //,sensorSet);
    }

    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        LocationDO other = (LocationDO) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (id == null) {
            return other.id == null;
        } else return id.equals(other.id);
    }

    @Override
    public String toString() {
        String sensorsNames = sensorSet.stream().map(Sensor::getName).collect(Collectors.joining(","));
        return "UserDO [id=" + id + ", name=" + name + ", sensorSet=" + sensorsNames + "]";
    }
}
