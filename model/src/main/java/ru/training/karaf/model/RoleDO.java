package ru.training.karaf.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
public class RoleDO implements Role, Serializable {
    private static final long serialVersionUID = 5474563217892L;

    @Id
    @GeneratedValue
    private long id;

    @Column(name = "name", nullable = false, length = 48, unique = true)
    private String name;

    @ManyToMany(mappedBy = "roles", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<UserDO> users;

    public RoleDO() {
    }

    public RoleDO(String name) {
        this.name = name;
        users = new HashSet<>();
    }

    @Override
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

    @Override
    public Set<UserDO> getUsers() {
        return users;
    }

    public void setUsers(Set<UserDO> users) {
        this.users = users;
    }

    public boolean addUsers(Set<UserDO> users) {
        boolean usersAdded = this.users.addAll(users);
        boolean roleAdded = users.stream().map(u -> u.getRoles().add(this)).reduce(true, (a, b) -> a && b);
        return usersAdded && roleAdded;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RoleDO)) {
            return false;
        }

        RoleDO roleDO = (RoleDO) o;

        return id == roleDO.id;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

    @Override
    public String toString() {
        String userNames = "[" + users.stream().map(UserDO::getName).collect(Collectors.joining(",")) + "]";
        return "RoleDO{"
                + "id=" + id
                + ", name='" + name + '\''
                + ", users=" + userNames
                + '}';
    }
}
