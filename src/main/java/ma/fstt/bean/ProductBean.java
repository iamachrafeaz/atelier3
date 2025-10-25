package ma.fstt.bean;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;
import ma.fstt.model.Product;
import ma.fstt.service.CartService;
import ma.fstt.service.ProductService;

import java.util.List;

@Getter
@Setter
@Named("productBean")
@RequestScoped
public class ProductBean {
    private List<Product> products;
    private String searchTerm;
    private Product selectedProduct;
    private Integer quantity;

    @Inject
    private ProductService service;

    @Inject
    private UserSessionBean userSession;

    @PostConstruct
    public void init() {
            this.quantity = 1;
        String idParam = FacesContext.getCurrentInstance()
                .getExternalContext()
                .getRequestParameterMap()
                .get("id");

        if (idParam != null) {
            selectedProduct = service.findById(Long.parseLong(idParam));
            userSession.setCurrentProductQuantity(1);
            userSession.setCurrentProduct(selectedProduct);
        } else {
            products = service.findAll();
        }
    }

    public void search() {
        products = service.searchByLabel(searchTerm);
    }

    public void decreaseQty()
    {
        quantity--;
        userSession.setCurrentProductQuantity(quantity);
    }

    public void increaseQty()
    {
        quantity++;
        userSession.setCurrentProductQuantity(quantity);
    }
}
