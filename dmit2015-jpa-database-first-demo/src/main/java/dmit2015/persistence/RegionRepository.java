package dmit2015.persistence;

import dmit2015.entity.Region;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class RegionRepository {

    // Assign a unitName if there are more than one persistence unit defined in persistence.xml
    @PersistenceContext //(unitName="pu-name-in-persistence.xml")
    private EntityManager _entityManager;

    @Transactional
    public void add(@Valid Region newRegion) {
        // If the primary key is not an identity column then write code below here to generate a new primary key value
        BigInteger nextId = _entityManager
                .createQuery("SELECT MAX(r.regionId) + 10 FROM Region r", BigInteger.class)
                .getSingleResult();
        newRegion.setRegionId(nextId);

        _entityManager.persist(newRegion);
    }

    public Optional<Region> findById(BigInteger regionId) {
        Optional<Region> optionalRegion = Optional.empty();
        try {
            Region querySingleResult = _entityManager.find(Region.class, regionId);
            if (querySingleResult != null) {
                optionalRegion = Optional.of(querySingleResult);
            }
        } catch (Exception ex) {
            // regionId value not found
        }
        return optionalRegion;
    }

    public List<Region> findAll() {
        return _entityManager.createQuery("SELECT o FROM Region o ", Region.class)
                .getResultList();
    }

    @Transactional
    public Region update(BigInteger id, @Valid Region updatedRegion) {
        Optional<Region> optionalRegion = findById(id);
        if (optionalRegion.isEmpty()) {
            String errorMessage = String.format("The id %s does not exists in the system.", id);
            throw new RuntimeException(errorMessage);
        }
        // The @Version field will be ignored and no OptimisticLockException thrown if you update an entity that was fetched in this methhod.
        return _entityManager.merge(updatedRegion);
    }

    @Transactional
    public void delete(Region existingRegion) {
        // Write code to throw a RuntimeException if this entity contains child records

        if (_entityManager.contains(existingRegion)) {
            _entityManager.remove(existingRegion);
        } else {
            _entityManager.remove(_entityManager.merge(existingRegion));
        }
    }

    @Transactional
    public void deleteById(BigInteger regionId) {
        Optional<Region> optionalRegion = findById(regionId);
        if (optionalRegion.isPresent()) {
            Region existingRegion = optionalRegion.orElseThrow();
            // Write code to throw a RuntimeException if this entity contains child records

            _entityManager.remove(existingRegion);
        }
    }

    public long count() {
        return _entityManager.createQuery("SELECT COUNT(o) FROM Region o", Long.class).getSingleResult();
    }

    @Transactional
    public void deleteAll() {
        _entityManager.flush();
        _entityManager.clear();
        _entityManager.createQuery("DELETE FROM Region").executeUpdate();
    }

}