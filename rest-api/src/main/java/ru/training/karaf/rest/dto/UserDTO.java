package ru.training.karaf.rest.dto;

import java.util.Set;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import ru.training.karaf.model.UniqueFieldEntity;
import ru.training.karaf.model.User;
import ru.training.karaf.rest.validation.UniqueKey;

//@UniqueKey(field = "login", message = "Login is already exist")
public class UserDTO implements User, UniqueFieldEntity {
    private long id;
    @Pattern(regexp = "^[0-9a-zA-Z-_]{3,48}$", message = "Login must contain from 3 to 48 letters or digits only")
    private String login;
    @Size(min = 5, max = 48, message = "Name must contain from 5 to 48 symbols")
    private String name;
    private Set<String> properties;

    public UserDTO() {
    }

    public UserDTO(User user) {
        id = user.getId();
        login = user.getLogin();
        name = user.getName();
        properties = user.getProperties();
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
        return "UserDTO [id =" + id + ", login=" + login + ", name=" + name + ", properties=" + properties + "]";
    }
}
