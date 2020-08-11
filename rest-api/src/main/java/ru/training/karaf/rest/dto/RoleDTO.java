package ru.training.karaf.rest.dto;

import ru.training.karaf.model.Entity;
import ru.training.karaf.model.Role;

import java.util.Set;
import java.util.stream.Collectors;

public class RoleDTO extends EntityDTO implements Role {
    private Set<EntityDTO> users;

    public RoleDTO() {
    }

    public RoleDTO(Role role) {
        super(role);
        users = role.getUsers().stream().map(EntityDTO::new).collect(Collectors.toSet());
    }

    @Override
    public Set<EntityDTO> getUsers() {
        return users;
    }

    public void setUsers(Set<EntityDTO> users) {
        this.users = users;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RoleDTO)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
