package bt.edu.gcit.usermicroservice.dao;

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
public class UserDAOImpl implements UserDAO {
    private EntityManager entityManager;
    
    @Autowired
    public UserDAOImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
 
    @Override
    public User save(User user) {
        return entityManager.merge(user);
    }
    
    @Override
    public User findById(Long id) {
        return entityManager.find(User.class, id);
    }
    @Override
    public User findById(int id) {
        return entityManager.find(User.class, id);
    }
    @Override
    public List<User> findAll() {
        return entityManager.createQuery("FROM User", User.class).getResultList();
    }

    @Override
    public void deleteById(Long id) {
        User user = findById(id);
        if (user != null) {
            entityManager.remove(user);
        }
    }

    // @Override
    // @Transactional
    // public User update(User user) {
    //     return entityManager.merge(user); // JPA merge handles updates
    // }

    @Override
    public User findByEmail(String email) {
        TypedQuery<User> query = entityManager.createQuery("from User where email = :email", User.class);
        query.setParameter("email", email);
        List<User> users = query.getResultList();
        System.out.println(users.size());
        if (users.isEmpty()) {
        return null;
        } else {
        System.out.println(users.get(0)+" "+users.get(0).getEmail());
        return users.get(0);
        }
        }
 
    @Override
    public void updateUserEnabledStatus(int id, boolean enabled) {
        User user = entityManager.find(User.class, id);
        System.out.println(user);
        if (user == null) {
        throw new UserNotFoundException("User not found with id " + id);
        }
        user.setEnabled(enabled);
        entityManager.persist(user);
        }
    }