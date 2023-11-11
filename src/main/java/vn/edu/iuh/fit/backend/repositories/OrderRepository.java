package vn.edu.iuh.fit.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.edu.iuh.fit.backend.models.Order;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("SELECT avg(s.total) FROM (SELECT sum(od.quantity * od.price) as total FROM Order o JOIN o.orderDetails od group by Month(o.orderDate), year(o.orderDate)) s")
    Double calcAvgRevenueByMonth();

    @Query("SELECT avg(s.total) FROM (SELECT sum(od.quantity * od.price) as total FROM Order o JOIN o.orderDetails od group by year(o.orderDate)) s")
    Double calcAvgRevenueByYear();

    @Query("SELECT Month(o.orderDate), year(o.orderDate), sum(od.quantity * od.price) FROM Order o join o.orderDetails od group by Month(o.orderDate), year(o.orderDate) order by year(o.orderDate) desc, month(o.orderDate) desc limit 12")
    List<Object[]> calcRevenueByMonth();

    @Query("SELECT day(o.orderDate), sum(od.quantity * od.price) FROM Order o join o.orderDetails od where year(o.orderDate) = :year and month(o.orderDate) = :month group by day(o.orderDate)")
    List<Object[]> calcRevenueByDay(@Param("year") int year, @Param("month")  int month);

    @Query("SELECT cast(o.orderDate as DATE), sum(od.quantity * od.price) FROM Order o join o.orderDetails od where o.orderDate >= :startDate and o.orderDate <= :endDate group by cast(o.orderDate as DATE)")
    List<Object[]> calcRevenueByTimePeriod(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("SELECT o.employee, sum(od.quantity * od.price) FROM Order o join o.orderDetails od where o.orderDate >= :startDate and o.orderDate <= :endDate group by o.employee")
    List<Object[]> calcRevenueByEmployeeInTimePeriod(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
}