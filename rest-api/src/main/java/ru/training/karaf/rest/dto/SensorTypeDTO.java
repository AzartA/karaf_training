package ru.training.karaf.rest.dto;

import ru.training.karaf.model.SensorType;

import java.util.Set;
import java.util.stream.Collectors;

public class SensorTypeDTO extends EntityDTO implements SensorType {
    private CapabilityDTO capability;
    private int minTime;
    private Set<EntityDTO> sensors;
    private Set<EntityDTO> parameters;

    public SensorTypeDTO() {
    }

    public SensorTypeDTO(long id, String name) {
        super(id,name);
    }

    public SensorTypeDTO(SensorType type) {
        super(type);
        capability = type.getCapability()==null?null:new CapabilityDTO(type.getCapability());
        minTime = type.getMinTime();
        parameters = type.getParameters().stream().map(EntityDTO::new).collect(Collectors.toSet());
        sensors = type.getSensors().stream().map(EntityDTO::new).collect(Collectors.toSet());
    }

    @Override
    public CapabilityDTO getCapability() {
        return capability;
    }

    public void setCapability(CapabilityDTO capability) {
        this.capability = capability;
    }

    @Override
    public int getMinTime() {
        return minTime;
    }

    public void setMinTime(int minTime) {
        this.minTime = minTime;
    }

    @Override
    public Set<EntityDTO> getParameters() {
        return parameters;
    }

    public void setParameters(Set<EntityDTO> parameters) {
        this.parameters = parameters;
    }

    @Override
    public Set<EntityDTO> getSensors() {
        return sensors;
    }

    public void setSensors(Set<EntityDTO> sensors) {
        this.sensors = sensors;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof SensorTypeDTO))
            return false;
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public String toString() {
        String paramNames = "[" + parameters.stream().map(EntityDTO::getName).collect(Collectors.joining(",")) + "]";
        String sensorNames = "[" + sensors.stream().map(EntityDTO::getName).collect(Collectors.joining(",")) + "]";
        return "SensorTypeDTO{" +
                "id=" + super.getId() +
                ", name=" + super.getName() +
                "capability=" + capability +
                ", minTime=" + minTime +
                ", parameters=" + paramNames +
                ", sensors=" + sensorNames +
                '}';
    }
}
