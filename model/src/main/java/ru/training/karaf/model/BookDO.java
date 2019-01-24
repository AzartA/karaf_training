package ru.training.karaf.model;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class BookDO implements Book {
    @Id
    @GeneratedValue
    private Long id;
    
    @Column(name = "TITLE")
    String title;
    
    @Column(name = "AUTHOR")
    String author;
    
    @Column(name = "RELEASE_YEAR")
    Integer year;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "GENRE_ID", nullable = false)    
    GenreDO genre;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @Override
    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    @Override
    public GenreDO getGenre() {
        return genre;
    }

    public void setGenre(GenreDO genre) {
        this.genre = genre;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, author, year, genre);
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
        final BookDO other = (BookDO) obj;
        if (!Objects.equals(this.title, other.title)) {
            return false;
        }
        if (!Objects.equals(this.author, other.author)) {
            return false;
        }
        if (!Objects.equals(this.year, other.year)) {
            return false;
        }
        if (!Objects.equals(this.genre, other.genre)) {
            return false;
        }
        return true;
    }
    
    
    
}
