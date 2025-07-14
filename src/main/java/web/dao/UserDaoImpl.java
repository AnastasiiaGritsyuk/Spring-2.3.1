package web.dao;

import web.model.User;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Repository
public class UserDaoImpl implements UserDao{

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<User> getAllUsers() {
        return entityManager.createQuery("from User order by id", User.class).getResultList();
    }

    @Override
    public User getUserById(Long id) {
        return entityManager.find(User.class, id);
    }

    @Override
    @Transactional
    public void saveUser(User user) {
        entityManager.persist(user);
    }

    @Override
    @Transactional
    public void updateUser(Long id, User updatedUser) {
        User userToBeUpdated = getUserById(id);
        userToBeUpdated.setName(updatedUser.getName());
        userToBeUpdated.setAge(updatedUser.getAge());
        userToBeUpdated.setEmail(updatedUser.getEmail());
        entityManager.merge(userToBeUpdated);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {

        User userToDelete = getUserById(id);
        if (userToDelete != null) {
            entityManager.remove(userToDelete);
        }
        entityManager.flush();

        entityManager.createNativeQuery(
                        "UPDATE users SET id = id - 1 WHERE id > ?1")
                .setParameter(1, id)
                .executeUpdate();

        resetSequence();
    }

    @Transactional
    public void resetSequence() {

        Number maxId = (Number) entityManager.createNativeQuery(
                        "SELECT MAX(id) FROM users")
                .getSingleResult();

        if (maxId == null) {
            entityManager.createNativeQuery(
                            "ALTER SEQUENCE users_id_seq RESTART WITH 1")
                    .executeUpdate();
        } else {
            entityManager.createNativeQuery(
                            "ALTER SEQUENCE users_id_seq RESTART WITH " + (maxId.longValue() + 1))
                    .executeUpdate();
        }
    }
}