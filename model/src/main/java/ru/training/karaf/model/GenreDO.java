package ru.training.karaf.model;

import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class GenreDO implements Genre {
    @Id
    @GeneratedValue
    private Long id;
    
    @Column(name = "NAME")
    private String name;
    
    @OneToMany(cascade = CascadeType.ALL,
            orphanRemoval = true,
            mappedBy = "genre")
    private List<BookDO> books;
    
    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public List<BookDO> getBooks() {
        return books;
    }
    
    public void setBooks(List<BookDO> books) {
        this.books = books;
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    public void addBook(BookDO book) {
        books.add(book);
        book.setGenre(this);
    }
    
    public void removeBook(BookDO book) {
        books.remove(book);
        book.setGenre(null);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, books);
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
        if (!Objects.equals(this.books, other.books)) {
            return false;
        }
        return true;
    }
}
