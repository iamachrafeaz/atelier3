package ma.fstt.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import ma.fstt.model.Cart;
import ma.fstt.model.CartItem;
import ma.fstt.model.Product;
import ma.fstt.model.User;
import ma.fstt.repository.CartRepository;
import ma.fstt.repository.ProductRepository;
import ma.fstt.repository.UserRepository;
import ma.fstt.utils.UserSessionBean;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Named
@ApplicationScoped
public class CartService {

    @Inject
    CartRepository cartRepository;

    @Inject
    UserRepository userRepository;

    @Inject
    ProductRepository productRepository;

    @Inject
    UserSessionBean userSession;

    public void saveOrUpdateCart(Long productId, Integer quantity) {
        User user = findUserBySession(userSession.getCurrentUser());
        Cart cart = findOrCreateCartForUser(userSession.getCurrentUser());
        Product product = validateAndFetchProduct(productId, quantity);
        addOrUpdateCartItem(cart, product, quantity);
        updateProductInventory(product, product.getInventoryQty() - quantity);
        persistCart(cart);
    }

    public Cart findByUserId(Long id) {
        return cartRepository.findByUserId(id);
    }

    private User findUserBySession(User sessionUser) {
        User user = userRepository.findByEmail(sessionUser.getEmail());
        if (user == null) {
            showError("User not found");
        }
        return user;
    }

    private Cart findOrCreateCartForUser(User user) {
        Cart cart = findByUserId(user.getId());
        if (cart == null) {
            cart = createNewCart(user);
        } else if (cart.getCartItems() == null) {
            cart.setCartItems(new HashSet<>());
        }
        return cart;
    }

    private Cart createNewCart(User user) {
        Cart cart = new Cart();
        cart.setUser(user);
        cart.setCartItems(new HashSet<>());
        Long id = cartRepository.save(cart);
        cart.setId(id);
        return cart;
    }

    private Product validateAndFetchProduct(Long productId, Integer requestedQty) {
        Product product = productRepository.findById(productId);
        if (product == null) {
            showError("Product not found");
        }

        if (product.getInventoryQty() < requestedQty) {
            showError("Insufficient quantity");
        }

        return product;
    }

    private void addOrUpdateCartItem(Cart cart, Product product, Integer quantity) {
        Optional<CartItem> existingItemOpt = cart.getCartItems()
                .stream()
                .filter(item -> item.getProduct().getId().equals(product.getId()))
                .findFirst();

        if (existingItemOpt.isPresent()) {
            updateExistingItem(existingItemOpt.get(), product, quantity);
        } else {
            addNewCartItem(cart, product, quantity);
        }
    }

    private void updateExistingItem(CartItem item, Product product, int addedQty) {
        int newQty = addedQty;
        if (newQty > product.getInventoryQty()) {
            showError("Quantity exceeds the maximum allowed quantity");
        }
        item.setQuantity(newQty);
    }

    private void addNewCartItem(Cart cart, Product product, int qty) {
        CartItem newItem = new CartItem();
        newItem.setCart(cart);
        newItem.setProduct(product);
        newItem.setQuantity(qty);
        cart.getCartItems().add(newItem);
    }

    private void updateProductInventory(Product product, int newQty) {
        product.setInventoryQty(newQty);
        productRepository.save(product);
    }

    private void persistCart(Cart cart) {
        cartRepository.save(cart);
    }

    public void deleteCartItem(Long productId) {
        Cart cart = findByUserId(userSession.getCurrentUser().getId());

        if (cart == null || cart.getCartItems() == null) {
        }

        Optional<CartItem> itemOpt = cart.getCartItems()
                .stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst();

        if (itemOpt.isEmpty()) {
            showError("CartItem not found");
        }

        CartItem item = itemOpt.get();

        Product product = item.getProduct();
        updateProductInventory(product, product.getInventoryQty() + item.getQuantity());

        cart.getCartItems().remove(item);
        cartRepository.save(cart);
    }

    private void showError(String msg) {
        FacesContext.getCurrentInstance()
                .addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", msg));
    }

    private void showInfo(String msg) {
        FacesContext.getCurrentInstance()
                .addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", msg));
    }
}
