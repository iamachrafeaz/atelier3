package ma.fstt.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "label", nullable = false)
    private String label;

    @Column(name = "price", nullable = false)
    private Double price;

    @Column(name = "inventory_qty", nullable = false)
    private Integer inventoryQty;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "image")
    private String image;

    @OneToMany(mappedBy = "product", cascade = CascadeType.REMOVE)
    private Set<OrderItem> orderItems;

    @OneToMany(mappedBy = "product")
    private Set<CartItem> cartItems;
}
