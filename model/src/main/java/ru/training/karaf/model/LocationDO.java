package ru.training.karaf.model;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Objects;
import java.util.Set;

@Entity
public class LocationDO implements Location  {
    @Id
    @GeneratedValue
    private Long id;
    @Column(name = "name")
    private String name;
   // @OneToMany(mappedBy = "location") //, fetch = FetchType.EAGER)

   // private Set<Sensor> sensorSet;

    public LocationDO() {
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
   /* public Set<Sensor> getSensorSet() {
        return sensorSet;
    }
    public void setSensorSet(Set<Sensor> sensorSet) {
        this.sensorSet = sensorSet;
    }
*/

    @Override
    public int hashCode() {
        return Objects.hash(id,name); //,sensorSet);
    }

    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        LocationDO other = (LocationDO) obj;
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
      /*  if (sensorSet == null) {
            if (other.sensorSet != null)
                return false;
        } else if(!sensorSet.equals(other.sensorSet))
            return false; */
        return true;
    }
    @Override


//ToDo упростить вывод stream to String
    public String toString() {
       // String sensorsNames = Arrays.toString(sensorSet.stream().map(Sensor::getName).toArray());
        return "UserDO [id=" + id + ", name=" + name + "]"; //", sensorSet=" + sensorsNames + "]";
    }
}
