package ru.training.karaf.rest.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;
import ru.training.karaf.model.Entity;

public class EntityDTO implements Entity {
    private long id;
    @NotNull(message = "Name should be present")
    @Pattern(regexp = "^(\\S+)[A-Za-z0-9_ ,-]*$",
            message = "Name must start with 3 letters min; can contain letters, digits, space, \"_\" or \",\" only.")
    @Length(min = 3, max = 48, message = "Name length must be from 3 to 48 symbols")
    private String name;

    public EntityDTO() {
    }

    public EntityDTO(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public EntityDTO(Entity entity) {
        this.id = entity.getId();
        this.name = entity.getName();
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
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EntityDTO)) {
            return false;
        }

        EntityDTO entityDTO = (EntityDTO) o;

        return id == entityDTO.id;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

    @Override
    public String toString() {
        return "EntityDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
