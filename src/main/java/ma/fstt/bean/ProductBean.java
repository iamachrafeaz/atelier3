package ma.fstt.bean;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;
import ma.fstt.model.Product;
import ma.fstt.service.ProductService;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Named
@SessionScoped
public class ProductBean implements Serializable {

    @Inject
    private ProductService produitService;

    private List<Product> products;

    @PostConstruct
    public void init() {
        products = produitService.getAllProducts();
    }
}
