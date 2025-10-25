package ma.fstt.bean;

import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import ma.fstt.model.Cart;
import ma.fstt.model.CartItem;
import ma.fstt.model.Product;
import ma.fstt.service.CartService;

import java.io.Serializable;

@Named
@ViewScoped
public class CartBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private CartService cartService;

    @Inject
    private UserSessionBean userSession;

    private Cart cart;

    private double cartTotal;

    @PostConstruct
    public void init() {
        loadCart();
    }

    public void addToCart() {
        if(userSession.isLoggedIn()) {
            cartService.saveOrUpdateCart(userSession.getCurrentProduct(), userSession.getCurrentProductQuantity());
        }else{
            FacesContext.getCurrentInstance()
                    .addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Must be logged in!"));

        }
    }

    private void loadCart() {
        if (userSession.getCurrentUser() != null) {
            cart = cartService.findByUserId(userSession.getCurrentUser().getId());
            calculateTotal();
        }
    }

    public void increaseQuantity(Long productId) {
        try {
            CartItem item = findCartItemByProductId(productId);
            if (item != null) {
                Product product = item.getProduct();
                int newQuantity = item.getQuantity() + 1;

                if (newQuantity > product.getInventoryQty() + item.getQuantity()) {
                    addMessage(FacesMessage.SEVERITY_WARN, "Warning",
                            "Cannot increase quantity. Insufficient inventory.");
                    return;
                }

                cartService.saveOrUpdateCart(product, newQuantity);
                loadCart();
                addMessage(FacesMessage.SEVERITY_INFO, "Success", "Quantity updated successfully");
            }
        } catch (Exception e) {
            addMessage(FacesMessage.SEVERITY_ERROR, "Error", "Failed to update quantity");
        }
    }

    public void decreaseQuantity(Long productId) {
        try {
            CartItem item = findCartItemByProductId(productId);
            if (item != null && item.getQuantity() > 1) {
                Product product = item.getProduct();
                int newQuantity = item.getQuantity() - 1;

                cartService.saveOrUpdateCart(product, newQuantity);
                loadCart();
                addMessage(FacesMessage.SEVERITY_INFO, "Success", "Quantity updated successfully");
            }
        } catch (Exception e) {
            addMessage(FacesMessage.SEVERITY_ERROR, "Error", "Failed to update quantity");
        }
    }

    public void removeItem(Long productId) {
        try {
            cartService.deleteCartItem(productId);
            loadCart();
            addMessage(FacesMessage.SEVERITY_INFO, "Success", "Item removed from cart");
        } catch (Exception e) {
            addMessage(FacesMessage.SEVERITY_ERROR, "Error", "Failed to remove item");
        }
    }

    private void calculateTotal() {
        cartTotal = 0.0;
        if (cart != null && cart.getCartItems() != null) {
            for (CartItem item : cart.getCartItems()) {
                cartTotal += item.getQuantity() * item.getProduct().getPrice();
            }
        }
    }

    private CartItem findCartItemByProductId(Long productId) {
        if (cart != null && cart.getCartItems() != null) {
            for (CartItem item : cart.getCartItems()) {
                if (item.getProduct().getId().equals(productId)) {
                    return item;
                }
            }
        }
        return null;
    }

    private void addMessage(FacesMessage.Severity severity, String summary, String detail) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(severity, summary, detail));
    }

    public String getImagePath(String imageName) {
        if (imageName == null || imageName.isEmpty()) {
            return "";
        }
        return "/uploads/" + imageName;
    }

    public boolean isCartEmpty() {
        return cart == null || cart.getCartItems() == null || cart.getCartItems().isEmpty();
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public double getCartTotal() {
        return cartTotal;
    }

    public void setCartTotal(double cartTotal) {
        this.cartTotal = cartTotal;
    }
}