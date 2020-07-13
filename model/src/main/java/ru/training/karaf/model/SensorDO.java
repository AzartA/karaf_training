package ru.training.karaf.model;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
public class SensorDO implements Sensor {
    @Id
    @GeneratedValue
    private Long id;
    @Column(name = "name", length = 48)
    private String name;
    @ManyToOne (optional = false, cascade = {CascadeType.ALL})
    @JoinColumn(name = "location")
    private LocationDO location;
    @ManyToOne (optional = false, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "type")
    private SensorTypeDO type;
    @ManyToMany(mappedBy="sensors")
    private Set<UserDO> users;
//ToDo Type? Maybe List? What about sorting?
    @OneToMany(mappedBy = "sensor")
    private Set<MeasuringDO> meashurings;

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
    public SensorTypeDO getType() {
        return type;
    }
    public void setType(SensorTypeDO type) {
        this.type = type;
    }
    public Set<MeasuringDO> getMeashurings() {
        return meashurings;
    }


    @Override
    public int hashCode() {
        return Objects.hash(id, name, location, type);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SensorDO)) return false;

        SensorDO sensorDO = (SensorDO) o;

        if (!id.equals(sensorDO.id)) return false;
        if (name != null ? !name.equals(sensorDO.name) : sensorDO.name != null) return false;
        if (location != null ? !location.equals(sensorDO.location) : sensorDO.location != null) return false;
        return type != null ? type.equals(sensorDO.type) : sensorDO.type == null;
    }

    @Override
    public String toString() {
        String userNames = "[" + users.stream().map(User::getName).collect(Collectors.joining(",")) + "]";

        return "SensorDO{" +
                "id=" + id +
                ", name=" + name +
                ", location=" + location.getName() +
                ", type=" + type.getName() +
                ", users=" + userNames +
                '}';
    }
}
