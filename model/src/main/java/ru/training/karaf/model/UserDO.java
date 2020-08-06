package ru.training.karaf.model;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@NamedQueries({
        @NamedQuery(name = UserDO.GET_ALL, query = "SELECT u FROM UserDO AS u"),
        @NamedQuery(name = UserDO.GET_BY_LOGIN, query = "SELECT u FROM UserDO AS u WHERE u.login = :login"),
        @NamedQuery(name = UserDO.GET_BY_ID, query = "SELECT u FROM UserDO AS u WHERE u.id = :id"),
        @NamedQuery(name = UserDO.GET_BY_ID_OR_LOGIN, query = "SELECT u FROM UserDO AS u WHERE u.id = :id OR u.login = :login")

})
public class UserDO implements User {
    public static final String GET_ALL = "Users.getAll";
    public static final String GET_BY_LOGIN = "Users.getByLogin";
    public static final String GET_BY_ID = "Users.getById";
    public static final String GET_BY_ID_OR_LOGIN = "Users.getByIdOrLogin";
    @Id
    @GeneratedValue
    private long id;
    @Column(name = "name")
    private String name;
    @Column(name = "login", length = 48, nullable = false, unique = true)
    private String login;
    @ElementCollection
    @CollectionTable(name = "user_properties",
            joinColumns = @JoinColumn(name = "user_id"))
    private Set<String> properties;
    @ManyToMany
    @JoinTable(name = "USER_SENSOR_SET")
    private Set<SensorDO> sensors;

    public UserDO() {
    }

    public UserDO(User user) {
        name = user.getName();
        login = user.getLogin();
        properties = user.getProperties();
        sensors = new HashSet<>();
    }

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

    public boolean addSensors(Set<SensorDO> sensors) {
        boolean sensorAdded = this.sensors.addAll(sensors);
        boolean usersAdded = sensors.stream().map(s -> s.getUsers().add(this)).reduce(true, (a, b) -> a && b);
        return usersAdded && sensorAdded;
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
        return "UserDO [id=" + id +
                ", firstName=" + name +
                ", login=" + login +
                ", properties=" + properties +
                ", sensors=" + sensorNames +
                "]";
    }
}
