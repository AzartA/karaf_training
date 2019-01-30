package ru.training.karaf.model;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.Date;
import java.util.List;
import java.util.Set;

public interface User {
    public UserName getUserName();
    public String getLibCard();
    public JsonNode getAddress();
    public Date getRegDate();
    public Avatar getAvatar();
    public Set<? extends Book> getBooks();
    public List<? extends Feedback> getFeedbacks();
}
