package ru.training.karaf.model;

import java.util.List;

public interface Book {
    
    String getTitle();
    
    String getAuthor();
    
    Integer getYear();
    
    Genre getGenre();
    
    List<? extends Feedback> getFeedbacks();
  
}
