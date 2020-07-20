package ru.training.karaf.repo;

import org.apache.aries.jpa.template.JpaTemplate;
import ru.training.karaf.model.Location;
import ru.training.karaf.model.LocationDO;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.List;
import java.util.Optional;

public class LocationRepoImpl implements LocationRepo {
    private JpaTemplate template;

    public LocationRepoImpl(JpaTemplate template) {
        this.template = template;
    }

    @Override
    public List<? extends Location> getAll() {
        return template.txExpr(em -> em.createNamedQuery(LocationDO.GET_ALL, LocationDO.class).getResultList());
    }

    @Override
    public Location create(Location location) {
        LocationDO locationToCreate = new LocationDO(location.getName());
        template.tx(em -> em.persist(locationToCreate));
        return null;
    }

    @Override
    public Optional<? extends Location> update(String name, Location location) {
        template.tx(em -> getByName(name, em).ifPresent(locationToUpdate -> {
            locationToUpdate.setName(location.getName());
            //locationToUpdate.getSensorSet(location.getSensorSet());
            em.merge(locationToUpdate);
        }));
        return Optional.empty();
    }

    @Override
    public Optional<? extends Location> get(String name) {
        return template.txExpr(em -> getByName(name, em));
    }

    @Override
    public String delete(String name) {
        template.tx(em -> getByName(name, em).ifPresent(em::remove));
        return "";
    }

    private Optional<LocationDO> getByName(String name, EntityManager em) {
        try {
            return Optional.of(em.createNamedQuery(LocationDO.GET_BY_NAME, LocationDO.class).setParameter("name", name)
                    .getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }
}
