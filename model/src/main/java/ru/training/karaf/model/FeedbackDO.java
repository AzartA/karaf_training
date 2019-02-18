package ru.training.karaf.model;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Table(name = "FEEDBACKDO", uniqueConstraints =
        {@UniqueConstraint(columnNames = {"USER_ID", "BOOK_ID"})})
@Entity
public class FeedbackDO implements Feedback, Serializable {
    
    @Id
    @GeneratedValue
    @Column(name = "ID")
    private Long id;
    
    @Column(name = "MESSAGE")
    @Lob
    @Basic(fetch = FetchType.LAZY)
    private String message;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private UserDO user;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BOOK_ID")
    private BookDO book;

    public FeedbackDO() {}
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    @Override
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public UserDO getUser() {
        return user;
    }

    public void setUser(UserDO user) {
        this.user = user;
    }
    
    @Override
    public BookDO getBook() {
        return book;
    }
    
    public void setBook(BookDO book) {
        this.book = book;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, message, user, book);
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
        final FeedbackDO other = (FeedbackDO) obj;
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
        return "FeedbackDO{" + "id=" + id + ", message=" + message + ", user="
                + user + ", book=" + book + '}';
    }
}
