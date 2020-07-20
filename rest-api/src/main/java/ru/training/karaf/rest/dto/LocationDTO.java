package ru.training.karaf.rest.dto;

import ru.training.karaf.model.Location;
import ru.training.karaf.model.Sensor;

import java.util.Set;
import java.util.stream.Collectors;

public class LocationDTO implements Location {
    private String name;
    private Set<? extends Sensor> sensorSet;

    public LocationDTO() {
    }

    public LocationDTO(Location location) {
        this.name = location.getName();
        this.sensorSet = location.getSensorSet();
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Set<? extends Sensor> getSensorSet() {
        return sensorSet;
    }

    public void setSensorSet(Set<? extends Sensor> sensorSet) {
        this.sensorSet = sensorSet;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LocationDTO)) return false;

        LocationDTO that = (LocationDTO) o;

        if (!name.equals(that.name)) return false;
        return sensorSet != null ? sensorSet.equals(that.sensorSet) : that.sensorSet == null;
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + (sensorSet != null ? sensorSet.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        String sensorsNames = sensorSet.stream().map(Sensor::getName).collect(Collectors.joining(","));
        return "LocationDTO{" +
                "name='" + name + '\'' +
                ", sensorSet=" + sensorsNames +
                '}';
    }


}
