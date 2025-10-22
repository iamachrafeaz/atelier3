package ma.fstt.repository;

import jakarta.enterprise.context.RequestScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import ma.fstt.model.Product;

import java.util.List;

@RequestScoped
public class ProductRepository {

    @PersistenceContext
    private EntityManager em;

    public void save(Product product) {
        em.persist(product);
    }

    public List<Product> findAll() {
        return em.createQuery("SELECT p FROM Product p", Product.class)
                .getResultList();
    }

    public List<Product> searchByLabel(String label) {

        Query query = em.createQuery("SELECT p FROM Product p WHERE p.label LIKE  :label", Product.class);
        query.setParameter("label", "%"+label+"%");
        return query.getResultList();
    }


    public Product findById(Long id) {
        return em.find(Product.class, id);
    }
}
