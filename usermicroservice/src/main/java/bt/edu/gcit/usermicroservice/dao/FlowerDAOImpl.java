package bt.edu.gcit.usermicroservice.dao;

import bt.edu.gcit.usermicroservice.entity.Flower;
import bt.edu.gcit.usermicroservice.entity.User;

import org.springframework.stereotype.Repository;
// import javax.persistence.EntityManager;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;
import bt.edu.gcit.usermicroservice.exception.UserNotFoundException;

@Repository
public class FlowerDAOImpl implements FlowerDAO {
    private EntityManager entityManager;
    
    @Autowired
    public FlowerDAOImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
 
    @Override
    public Flower save(Flower flower) {
        return entityManager.merge(flower);
    }
    @Override
    public Flower findById(int id) {
        return entityManager.find(Flower.class, id);
    } 
    @Override
    public Flower findById(long id) {
        return entityManager.find(Flower.class, id);
    } 
    @Override
public List<Flower> findByShopOwnerId(Long shopOwnerId) {
    String jpql = "SELECT f FROM Flower f WHERE f.shopOwner.id = :shopOwnerId";
    TypedQuery<Flower> query = entityManager.createQuery(jpql, Flower.class);
    query.setParameter("shopOwnerId", shopOwnerId);
    return query.getResultList(); 
}
public List<Flower> findAll() {
    String query = "SELECT f FROM Flower f";  // JPQL query to fetch all flowers
    return entityManager.createQuery(query, Flower.class).getResultList();
}

  
    }