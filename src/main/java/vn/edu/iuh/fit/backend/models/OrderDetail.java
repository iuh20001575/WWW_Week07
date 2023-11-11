package vn.edu.iuh.fit.backend.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import vn.edu.iuh.fit.backend.pks.OrderDetailPK;

@Getter
@Setter
@Entity
@Table(name = "order_detail")
@IdClass(OrderDetailPK.class)
public class OrderDetail {
    @Column(name = "quantity", nullable = false)
    private double quantity;
    @Column(name = "price", nullable = false)
    private double price;
    @Column(name = "note", length = 255, nullable = true)
    private String note;

    @Id
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;
    @Id
    @JoinColumn(name = "product_id")
    @ManyToOne
    private Product product;

    public OrderDetail() {
    }

    public OrderDetail(double quantity, double price, String note, Order order, Product product) {
        this.quantity = quantity;
        this.price = price;
        this.note = note;
        this.order = order;
        this.product = product;
    }

    @Override
    public String toString() {
        return "OrderDetail{" +
                ", quantity=" + quantity +
                ", price=" + price +
                ", note='" + note + '\'' +
                ", order=" + order +
                ", product=" + product +
                '}';
    }
}
