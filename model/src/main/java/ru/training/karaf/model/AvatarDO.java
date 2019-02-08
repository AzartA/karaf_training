package ru.training.karaf.model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Table(name = "AVATARDO")
@Entity
@NamedQuery(name = AvatarDO.GET_ALL_AVATARS, query = "SELECT a FROM AvatarDO a")
public class AvatarDO implements Avatar, Serializable {
    public static final String GET_ALL_AVATARS = "AvatarDO.getAllAvatars";
    
    @Id
    @GeneratedValue
    @Column(name = "ID")
    private Long id;
    
    @Column(name = "PICTURE")
    @Lob
    @Basic(fetch = FetchType.LAZY)
    private byte[] picture;

    public AvatarDO() {}
    
    public AvatarDO(Avatar avatar) {
        this.picture = avatar.getPicture();
    }

    public AvatarDO(byte[] picture) {
        this.picture = picture;
    }
      
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
