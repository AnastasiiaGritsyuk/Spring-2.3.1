package web.dao;

import org.springframework.transaction.annotation.Transactional;
import web.model.User;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class UserDaoImpl implements UserDao{

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public List<User> getAllUsers() {
        try {
            return entityManager.createQuery("from User order by id", User.class)
                    .getResultList();
        } catch (Exception e) {
            throw new RuntimeException("Failed to get users", e);
        }
    }

    @Override
    @Transactional
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
        if (userToBeUpdated != null) {
            userToBeUpdated.setName(updatedUser.getName());
            userToBeUpdated.setAge(updatedUser.getAge());
            userToBeUpdated.setEmail(updatedUser.getEmail());
            entityManager.merge(userToBeUpdated);
        }
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        User userToDelete = getUserById(id);
        if (userToDelete != null) {
            entityManager.remove(userToDelete);
        }
        entityManager.flush();

        List<Long> idsToUpdate = entityManager.createQuery(
                        "SELECT u.id FROM User u WHERE u.id > :id ORDER BY u.id", Long.class)
                .setParameter("id", id)
                .getResultList();

        for (Long currentId : idsToUpdate) {
            entityManager.createNativeQuery(
                            "UPDATE users SET id = ?1 WHERE id = ?2")
                    .setParameter(1, currentId - 1)
                    .setParameter(2, currentId)
                    .executeUpdate();
        }


        resetSequence();
        entityManager.clear();
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