package ru.kata.spring.boot_security.demo.dao;

import org.springframework.stereotype.Repository;
import ru.kata.spring.boot_security.demo.model.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Repository
public class UserDaoImpl implements UserDao {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public User getUserById(long id) {
        return entityManager.find(User.class, id);
    }

    @Override
    public User getUserByEmail(String email) {
//        return entityManager.find(User.class, username);

//        Query query = entityManager.createQuery(String.format("from User u where u.email = %s", username));
//        return (User) query.getResultList().get(0);

        User user = entityManager.createQuery("SELECT u from User u WHERE u.email = :email", User.class).
                setParameter("email", email).getSingleResult();
        return user;
    }

    @Override
    public void deleteUser(long id) {
        entityManager.remove(getUserById(id));
    }

    @Override
    public void saveOrUpdateUser(User user) {
        entityManager.merge(user);
    }

    @Override
    public List<User> getAllUsers() {
        Query query = entityManager.createQuery("from User u order by u.id");
        return query.getResultList();
    }
}
