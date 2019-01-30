package ru.training.karaf.model;

import java.util.List;

public interface Book {
    public String getTitle();
    public String getAuthor();
    public Integer getYear();
    public Genre getGenre();
    public List<? extends Feedback> getFeedbacks();
}
