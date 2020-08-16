package ru.training.karaf.view;

import java.util.List;
import java.util.Optional;

import ru.training.karaf.model.Role;

public interface RoleView extends View<Role> {
    Optional<? extends Role> addUsers(long id, List<Long> userIds, String login);
    Optional<? extends Role> removeUsers(long id, List<Long> userIds, String login);
}
