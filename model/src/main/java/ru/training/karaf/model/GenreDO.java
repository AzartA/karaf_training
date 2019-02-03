package ru.training.karaf.model;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Table(name = "GENREDO")
@Entity
@NamedQueries({
    @NamedQuery(name = GenreDO.GET_ALL_GENRES,
            query = "SELECT g FROM GenreDO g"),
    @NamedQuery(name = GenreDO.GET_GENRE_BY_NAME,
            query = "SELECT g FROM GenreDO g WHERE g.name = :name")
})
public class GenreDO implements Genre, Serializable {
    public static final String GET_ALL_GENRES = "GenreDO.getAll";
    public static final String GET_GENRE_BY_NAME = "GenreDO.getByName";
    
    @Id
    @GeneratedValue
    @Column(name = "ID")
    private Long id;
    
    @Column(name = "NAME", unique = true)
    private String name;

    public GenreDO() {}
    
    public GenreDO(Genre genre) {
        this.name = genre.getName();
    }

    public GenreDO(String name) {
        this.name = name;
    }
    
    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, name);
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
        final GenreDO other = (GenreDO) obj;
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "GenreDO{" + "id=" + id + ", name=" + name + '}';
    }
}
