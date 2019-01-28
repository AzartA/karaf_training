package ru.training.karaf.repo;

import java.util.List;
import java.util.Optional;
import org.apache.aries.jpa.template.JpaTemplate;
import ru.training.karaf.model.BookDO;
import ru.training.karaf.model.Feedback;
import ru.training.karaf.model.FeedbackDO;
import ru.training.karaf.model.UserDO;

public class FeedbackRepoImpl implements FeedbackRepo {
    private JpaTemplate template;

    public FeedbackRepoImpl(JpaTemplate template) {
        this.template = template;
    }

    @Override
    public List<? extends Feedback> getAllFeedbacks() {
        return template.txExpr(em -> em.createNamedQuery
            (FeedbackDO.GET_ALL_FEEDBACKS, FeedbackDO.class).getResultList());
    }

    @Override
    public void createFeedback(Feedback feedback) {
        FeedbackDO fbToCreate = new FeedbackDO();
        fbToCreate.setBook((BookDO)feedback.getBook());
        fbToCreate.setMessage(feedback.getMessage());
        fbToCreate.setUser((UserDO)feedback.getUser());
        
        template.tx(em -> em.persist(fbToCreate));
    }

    @Override
    public void updateFeedback(Long id, Feedback feedback) {
        FeedbackDO fbToUpdate = template.txExpr
            (em -> em.find(FeedbackDO.class, id));
        if (fbToUpdate != null) {
            fbToUpdate.setBook((BookDO)feedback.getBook());
            fbToUpdate.setMessage(feedback.getMessage());
            fbToUpdate.setUser((UserDO)feedback.getUser());
            
            template.tx(em -> em.merge(fbToUpdate));
        }
    }

    @Override
    public Optional<? extends Feedback> getFeedback(Long id) {
        FeedbackDO fb = template.txExpr(em -> em.find(FeedbackDO.class, id));
        if (fb != null) {
            return Optional.of(fb);
        }
        return Optional.empty();
    }

    @Override
    public void deleteFeedback(Long id) {
        template.tx(em -> getFeedback(id).ifPresent(em::remove));
    }
}
