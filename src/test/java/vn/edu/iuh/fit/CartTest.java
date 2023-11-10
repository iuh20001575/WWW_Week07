package vn.edu.iuh.fit;

import jakarta.annotation.PostConstruct;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import vn.edu.iuh.fit.backend.models.Cart;
import vn.edu.iuh.fit.backend.models.Employee;
import vn.edu.iuh.fit.backend.repositories.CartRepository;
import vn.edu.iuh.fit.backend.repositories.ProductRepository;

@SpringBootTest
class CartTest {
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private ProductRepository productRepository;

    @PostConstruct
    void insert() {

        for (int i = 1; i <= 10; i++) {
            Cart cart = new Cart(
                    new Employee(1L),
                    productRepository.findById(Long.parseLong(String.valueOf(i))).get(),
                    10
            );

            cartRepository.save(cart);
        }
    }

    @Test
    void test() {
        Assertions.assertFalse(cartRepository.findAll().isEmpty());
    }
}