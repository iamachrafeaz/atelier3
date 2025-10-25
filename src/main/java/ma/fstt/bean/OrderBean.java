package ma.fstt.bean;

import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;
import ma.fstt.model.Order;
import ma.fstt.repository.OrderRepository;
import ma.fstt.service.OrderService;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Named
@ViewScoped
public class OrderBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private OrderService orderService;

    @Inject
    private UserSessionBean userSession;

    private List<Order> orders;
    private Order selectedOrder;

    @PostConstruct
    public void init() {
        loadOrders();
    }

    private void loadOrders() {
        if (userSession.getCurrentUser() != null) {
            try {
                orders = orderService.listOrdersByUserId(userSession.getCurrentUser().getId());
            } catch (Exception e) {
                addMessage(FacesMessage.SEVERITY_ERROR, "Error", "Failed to load orders");
                orders = List.of();
            }
        }
    }

    private void addMessage(FacesMessage.Severity severity, String summary, String detail) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(severity, summary, detail));
    }

    public String saveOrder() {
        orderService.saveOrder();
        return "/order/orderList.xhtml?faces-redirect=true";

    }
}
