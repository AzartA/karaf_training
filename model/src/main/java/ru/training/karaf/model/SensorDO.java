package ru.training.karaf.model;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
public class SensorDO implements Sensor {
    @Id
    @GeneratedValue
    private Long id;
    @Column(name = "name")
    private String name;
    @ManyToOne (optional = false, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "location")
    private LocationDO location;
    @ManyToOne (optional = false, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "type")
    private SensorTypeDO type;
    @ManyToMany(mappedBy="sensors")
    private Set<UserDO> users;

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
    public LocationDO getLocation() {
        return location;
    }
    public void setLocation(LocationDO location) {
        this.location = location;
    }
    public Set<UserDO> getUsers() {
        return users;
    }
    public void setUsers(Set<UserDO> users) {
        this.users = users;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, location, users);
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
        if (users == null) {
            if (other.users != null)
                return false;
        } else if (!users.equals(other.users))
            return false;
        return true;
    }
    @Override
    public String toString() {
        String userNames = "[" + users.stream().map(User::getName).collect(Collectors.joining(",")) + "]";
        return "SensorDO [id=" + id + ", name=" + name + ", location =" + location.getName() +
                ", users ="  + userNames + "]";
    }
}
