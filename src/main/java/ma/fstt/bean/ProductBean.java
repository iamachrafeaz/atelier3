package ma.fstt.bean;
// ProductBean.java

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import ma.fstt.model.Product;
import ma.fstt.service.ProductService;

import java.util.List;

@Named("productBean")
@RequestScoped
public class ProductBean {
    private List<Product> products;
    private String searchTerm;

    @Inject
    private ProductService service;

    @PostConstruct
    public void init() {
        products = service.findAll();
    }

    public void search() {
        products = service.searchByLabel(searchTerm);
    }

    public List<Product> getProducts() { return products; }
    public String getSearchTerm() { return searchTerm; }
    public void setSearchTerm(String searchTerm) { this.searchTerm = searchTerm; }
}
