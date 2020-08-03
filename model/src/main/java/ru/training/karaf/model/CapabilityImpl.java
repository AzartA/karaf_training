package ru.training.karaf.model;

import java.io.Serializable;
import java.util.Objects;

public class CapabilityImpl implements Serializable, Capability {
    private static final long serialVersionUID = 9874563217891L;
    private String range;
    private String accuracy;
    private String resolution;

    @Override
    public String getRange() {
        return range;
    }

    public void setRange(String range) {
        this.range = range;
    }

    @Override
    public String getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(String accuracy) {
        this.accuracy = accuracy;
    }

    @Override
    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CapabilityImpl)) {
            return false;
        }

        CapabilityImpl that = (CapabilityImpl) o;

        if (!Objects.equals(range, that.range)) {
            return false;
        }
        if (!Objects.equals(accuracy, that.accuracy)) {
            return false;
        }
        return Objects.equals(resolution, that.resolution);
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
        return "Capability{" +
                "range='" + range + '\'' +
                ", accuracy='" + accuracy + '\'' +
                ", resolution='" + resolution + '\'' +
                '}';
    }
}