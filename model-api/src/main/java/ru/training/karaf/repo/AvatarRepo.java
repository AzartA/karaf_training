package ru.training.karaf.repo;

import java.util.List;
import java.util.Optional;
import ru.training.karaf.model.Avatar;

public interface AvatarRepo {
    List<? extends Avatar> getAllAvatars();
    void createAvatar(Avatar avatar);
    void updateAvatar(Long id, Avatar avatar);
    Optional<? extends Avatar> getAvatar(Long id);
    void deleteAvatar(Long id);
}
