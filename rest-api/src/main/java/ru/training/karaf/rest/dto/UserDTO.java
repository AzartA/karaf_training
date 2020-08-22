package ru.training.karaf.rest.dto;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ru.training.karaf.model.Entity;
import ru.training.karaf.model.Sensor;
import ru.training.karaf.model.UniqueFieldEntity;
import ru.training.karaf.model.User;
import ru.training.karaf.rest.validation.UniqueKey;

//@UniqueKey(field = "login", message = "Login is already exist")
public class UserDTO implements User, UniqueFieldEntity {
    private long id;
    @NotNull(message = "Login should be present")
    @Pattern(regexp = "^[0-9a-zA-Z-_]{3,48}$", message = "Login must contain from 3 to 48 letters or digits only")
    private String login;
    @Size(min = 3, max = 48, message = "Name must contain from 3 to 48 symbols")
    private String name;
    @JsonIgnore
    private String password;
    private Set<String> properties;
    private Set<EntityDTO> sensors;
    private Set<EntityDTO> roles;

    public UserDTO() {
    }

    public UserDTO(User user) {
        id = user.getId();
        login = user.getLogin();
        name = user.getName();
        properties = user.getProperties();
        sensors = user.getSensors().stream().map(EntityDTO::new).collect(Collectors.toSet());
        roles = user.getRoles().stream().map(EntityDTO::new).collect(Collectors.toSet());
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<String> getProperties() {
        return properties;
    }

    public void setProperties(Set<String> properties) {
        this.properties = properties;
    }

    public Set<EntityDTO> getSensors() {
        return sensors;
    }

    public void setSensors(Set<EntityDTO> sensors) {
        this.sensors = sensors;
    }

    @Override
    public Set<EntityDTO> getRoles() {
        return roles;
    }

    public void setRoles(Set<EntityDTO> roles) {
        this.roles = roles;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof UserDTO)) {
            return false;
        }
        UserDTO other = (UserDTO) obj;
        return id == other.getId();
    }

    @Override
    public String toString() {
        String sensorNames = "[" + sensors.stream().map(Entity::getName).collect(Collectors.joining(",")) + "]";
        String roleNames = "[" + roles.stream().map(Entity::getName).collect(Collectors.joining(",")) + "]";

        return "UserDTO [id =" + id +
                ", firstName=" + name +
                ", login=" + login +
                ", properties=" + properties +
                ", sensors=" + sensorNames +
                ", roles=" + roleNames +
                "]";
    }
}
