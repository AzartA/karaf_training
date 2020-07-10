package ru.training.karaf.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class SensorDO implements Sensor {
    @Id
    @GeneratedValue
    private Long id;
    @Column(name = "name")
    private String name;
    //@ManyToOne (optional = false, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
   // @JoinColumn(name = "location")
    @Column(name = "location")
    private Location location;

    public SensorDO() {
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
    public Location getLocation() {
        return location;
    }
    public void setLocation(Location location) {
        this.location = location;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id,name,location);
    }

    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        SensorDO other = (SensorDO) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (location == null) {
            if (other.location != null)
                return false;
        } else if (!location.equals(other.location))
            return false;
        return true;
    }
    @Override
    public String toString() {
        return "SensorDO [id=" + id + ", name=" + name + ", location =" + location.getName() + "]";
    }
}
