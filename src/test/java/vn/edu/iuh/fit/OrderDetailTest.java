package vn.edu.iuh.fit;

import jakarta.annotation.PostConstruct;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import vn.edu.iuh.fit.backend.models.OrderDetail;
import vn.edu.iuh.fit.backend.models.Product;
import vn.edu.iuh.fit.backend.repositories.OrderDetailRepository;
import vn.edu.iuh.fit.backend.repositories.OrderRepository;
import vn.edu.iuh.fit.backend.repositories.ProductRepository;

import java.util.Random;

@SpringBootTest
class OrderDetailTest {
    @Autowired
    private OrderDetailRepository orderDetailRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private OrderRepository orderRepository;

    @PostConstruct
    void insert() {
        Random random = new Random();

        for (int i = 20; i <= 319; i++) {
            int orderDetailsSize = random.nextInt(10);
            for (int j = 0; j < orderDetailsSize; j++) {
                Product product = productRepository.findById((long) (random.nextInt(300) + 1)).get();
                System.out.println("=====================");
                System.out.println(product);

                OrderDetail orderDetail = new OrderDetail(
                        random.nextInt(100) + 1,
                        random.nextDouble(1000000),
                        null,
                        orderRepository.findById((long) i).get(),
//                        product
                        new Product(product.getProduct_id())
                );


                orderDetailRepository.save(orderDetail);
            }
        }
    }

    @Test
    void test() {
        Assertions.assertFalse(orderDetailRepository.findAll().isEmpty());
    }
}