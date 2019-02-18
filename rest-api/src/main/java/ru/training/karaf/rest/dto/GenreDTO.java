package ru.training.karaf.rest.dto;

import java.util.Objects;
import ru.training.karaf.model.Genre;

public class GenreDTO implements Genre {
    
    private String name;
    
    public GenreDTO() {}
    
    public GenreDTO(Genre genre) {
        this.name = genre.getName();
    }

    public GenreDTO(String name) {
        this.name = name;
    }
    
    @Override
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.name);
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
        final GenreDTO other = (GenreDTO) obj;
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "GenreDTO{" + "name=" + name + '}';
    }
}
