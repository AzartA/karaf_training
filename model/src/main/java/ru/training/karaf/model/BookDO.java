package ru.training.karaf.model;

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
public class BookDO implements Book {
    public static final String GET_ALL_BOOKS = "Books.getAll";
    public static final String GET_BOOK_BY_TITLE = "Books.getByTitle";
    
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
    
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "GENRE_ID", nullable = false)
    private GenreDO genre;
    
    @OneToMany(cascade = CascadeType.ALL,
            mappedBy = "book",
            orphanRemoval = true)
    private List<FeedbackDO> feedbacks = new ArrayList<>();

    public BookDO() {}
    
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
