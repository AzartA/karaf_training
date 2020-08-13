package ru.training.karaf.rest.dto;

import java.util.Set;
import java.util.stream.Collectors;

import ru.training.karaf.model.Sensor;

public class SensorDTO extends EntityDTO implements Sensor {
    private long x;
    private long y;
    private EntityDTO location;
    private EntityDTO type;//SensorTypeDTO type;
    private Set<EntityDTO> users;

    public SensorDTO() {
    }

    public SensorDTO(Sensor sensor) {
        super(sensor);
        x = sensor.getX();
        y = sensor.getY();
        this.location = sensor.getLocation() == null ? null : new EntityDTO(sensor.getLocation());
        this.type = sensor.getType() == null ? null : new EntityDTO(sensor.getType());//new SensorTypeDTO(sensor.getType());
        this.users = sensor.getUsers().stream().map(EntityDTO::new).collect(Collectors.toSet());
    }

    @Override
    public long getX() {
        return x;
    }

    public void setX(long x) {
        this.x = x;
    }

    @Override
    public long getY() {
        return y;
    }

    public void setY(long y) {
        this.y = y;
    }

    @Override
    public EntityDTO getLocation() {
        return location;
    }

    public void setLocation(EntityDTO location) {
        this.location = location;
    }

    @Override
    public EntityDTO getType() {
        return type;
    }
    /*@Override
    public SensorTypeDTO getType() {
        return type;
    }*/

    public void setType(SensorTypeDTO type) {
        this.type = type;
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
        if (!(o instanceof SensorDTO)) {
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

    @Override
    public String toString() {
        String userNames = "[" + users.stream().map(EntityDTO::getName).collect(Collectors.joining(",")) + "]";
        return "SensorDTO{" +
                "id=" + super.getId() +
                ", name=" + super.getName() +
                "location=" + (location != null ? location.getName() : "null") +
                ", type=" + (type != null ? type.getName() : "null") +
                ", users=" + userNames +
                '}';
    }
}
