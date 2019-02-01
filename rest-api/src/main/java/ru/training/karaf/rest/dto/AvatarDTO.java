package ru.training.karaf.rest.dto;

import java.util.Arrays;
import ru.training.karaf.model.Avatar;

public class AvatarDTO implements Avatar {
    private byte[] picture;
    
    public AvatarDTO() {}
    
    public AvatarDTO(Avatar avatar) {
        this.picture = avatar.getPicture();
    }

    public AvatarDTO(byte[] picture) {
        this.picture = picture;
    }
    
    @Override
    public byte[] getPicture() {
        return picture;
    }
        
    public void setPicture(byte[] picture) {
        this.picture = picture;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 79 * hash + Arrays.hashCode(this.picture);
        return hash;
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
        final AvatarDTO other = (AvatarDTO) obj;
        if (!Arrays.equals(this.picture, other.picture)) {
            return false;
        }
        return true;
    }
}
