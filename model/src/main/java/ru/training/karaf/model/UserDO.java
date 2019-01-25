package ru.training.karaf.model;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@NamedQueries({
    @NamedQuery(name = UserDO.GET_ALL, query = "SELECT u FROM UserDO AS u"),
    @NamedQuery(name = UserDO.GET_BY_LIB_CARD,
            query = "SELECT u FROM UserDO AS u WHERE u.libCard = :libCard")
})

public class UserDO implements User {
    public static final String GET_ALL = "Users.getAll";
    public static final String GET_BY_LIB_CARD = "Users.getByLibCard";
    
    @Id
    @GeneratedValue
    @Column(name = "ID")
    private Long id;
    
    @Embedded
    UserNameDO userName;
    
    @Column(name = "LIB_CARD", nullable = false, unique = true)
    private String libCard;
    
    @Column(name = "ADDRESS")
    private JsonNode address;
    
    @Column(name = "REG_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date regDate;
    
    @OneToOne
    @JoinColumn(name = "AVATAR_ID")
    private AvatarDO avatar;
    
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "USERDO_BOOKDO",
            joinColumns = @JoinColumn(name = "USER_ID"),
            inverseJoinColumns = @JoinColumn(name = "BOOK_ID"))
    private List<BookDO> books;

    public UserDO() {}

    @Override
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
    
    @Override
    public UserName getUserName() {
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
    public Avatar getAvatar() {
        return avatar;
    }
    
    public void setAvatar(AvatarDO avatar) {
        this.avatar = avatar;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, userName, libCard, address, regDate);
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
        if (!Objects.equals(this.books, other.books)) {
            return false;
        }
        return true;
    } 

    @Override
    public String toString() {
        return "UserDO{" + "id=" + id + ", userName=" + userName + ", libCard="
                + libCard + ", address=" + address + ", regDate=" + regDate +
                ", avatar=" + avatar + ", books=" + books + '}';
    }
}
