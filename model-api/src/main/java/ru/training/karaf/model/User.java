package ru.training.karaf.model;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.Date;
import java.util.List;

public interface User {
    UserName getUserName();
    String getLibCard();
    JsonNode getAddress();
    Date getRegDate();
    Avatar getAvatar();
    List<? extends Book> getBooks();
}
