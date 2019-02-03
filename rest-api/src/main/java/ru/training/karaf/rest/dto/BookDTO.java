package ru.training.karaf.rest.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.xml.bind.annotation.XmlTransient;
import ru.training.karaf.model.Book;

public class BookDTO implements Book {
    private String title;
    private String author;
    private Integer year;
    private GenreDTO genre;
    
    @XmlTransient
    private List<FeedbackDTO> feedbacks = new ArrayList<>();

    public BookDTO() {}
    
    public BookDTO(Book book) {
        this.title = book.getTitle();
        this.author = book.getAuthor();
        this.year = book.getYear();
        this.genre = new GenreDTO(book.getGenre());
//        book.getFeedbacks().forEach(f -> {
//            FeedbackDTO fb = new FeedbackDTO();
//            fb.setBook(this);
//            fb.setMessage(f.getMessage());
//            fb.setUser(new UserDTO(f.getUser()));
//            feedbacks.add(fb);
//        });
    }

    public BookDTO(String title, String author, Integer year, GenreDTO genre) {
        this.title = title;
        this.author = author;
        this.year = year;
        this.genre = genre;
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
    public GenreDTO getGenre() {
        return genre;
    }

    public void setGenre(GenreDTO genre) {
        this.genre = genre;
    }

    @Override
    public List<FeedbackDTO> getFeedbacks() {
        return feedbacks;
    }

    public void setFeedbacks(List<FeedbackDTO> feedbacks) {
        this.feedbacks = feedbacks;
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, author, year, genre);
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
        final BookDTO other = (BookDTO) obj;
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
        return "BookDTO{" + "title=" + title + ", author=" + author + ", year="
                + year + ", genre=" + genre + '}';
    }
}
