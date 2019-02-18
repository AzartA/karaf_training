package ru.training.karaf.model;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.Date;
import java.util.List;
import java.util.Set;

public interface User {
    
    UserName getUserName();
    
    String getLibCard();
    
    JsonNode getAddress();
    
    Date getRegDate();
    
    Avatar getAvatar();
    
    Set<? extends Book> getBooks();
    
    List<? extends Feedback> getFeedbacks();
  
}
