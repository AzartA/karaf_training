package ru.training.karaf.repo;

import org.apache.aries.jpa.template.JpaTemplate;
import org.apache.aries.jpa.template.TransactionType;
import org.apache.commons.io.IOUtils;
import org.postgresql.PGConnection;
import org.postgresql.largeobject.LargeObject;
import org.postgresql.largeobject.LargeObjectManager;
import ru.training.karaf.model.Location;
import ru.training.karaf.model.LocationDO;
import ru.training.karaf.wrapper.QueryParams;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.sql.DataSource;
import javax.validation.ValidationException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class LocationRepo {
    private final JpaTemplate template;
    private final Repo repo;
    private static final Class<LocationDO> CLASS = LocationDO.class;
    private final DataSource dataSource;

    public LocationRepo(JpaTemplate template, DataSource dataSource) {
        this.template = template;
        this.dataSource = dataSource;
        repo = new Repo(template);
    }


    public List<? extends Location> getAll(QueryParams query ) {
        return repo.getAll(query, CLASS);
    }


    public long getCount(QueryParams query) {
        return repo.getCount(query, CLASS);
    }


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


    public Optional<? extends Location> get(long id) {
        return template.txExpr(TransactionType.Required, em -> getById(id, em));
    }


    public Optional<? extends Location> getByName(String name) {
        return template.txExpr(em -> getByName(name, em));
    }


    public Optional<LocationDO> delete(long id) {
        return template.txExpr(em -> getById(id, em).map(l -> {
            deletePlan(id);
            l.getSensors().forEach(s -> s.setLocation(null));
            em.remove(l);
            return l;
        }));
    }

    public Optional<Object> getPlan(long id, OutputStream outputStream) {
        try (Connection conn = dataSource.getConnection()) {
            try {
                conn.setAutoCommit(false);
                LargeObjectManager largeObjectManager = conn.unwrap(PGConnection.class).getLargeObjectAPI();
                long oid = template.txExpr(em -> em.find(LocationDO.class, id)).getPlanOid();
                LargeObject lob = largeObjectManager.open(oid, LargeObjectManager.READ);
                //largeObjectManager.delete(oid);
                IOUtils.copy(lob.getInputStream(), outputStream);
                lob.close();
                conn.commit();
                return Optional.of(new Object());
            } catch (IOException e) {
                //LOG.error("Exception: {}", e.getMessage());
                conn.rollback();
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException ex) {
            //LOG.error("Exception: {}", ex.getMessage());
        }
        return Optional.empty();
    }


    public long setPlan(long id, InputStream inputStream, String type) {
        try (Connection conn = dataSource.getConnection()) {
            try {
                conn.setAutoCommit(false);
                LargeObjectManager largeObjectManager = conn.unwrap(PGConnection.class).getLargeObjectAPI();
                long oid = largeObjectManager.createLO(LargeObjectManager.READ | LargeObjectManager.WRITE);
                LargeObject lob = largeObjectManager.open(oid, LargeObjectManager.WRITE);
                long size = IOUtils.copy(inputStream, lob.getOutputStream());
                lob.close();
                conn.commit();
                template.tx(em -> {
                    LocationDO location = em.find(LocationDO.class, id);
                    location.setPlanOid(oid);
                    location.setPictureType(type);
                });
                return size;
            } catch (IOException ex) {
                //LOG.error("Exception: {}", ex.getMessage());
                conn.rollback();
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException ex) {
            //LOG.error("Exception: {}", ex.getMessage());
        }
        return -1L;
    }


    public Optional<? extends Location> deletePlan(long id) {
        try (Connection conn = dataSource.getConnection()) {
            conn.setAutoCommit(false);
            LargeObjectManager largeObjectManager = conn.unwrap(PGConnection.class).getLargeObjectAPI();
            long oid = template.txExpr(em -> em.find(LocationDO.class, id)).getPlanOid();
            largeObjectManager.delete(oid);
            conn.commit();
            conn.setAutoCommit(true);
            return Optional.of(template.txExpr(em -> {
                LocationDO location = em.find(LocationDO.class, id);
                location.setPlanOid(0L);
                location.setPictureType(null);
                return location;
            }));
        } catch (SQLException ex) {
            //LOG.error("Exception: {}", ex.getMessage());
        }
        return Optional.empty();
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
