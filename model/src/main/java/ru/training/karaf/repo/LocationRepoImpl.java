package ru.training.karaf.repo;

import java.io.OutputStream;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.sql.DataSource;
import javax.validation.ValidationException;

import org.apache.aries.jpa.template.JpaTemplate;
import org.apache.aries.jpa.template.TransactionType;
import ru.training.karaf.model.Location;
import ru.training.karaf.model.LocationDO;

public class LocationRepoImpl implements LocationRepo {
    private final JpaTemplate template;
    private final DataSource dataSource;

    public LocationRepoImpl(JpaTemplate template, DataSource dataSource) {
        this.template = template;
        this.dataSource = dataSource;
    }

    @Override
    public List<? extends Location> getAll() {
        return template.txExpr(em -> em.createNamedQuery(LocationDO.GET_ALL, LocationDO.class).getResultList());
    }

    @Override
    public Optional<? extends Location> create(Location location) {
        LocationDO locationToCreate = new LocationDO(location.getName());
        return template.txExpr(em -> {
            if (!(getByName(location.getName(), em).isPresent())) {
                em.persist(locationToCreate);
                return Optional.of(locationToCreate);
            }
            return Optional.empty();
        });
    }

    @Override
    public Optional<? extends Location> update(long id, Location location) {
        return template.txExpr(em -> {
            List<LocationDO> l = getByIdOrName(id, location.getName(), em);
            if (l.size() > 1) {
                throw new ValidationException("This name is already exist");
            }
            if (!l.isEmpty()) {
                LocationDO locationToUpdate = l.get(0);
                if (locationToUpdate.getId() == id) {
                    locationToUpdate.setName(location.getName());
                    em.merge(locationToUpdate);
                    return Optional.of(locationToUpdate);
                }
            }
            return Optional.empty();
        });
    }

    @Override
    public Optional<? extends Location> get(long id) {
        return template.txExpr(TransactionType.Required, em -> getById(id, em));
    }

    @Override
    public Optional<? extends Location> getByName(String name) {
        return template.txExpr(em -> getByName(name, em));
    }

    @Override
    public Optional<LocationDO> delete(long id) {
        return template.txExpr(em -> getById(id, em).map(l -> {
            l.getSensorSet().forEach(s -> s.setLocation(null));
            em.remove(l);
            return l;
        }));
    }

    public OutputStream getPlan(long id, OutputStream outputStream){

    }



    private Optional<LocationDO> getByName(String name, EntityManager em) {
        try {
            return Optional.of(em.createNamedQuery(LocationDO.GET_BY_NAME, LocationDO.class).setParameter("name", name)
                    .getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    private Optional<LocationDO> getById(long id, EntityManager em) {
        return Optional.ofNullable(em.find(LocationDO.class, id));
    }

    private List<LocationDO> getByIdOrName(long id, String name, EntityManager em) {
        return em.createNamedQuery(LocationDO.GET_BY_ID_OR_NAME, LocationDO.class)
                .setParameter("id", id).setParameter("name", name)
                .getResultList();
    }


}
