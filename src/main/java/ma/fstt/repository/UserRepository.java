package ma.fstt.repository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import ma.fstt.model.User;

import java.util.List;

@ApplicationScoped
@Named
public class UserRepository {

    @PersistenceContext(unitName = "default")
    EntityManager em;

    @Transactional
    public void save(User user) {
        if (user.getId() == null) {
            em.persist(user);
        } else {
            em.merge(user);
        }
    }


    public void deleteById(Long id) {
        User c = em.find(User.class, id);
        if (c != null) em.remove(c);
    }

    public User findByEmail(String email) {
        User user = em.createQuery("SELECT u FROM User u WHERE u.email = :email", User.class)
                .setParameter("email", email)
                .getSingleResult();

        return user;
    }

    public List<User> findAll() {
        return em.createQuery("SELECT u FROM User u", User.class).getResultList();
    }

    public Boolean existsByEmail(String email) {
        List<User> users = em.createQuery("SELECT u FROM User u WHERE u.email = :email", User.class)
                .setParameter("email", email)
                .getResultList();
        return !users.isEmpty();
    }
}
