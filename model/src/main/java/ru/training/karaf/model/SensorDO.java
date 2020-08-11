package ru.training.karaf.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
public class SensorDO implements Sensor {
    @Id
    @GeneratedValue
    private long id;
    @Column(name = "name", nullable = false, length = 48, unique = true)
    private String name;
    @ManyToOne(optional = false, fetch= FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "location")
    private LocationDO location;
    @ManyToOne(optional = false, fetch= FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private long x;
    private long y;
    @JoinColumn(name = "type")
    private SensorTypeDO type;
    @ManyToMany(mappedBy = "sensors", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<UserDO> users;
    @OneToMany(mappedBy = "sensor", cascade = {CascadeType.ALL})
    private List<MeasuringDO> measurings;

    public SensorDO() {
    }

    public SensorDO(String name) {
        this.name = name;
    }

    public SensorDO(Sensor sensor) {
        this.name = sensor.getName();
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

    public SensorTypeDO getType() {
        return type;
    }

    public void setType(SensorTypeDO type) {
        type.getSensors().add(this);
        this.type = type;
    }

    public List<MeasuringDO> getMeasurings() {
        return measurings;
    }

    public boolean addUsers(Set<UserDO> users) {
        boolean usersAdded = this.users.addAll(users);
        boolean sensorAdded = users.stream().map(u -> u.getSensors().add(this)).reduce(true, (a, b) -> a && b);
        return usersAdded && sensorAdded;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SensorDO)) return false;
        SensorDO that = (SensorDO) o;
        return id == that.id;
    }

    @Override
    public String toString() {
        String userNames = "[" + users.stream().map(User::getName).collect(Collectors.joining(",")) + "]";
        return "SensorDO{" +
                "id=" + id +
                ", name=" + name +
                ", location=" + (location!= null? location.getName(): "null") +
                ", type=" + (type!= null? type.getName():"null") +
                ", users=" + userNames +
                '}';
    }
}
