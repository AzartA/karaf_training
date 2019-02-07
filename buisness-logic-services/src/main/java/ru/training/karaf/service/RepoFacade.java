package ru.training.karaf.service;

import java.util.List;
import ru.training.karaf.repo.Repo;

public class RepoFacade {
    
    private List<Repo> repos;

    public void setRepos(List<Repo> repos) {
        this.repos = repos;
    }

    public void init() {
        repos.forEach(r -> {
            System.err.println(r.getClass().getCanonicalName());
        });
    }
    
    public Object retrieveEntity(Class type, Long id) {
        System.err.println(repos.get(0).getById(id));
        System.err.println(repos.get(1).getById(id));
        System.err.println(repos.get(2).getById(id));
        return null;
    }
    
}
