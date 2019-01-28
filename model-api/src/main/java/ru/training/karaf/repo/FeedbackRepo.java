package ru.training.karaf.repo;

import java.util.List;
import java.util.Optional;
import ru.training.karaf.model.Feedback;

public interface FeedbackRepo {
    List<? extends Feedback> getAllFeedbacks();
    void createFeedback(Feedback feedback);
    void updateFeedback(Long id, Feedback feedback);
    Optional<? extends Feedback> getFeedback(Long id);
    void deleteFeedback(Long id);
}
