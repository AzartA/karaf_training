package ru.training.karaf.model;

import java.util.Arrays;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class AvatarDO implements Avatar {
    @Id
    @GeneratedValue
    @Column(name = "ID")
    private Long id;
    
    @Column(name = "PICTURE")
    private byte[] picture;

    public AvatarDO() {}
      
    @Override
    public byte[] getPicture() {
        return picture;
    }
        
    public void setPicture(byte[] picture) {
        this.picture = picture;
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, picture);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final AvatarDO other = (AvatarDO) obj;
        if (!Arrays.equals(this.picture, other.picture)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "AvatarDO{" + "id=" + id + '}';
    }
}
