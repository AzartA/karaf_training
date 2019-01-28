package ru.training.karaf.rest.dto;

import java.util.Objects;
import javax.xml.bind.annotation.XmlTransient;
import ru.training.karaf.model.Book;
import ru.training.karaf.model.Feedback;
import ru.training.karaf.model.User;

public class FeedbackDTO implements Feedback {
    private String message;
    private User user;
    private Book book;

    public FeedbackDTO() {}
    
    public FeedbackDTO(Feedback feedback) {
        this.message = feedback.getMessage();
        this.user = feedback.getUser();
        this.book = feedback.getBook();
    }
    
    @Override
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public User getUser() {
        return user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public Book getBook() {
        return book;
    }
    
    public void setBook(Book book) {
        this.book = book;
    }

    @Override
    public int hashCode() {
        return Objects.hash(message, user, book);
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
        final FeedbackDTO other = (FeedbackDTO) obj;
        if (!Objects.equals(this.message, other.message)) {
            return false;
        }
        if (!Objects.equals(this.user, other.user)) {
            return false;
        }
        if (!Objects.equals(this.book, other.book)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "FeedbackDTO{" + "message=" + message + ", user=" + user +
                ", book=" + book + '}';
    }
}
