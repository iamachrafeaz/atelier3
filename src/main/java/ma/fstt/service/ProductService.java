package ma.fstt.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import ma.fstt.model.Product;
import ma.fstt.repository.ProductRepository;

import java.util.List;


@Named
@RequestScoped
public class ProductService {
    @Inject
    ProductRepository productRepository;

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public List<Product> searchByLabel(String label) {
        return productRepository.searchByLabel(label);
    }

    public Product findProductById(Long id) {
        return productRepository.findById(id);
    }
}