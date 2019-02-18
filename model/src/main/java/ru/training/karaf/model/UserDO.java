package ru.training.karaf.model;

import com.fasterxml.jackson.databind.JsonNode;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


@Table(name = "USERDO")
@Entity
@NamedQueries({
    @NamedQuery(name = UserDO.GET_ALL_USERS,
            query = "SELECT u FROM UserDO AS u"),
    @NamedQuery(name = UserDO.GET_USER_BY_LIB_CARD,
            query = "SELECT u FROM UserDO AS u WHERE u.libCard = :libCard")
})
@NamedNativeQuery(name = UserDO.REMOVE_AVATAR,
        query = "DELETE FROM AVATARDO WHERE id = ?")

public class UserDO implements User, Serializable {
    
    public static final String GET_ALL_USERS = "Users.getAllUsers";
    public static final String GET_USER_BY_LIB_CARD = "Users.getUserByLibCard";
    public static final String REMOVE_AVATAR = "Users.removeAvatar";
    
    @Id
    @GeneratedValue
    @Column(name = "ID")
    private Long id;
    
    @Embedded
    UserNameDO userName;
    
    @Column(name = "LIB_CARD", nullable = false, unique = true)
    private String libCard;
    
    @Column(name = "ADDRESS", columnDefinition = "jsonb")
    @Convert(converter = ru.training.karaf.converter.JsonConverter.class)
    private JsonNode address;
    
    @Column(name = "REG_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date regDate;
    
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "AVATAR_ID")
    private AvatarDO avatar;
    
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "USERDO_BOOKDO",
            joinColumns = @JoinColumn(name = "USER_ID"),
            inverseJoinColumns = @JoinColumn(name = "BOOK_ID"))
    private Set<BookDO> books = new HashSet<>();
    
    @OneToMany(cascade = CascadeType.ALL,
            mappedBy = "user",
            orphanRemoval = true)
    private List<FeedbackDO> feedbacks = new ArrayList<>();

    public UserDO() {}
    
    public UserDO(User user) {
        this.address = user.getAddress();
        if (user.getAvatar() != null) {
            this.avatar = new AvatarDO(user.getAvatar());
        }
        this.libCard = user.getLibCard();
        this.regDate = user.getRegDate();
        this.userName = new UserNameDO(user.getUserName());
        user.getBooks().forEach(b -> {
            BookDO book = new BookDO(b);
            books.add(book);
        });
        user.getFeedbacks().forEach(f -> {
            FeedbackDO feedback = new FeedbackDO();
            feedback.setBook(new BookDO(f.getBook()));
            feedback.setMessage(f.getMessage());
            feedback.setUser(this);
            feedbacks.add(feedback);
        });
    }

    @Override
    public List<FeedbackDO> getFeedbacks() {
        return feedbacks;
    }

    public void setFeedbacks(List<FeedbackDO> feedbacks) {
        this.feedbacks = feedbacks;
    }
    
    public void addFeedback(FeedbackDO feedback) {
        feedbacks.add(feedback);
        feedback.setUser(this);
    }
    
    public void removeFeedback(FeedbackDO feedback) {
        feedbacks.remove(feedback);
        feedback.setUser(null);
    }
    
    @Override
    public Set<BookDO> getBooks() {
        return books;
    }

    public void setBooks(Set<BookDO> books) {
        this.books = books;
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    @Override
    public UserNameDO getUserName() {
        return userName;
    }
    
    public void setUserName(UserNameDO userName) {
        this.userName = userName;
    }
    
    @Override
    public JsonNode getAddress() {
        return address;
    }
    
    public void setAddress(JsonNode address) {
        this.address = address;
    }
    
    @Override
    public String getLibCard() {
        return libCard;
    }
    
    public void setLibCard(String libCard) {
        this.libCard = libCard;
    }
    
    @Override
    public Date getRegDate() {
        return regDate;
    }
    
    public void setRegDate(Date regDate) {
        this.regDate = regDate;
    }
    
    @Override
    public AvatarDO getAvatar() {
        return avatar;
    }
    
    public void setAvatar(AvatarDO avatar) {
        this.avatar = avatar;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, avatar, userName, libCard, address, regDate);
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
        final UserDO other = (UserDO) obj;
        if (!Objects.equals(this.libCard, other.libCard)) {
            return false;
        }
        if (!Objects.equals(this.userName, other.userName)) {
            return false;
        }
        if (!Objects.equals(this.address, other.address)) {
            return false;
        }
        if (!Objects.equals(this.regDate, other.regDate)) {
            return false;
        }
        if (!Objects.equals(this.avatar, other.avatar)) {
            return false;
        }
        return true;
    } 

    @Override
    public String toString() {
        return "UserDO{" + "id=" + id + ", userName=" + userName + ", libCard="
                + libCard + ", address=" + address + ", regDate=" + regDate +
                ", avatar=" + avatar + '}';
    }
}
