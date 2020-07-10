package ru.training.karaf.model;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Entity
public class SensorTypeDO implements SensorType {
    @Id
    @GeneratedValue
    private Long id;
    @Column(length = 48)
    private String name;
    @Column (length = 48)
    private String range;
    @Column(name = "min_time")
    private int minTime;
    @OneToMany(mappedBy = "type", fetch = FetchType.EAGER)
    private Set<SensorDO> sensorSet;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getRange() {
        return range;
    }
    public void setRange(String range) {
        this.range = range;
    }
    public int getMinTime() {
        return minTime;
    }
    public void setMinTime(int minTime) {
        this.minTime = minTime;
    }
    public Set<SensorDO> getSensorSet() {
        return sensorSet;
    }
    public void setSensorSet(Set<SensorDO> sensorSet) {
        this.sensorSet = sensorSet;
    }
    public void setName(String name) {
        this.name = name;
    }
    @Override
    public String getName() {
        return null;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id,name,range, minTime);
    }

    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        SensorTypeDO other = (SensorTypeDO) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (range == null) {
            if (other.range != null)
                return false;
        } else if (!range.equals(other.range))
            return false;
        if (minTime != other.minTime)
                return false;
        return true;
    }
    @Override
    public String toString() {
        return "SensorDO [id=" + id + ", name=" + name + ", range =" + range + ", minInterval =" + minTime + "]";
    }
}
