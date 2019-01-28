package ru.training.karaf.repo;

import java.util.List;
import java.util.Optional;
import org.apache.aries.jpa.template.JpaTemplate;
import ru.training.karaf.model.Avatar;
import ru.training.karaf.model.AvatarDO;

public class AvatarRepoImpl implements AvatarRepo {
    private JpaTemplate template;

    public AvatarRepoImpl(JpaTemplate template) {
        this.template = template;
    }

    @Override
    public List<? extends Avatar> getAllAvatars() {
        return template.txExpr(em -> em.createNamedQuery
            (AvatarDO.GET_ALL_AVATARS, AvatarDO.class).getResultList());
    }

    @Override
    public void createAvatar(Avatar avatar) {
        AvatarDO avatarToCreate = new AvatarDO();
        avatarToCreate.setPicture(avatar.getPicture());
        
        template.tx(em -> em.persist(avatarToCreate));
    }

    @Override
    public void updateAvatar(Long id, Avatar avatar) {
        AvatarDO avatarToUpdate =
                template.txExpr(em -> em.find(AvatarDO.class, id));
        
        if (avatarToUpdate != null) {
            avatarToUpdate.setPicture(avatar.getPicture());
            template.tx(em -> em.merge(avatarToUpdate));
        }
    }

    @Override
    public Optional<? extends Avatar> getAvatar(Long id) {
        AvatarDO avatar = template.txExpr(em -> em.find(AvatarDO.class, id));
        if (avatar != null) {
            return Optional.of(avatar);
        }
        return Optional.empty();
    }

    @Override
    public void deleteAvatar(Long id) {
        template.tx(em -> getAvatar(id).ifPresent(em::remove));
    }
}
