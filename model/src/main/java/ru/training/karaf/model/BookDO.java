package ru.training.karaf.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Table(name = "BOOKDO")
@Entity
@NamedQueries({
    @NamedQuery(name = BookDO.GET_ALL_BOOKS,
            query = "SELECT b FROM BookDO b"),
    @NamedQuery(name = BookDO.GET_BOOK_BY_TITLE,
            query = "SELECT b FROM BookDO b WHERE b.title = :title")
})
@NamedNativeQuery(name = BookDO.RESET_OWNERSHIP,
            query = "DELETE FROM USERDO_BOOKDO WHERE BOOK_ID = ?")

public class BookDO implements Book, Serializable {
    public static final String GET_ALL_BOOKS = "Books.getAll";
    public static final String GET_BOOK_BY_TITLE = "Books.getByTitle";
    public static final String RESET_OWNERSHIP = "Books.resetOwnership";
    
    
    @Id
    @GeneratedValue
    @Column(name = "ID")
    private Long id;
    
    @Column(name = "TITLE", unique = true)
    private String title;
    
    @Column(name = "AUTHOR")
    private String author;
    
    @Column(name = "RELEASE_YEAR")
    private Integer year;
    
    @ManyToOne(fetch = FetchType.LAZY/*, cascade = CascadeType.ALL*/)
    @JoinColumn(name = "GENRE_ID", nullable = false)
    private GenreDO genre;
    
    @OneToMany(cascade = CascadeType.ALL,
            mappedBy = "book",
            orphanRemoval = true)
    private List<FeedbackDO> feedbacks = new ArrayList<>();

    public BookDO() {}
    
    public BookDO(Book book){
        this.title = book.getTitle();
        this.author = book.getAuthor();
        this.year = book.getYear();
        this.genre = new GenreDO(book.getGenre());
//        book.getFeedbacks().forEach(f -> {
//            FeedbackDO fb = new FeedbackDO();
//            fb.setBook(this);
//            fb.setMessage(f.getMessage());
//            fb.setUser(new UserDO(f.getUser()));
//            feedbacks.add(fb);
//        });
    }
    
    public BookDO(String title, String author, Integer year, GenreDO genre) {
        this.title = title;
        this.author = author;
        this.year = year;
        this.genre = genre;
    }
    
    public void addFeedback(FeedbackDO feedback) {
        feedbacks.add(feedback);
        feedback.setBook(this);
    }
    
    public void removeFeedback(FeedbackDO feedback) {
        feedbacks.remove(feedback);
        feedback.setBook(null);
    }
    
    @Override
    public List<FeedbackDO> getFeedbacks() {
        return feedbacks;
    }

    public void setFeedbacks(List<FeedbackDO> feedbacks) {
        this.feedbacks = feedbacks;
    }
    
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

    @Override
    public String toString() {
        return "BookDO{" + "id=" + id + ", title=" + title + ", author=" +
                author + ", year=" + year + ", genre=" + genre + '}';
    }
}
