package ru.training.karaf.view;

import ru.training.karaf.model.Entity;
import ru.training.karaf.model.User;
import ru.training.karaf.repo.Repo;
import ru.training.karaf.repo.SensorRepo;
import ru.training.karaf.repo.UserAuthRepo;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class ViewImpl {
    private Repo repo;
    private UserAuthRepo auth;

    public ViewImpl(Repo repo, UserAuthRepo auth) {
        this.repo = repo;
        this.auth = auth;
    }

    public <T> List<? extends T> getAll(
            List<String> by, List<String> order, List<String> field, List<String> cond, List<String> value, int pg, int sz,
            Class<T> type
    ) {
       /* String[] authInfo = new String[2];
        User user = auth.getUser(login);
        Set<Long> roles = user.getRoles().stream().map(Entity::getId).collect(Collectors.toSet());
        if (!roles.contains(1L)) {
            authInfo[0] = "users.id";
            authInfo[1] = Long.toString(user.getId());
        }
        return repo.getAll(by, order, field, cond, value, pg, sz, authInfo, type);*/
        return null;
    }
}
