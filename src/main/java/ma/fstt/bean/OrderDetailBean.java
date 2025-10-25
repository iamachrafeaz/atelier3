package ma.fstt.bean;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import ma.fstt.model.Order;
import ma.fstt.service.OrderService;

import java.io.Serializable;

@Named
@ViewScoped
public class OrderDetailBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private OrderService orderService;

    @Inject
    private UserSessionBean userSession;

    private Long orderId;
    private Order order;

    /**
     * Called when the page is loaded via f:event preRenderView
     */
    public void loadOrder() {
        if (orderId != null && userSession.getCurrentUser() != null) {
            try {
                order = orderService.showOrderById(orderId);

                // Security check: make sure the order belongs to the current user
                if (order != null && !order.getUser().getId().equals(userSession.getCurrentUser().getId())) {
                    addMessage(FacesMessage.SEVERITY_ERROR, "Access Denied",
                            "You don't have permission to view this order");
                    order = null;
                }
            } catch (Exception e) {
                addMessage(FacesMessage.SEVERITY_ERROR, "Error", "Failed to load order details");
                order = null;
            }
        }
    }

    public String getImagePath(String imageName) {
        if (imageName == null || imageName.isEmpty()) {
            return "";
        }
        return "/uploads/" + imageName;
    }

    public String getStatusLabel() {
        if (order == null || order.getStatus() == null) {
            return "Unknown";
        }

        String status = order.getStatus().name();
        switch (status) {
            case "PENDING":
                return "Pending";
            case "PROCESSING":
                return "Processing";
            case "COMPLETED":
                return "Completed";
            case "CANCELED":
                return "Canceled";
            default:
                return "Unknown";
        }
    }

    public String getStatusStyleClass() {
        if (order == null || order.getStatus() == null) {
            return "status-unknown";
        }

        String status = order.getStatus().name();
        switch (status) {
            case "PENDING":
                return "status-pending";
            case "PROCESSING":
                return "status-processing";
            case "COMPLETED":
                return "status-completed";
            case "CANCELED":
                return "status-canceled";
            default:
                return "status-unknown";
        }
    }

    public double calculateItemSubtotal(int quantity, double price) {
        return quantity * price;
    }

    private void addMessage(FacesMessage.Severity severity, String summary, String detail) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(severity, summary, detail));
    }

    // Getters and Setters

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}