package ma.fstt.repository;


import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import ma.fstt.model.Cart;

@Named
@RequestScoped
public class CartRepository {

    @PersistenceContext
    private EntityManager em;


    public Long save(Cart object) {
        Cart managedCart = em.merge(object);
        return managedCart.getId();
    }

    public Cart findByUserId(Long id) {
        return em.createQuery("SELECT c FROM Cart c WHERE c.user.id = :id", Cart.class)
                .setParameter("id", id)
                .getResultStream()
                .findFirst()
                .orElse(null);
    }

    public void delete(Cart object) {
        Cart managedCart = em.find(Cart.class, object.getId());
        if (managedCart != null) {
            em.remove(managedCart);
        }
    }
}
