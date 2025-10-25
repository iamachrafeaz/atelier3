package ma.fstt.bean;

import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;
import ma.fstt.model.Product;
import ma.fstt.model.User;

import java.io.Serializable;

@Named
@SessionScoped
@Getter
@Setter
public class UserSessionBean implements Serializable {

    private User currentUser;

    private Product currentProduct;

    private Integer currentProductQuantity;

    public void login(User userSessionResponse) {
        this.currentUser = userSessionResponse;
    }

    public void logout() {
        this.currentUser = null;
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }
}
