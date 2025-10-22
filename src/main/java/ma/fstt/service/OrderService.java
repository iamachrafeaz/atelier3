package ma.fstt.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import ma.fstt.enums.OrderStatusEnum;
import ma.fstt.model.Cart;
import ma.fstt.model.CartItem;
import ma.fstt.model.Order;
import ma.fstt.model.OrderItem;
import ma.fstt.repository.CartRepository;
import ma.fstt.repository.OrderRepository;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Named
@RequestScoped
public class OrderService {
    @Inject
    OrderRepository orderRepository;

    @Inject
    CartRepository cartRepository;

    public void saveOrder(Long userId)
    {
        Order order = new Order();
        Set<OrderItem> orderItems = new HashSet<>();

        Cart cart = cartRepository.findByUserId(userId);

        for (CartItem item : cart.getCartItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(item.getProduct());
            orderItem.setQuantity(item.getQuantity());

            orderItems.add(orderItem);
            orderItem.setOrder(order);
        }

        order.setStatus(OrderStatusEnum.PENDING);
        order.setOrderItems(orderItems);
        order.setCreatedAt(new Date(System.currentTimeMillis()));
        order.setUser(cart.getUser());
        double total = orderItems.stream()
                .mapToDouble(oi -> oi.getQuantity() * oi.getProduct().getPrice())
                .sum();
        order.setTotal(total);

        orderRepository.save(order);

        cartRepository.delete(cart);
    }

    public List<Order> listOrdersByUserId(Long userId)
    {
        return orderRepository.findByUserId(userId);
    }

    public Order showOrderById(Long orderId)
    {
        return orderRepository.findById(orderId);
    }
}

