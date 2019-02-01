package ru.training.karaf.rest.dto;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import javax.xml.bind.annotation.XmlTransient;
import ru.training.karaf.model.User;

public class UserDTO implements User {
    private UserNameDTO userName;
    private String libCard;
    private JsonNode address;
    private Date regDate;
    
    @XmlTransient
    private AvatarDTO avatar;
    
    @XmlTransient
    private Set<BookDTO> books = new HashSet<>();
    
    @XmlTransient
    private List<FeedbackDTO> feedbacks = new ArrayList<>();

    public UserDTO() {}
    
    public UserDTO(User user) {
        this.address = user.getAddress();
        if (user.getAvatar() != null) {
            this.avatar = new AvatarDTO(user.getAvatar());
        }
        this.libCard = user.getLibCard();
        this.regDate = user.getRegDate();
        this.userName = new UserNameDTO(user.getUserName());
        user.getBooks().forEach(b -> {
            BookDTO book = new BookDTO(b);
            books.add(book);
        });
        user.getFeedbacks().forEach(f -> {
            FeedbackDTO feedback = new FeedbackDTO();
            feedback.setBook(new BookDTO(f.getBook()));
            feedback.setMessage(f.getMessage());
            feedback.setUser(this);
            feedbacks.add(feedback);
        });
    }

    @Override
    public UserNameDTO getUserName() {
        return userName;
    }

    public void setUserName(UserNameDTO userName) {
        this.userName = userName;
    }

    @Override
    public String getLibCard() {
        return libCard;
    }

    public void setLibCard(String libCard) {
        this.libCard = libCard;
    }

    @Override
    public JsonNode getAddress() {
        return address;
    }

    public void setAddress(JsonNode address) {
        this.address = address;
    }

    @Override
    public Date getRegDate() {
        return regDate;
    }

    public void setRegDate(Date regDate) {
        this.regDate = regDate;
    }

    @Override
    public AvatarDTO getAvatar() {
        return avatar;
    }

    public void setAvatar(AvatarDTO avatar) {
        this.avatar = avatar;
    }

    @Override
    public Set<BookDTO> getBooks() {
        return books;
    }

    public void setBooks(Set<BookDTO> books) {
        this.books = books;
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
        return Objects.hash(userName, libCard, address, regDate, avatar);
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
        final UserDTO other = (UserDTO) obj;
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
        return "UserDTO{" + "userName=" + userName + ", libCard=" + libCard +
                ", address=" + address + ", regDate=" + regDate + ", avatar="
                + avatar + '}';
    }
}
