package ru.training.karaf.rest.dto;

import ru.training.karaf.model.Capability;

public class CapabilityDTO implements Capability {
    private String range;
    private String accuracy;
    private String resolution;

    public CapabilityDTO() {
    }

    public CapabilityDTO(String range, String accuracy, String resolution) {
        this.range = range;
        this.accuracy = accuracy;
        this.resolution = resolution;
    }

    public CapabilityDTO(Capability capability) {
        this.range = capability.getRange();
        this.accuracy = capability.getAccuracy();
        this.resolution = capability.getResolution();
    }

    @Override
    public String getRange() {
        return range;
    }

    @Override
    public String getAccuracy() {
        return accuracy;
    }

    @Override
    public String getResolution() {
        return resolution;
    }

    public void setRange(String range) {
        this.range = range;
    }

    public void setAccuracy(String accuracy) {
        this.accuracy = accuracy;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof CapabilityDTO))
            return false;

        CapabilityDTO that = (CapabilityDTO) o;

        if (range != null ? !range.equals(that.range) : that.range != null)
            return false;
        if (accuracy != null ? !accuracy.equals(that.accuracy) : that.accuracy != null)
            return false;
        return resolution != null ? resolution.equals(that.resolution) : that.resolution == null;
    }

    @Override
    public int hashCode() {
        int result = range != null ? range.hashCode() : 0;
        result = 31 * result + (accuracy != null ? accuracy.hashCode() : 0);
        result = 31 * result + (resolution != null ? resolution.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "CapabilityDTO{" +
                "range='" + range + '\'' +
                ", accuracy='" + accuracy + '\'' +
                ", resolution='" + resolution + '\'' +
                '}';
    }
}
