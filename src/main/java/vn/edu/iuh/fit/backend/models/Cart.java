package vn.edu.iuh.fit.backend.models;

import jakarta.persistence.*;
import lombok.*;
import vn.edu.iuh.fit.backend.pks.CartPK;

@Entity
@Table(name = "cart")
@IdClass(CartPK.class)
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Cart {
    @Id
    @ManyToOne
    @JoinColumn(name = "cust_id")
    private Customer customer;
    @Id
    @ManyToOne
    @JoinColumn(name = "prod_id")
    private Product product;
    @Column(name = "qty", nullable = false)
    private int quantity;
}
