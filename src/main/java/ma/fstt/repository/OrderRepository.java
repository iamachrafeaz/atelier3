package ma.fstt.repository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import ma.fstt.model.Order;

import java.util.List;

@Named
@ApplicationScoped
public class OrderRepository {

    @PersistenceContext
    private EntityManager em;

    public void save(Order object) {
        em.persist(object);
    }

    public void deleteById(Long id) {
        em.remove(em.find(Order.class, id));
    }

    public List<Order> findByUserId(Long id) {
        return em.createQuery("select o FROM Order o WHERE o.user.id = :userId").setParameter("userId", id).getResultList();
    }

    public Order findById(Long id) {
        return em.find(Order.class, id);
    }
}
