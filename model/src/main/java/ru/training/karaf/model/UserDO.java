package ru.training.karaf.model;

import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.*;

@Entity
@NamedQueries({
    @NamedQuery(name = UserDO.GET_ALL, query = "SELECT u FROM UserDO AS u"),
    @NamedQuery(name = UserDO.GET_BY_LOGIN, query = "SELECT u FROM UserDO AS u WHERE u.login = :login")
})
public class UserDO implements User {
    public static final String GET_ALL = "Users.getAll";
    public static final String GET_BY_LOGIN = "Users.getByLogin";
    
    @Id
    @GeneratedValue
    private long id;
    @Column(name = "name")
    private String name;
    @Column(name = "login", nullable = false, unique=true)
    private String login;
    @ElementCollection
    @CollectionTable(name = "user_properties",
            joinColumns = @JoinColumn(name = "user_id"))
    private Set<String> properties;
    @ManyToMany
    @JoinTable(name="USER_SENSOR_SET")
    Set<SensorDO> sensors;

    public UserDO() {}

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    @Override
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getLogin() {
        return login;
    }
    public void setLogin(String login) {
        this.login = login;
    }
    public Set<String> getProperties() {
        return properties;
    }
    public void setProperties(Set<String> properties) {
        this.properties = properties;
    }
    public Set<SensorDO> getSensors() {
        return sensors;
    }
    public void setSensors(Set<SensorDO> sensors) {
        this.sensors = sensors;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(id);
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserDO)) return false;
        UserDO that = (UserDO) o;
        return id == that.id;
    }
    @Override
    public String toString() {
        String sensorNames = "[" + sensors.stream().map(Sensor::getName).collect(Collectors.joining(",")) + "]";
        return "UserDO [id=" + id + ", firstName=" + name + ", login=" + login
                + ", properties=" + properties + ", sensors=" + sensorNames + "]";
    }
}
