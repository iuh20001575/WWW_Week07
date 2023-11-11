package vn.edu.iuh.fit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import vn.edu.iuh.fit.backend.models.Order;
import vn.edu.iuh.fit.backend.models.OrderDetail;
import vn.edu.iuh.fit.backend.repositories.EmployeeRepository;
import vn.edu.iuh.fit.backend.repositories.OrderDetailRepository;
import vn.edu.iuh.fit.backend.repositories.OrderRepository;
import vn.edu.iuh.fit.backend.repositories.ProductRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@SpringBootTest
class OrderTest {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private OrderDetailRepository orderDetailRepository;
    @Autowired
    private EmployeeRepository employeeRepository;

//    @PostConstruct
    void insert() {
        Random random = new Random();

        for (int i = 0; i < 300; i++) {
            Order order = new Order(
                    LocalDateTime.now(),
                    employeeRepository.findById((random.nextLong(100) + 1)).get(),
                    null
            );

            List<OrderDetail> orderDetails = new ArrayList<>();
            int size = random.nextInt(10) + 1;
            for (int j = 0; j < size; j++) {
                OrderDetail orderDetail = new OrderDetail(
                        random.nextInt(10) + 1,
                        random.nextDouble(1000000) + 1,
                        null,
                        order,
                        productRepository.findById(random.nextLong(300) + 1).get()
                );

                orderDetails.add(orderDetail);
            }

            order.setOrderDetails(orderDetails);

            System.out.println("========================");
            System.out.println(order);
            System.out.println("========================");

            orderRepository.save(order);
         }
    }

    @Test
    void test() {
        Assertions.assertFalse(orderRepository.findAll().isEmpty());
    }

    @Test
    void update() {
        List<Order> orders = orderRepository.findAll();
        Random random = new Random();

        orders.forEach(order -> {
            LocalDateTime localDateTime = LocalDateTime.of(LocalDate.of(
                    random.nextInt(2010, 2024),
                    random.nextInt(12) + 1,
                    random.nextInt(25) + 1
            ), LocalTime.MIDNIGHT);

            order.setOrderDate(localDateTime);
        });

        orderRepository.saveAll(orders);
        Assertions.assertTrue(true);
    }
}