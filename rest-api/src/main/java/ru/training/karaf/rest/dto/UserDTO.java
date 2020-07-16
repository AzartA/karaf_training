package ru.training.karaf.rest.dto;

import java.util.Set;


import ru.training.karaf.model.User;

import javax.validation.constraints.Size;


public class UserDTO implements User {

    @Size(min = 3, max = 48,message = "Login must contain from 3 to 48 symbols!")
    private String login;
    private String name;
    private Set<String> properties;

    public UserDTO() {}
    
    public UserDTO(User user) {
        login = user.getLogin();
        name = user.getName();
        properties = user.getProperties();
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
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((login == null) ? 0 : login.hashCode());
        result = prime * result + ((properties == null) ? 0 : properties.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        UserDTO other = (UserDTO) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (login == null) {
            if (other.login != null)
                return false;
        } else if (!login.equals(other.login))
            return false;
        if (properties == null) {
            if (other.properties != null)
                return false;
        } else if (!properties.equals(other.properties))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "UserDTO [login=" + login + ", name=" + name + ", properties=" + properties + "]";
    }
}
