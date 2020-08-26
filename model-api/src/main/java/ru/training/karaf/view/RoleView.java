package ru.training.karaf.view;

import java.util.List;
import java.util.Optional;

import ru.training.karaf.model.Role;
import ru.training.karaf.model.User;

public interface RoleView extends View<Role>, ViewType {
    Optional<? extends Role> addUsers(long id, List<Long> userIds, User currentUser);
    Optional<? extends Role> removeUsers(long id, List<Long> userIds, User currentUser);
}
