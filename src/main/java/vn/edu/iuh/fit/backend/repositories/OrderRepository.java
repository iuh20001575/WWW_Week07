package vn.edu.iuh.fit.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import vn.edu.iuh.fit.backend.models.Order;

import java.util.List;
import java.util.Objects;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("SELECT avg(s.total) FROM (SELECT sum(od.quantity * od.price) as total FROM Order o JOIN o.orderDetails od group by Month(o.orderDate), year(o.orderDate)) s")
    Double calcAvgRevenueByMonth();

    @Query("SELECT avg(s.total) FROM (SELECT sum(od.quantity * od.price) as total FROM Order o JOIN o.orderDetails od group by year(o.orderDate)) s")
    Double calcAvgRevenueByYear();

    @Query("SELECT Month(o.orderDate), year(o.orderDate), sum(od.quantity * od.price) FROM Order o join o.orderDetails od group by Month(o.orderDate), year(o.orderDate) order by year(o.orderDate) desc, month(o.orderDate) desc limit 12")
    List<Object[]> calcRevenueByMonth();
}