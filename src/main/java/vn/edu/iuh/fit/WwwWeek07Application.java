package vn.edu.iuh.fit;

import net.datafaker.Faker;
import net.datafaker.providers.base.Device;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import vn.edu.iuh.fit.backend.enums.ProductStatus;
import vn.edu.iuh.fit.backend.models.*;
import vn.edu.iuh.fit.backend.repositories.*;

import java.time.LocalDateTime;
import java.util.Optional;

@SpringBootApplication
public class WwwWeek07Application {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductPriceRepository productPriceRepository;
    @Autowired
    private ProductImageRepository productImageRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private CartRepository cartRepository;

    public static void main(String[] args) {
        SpringApplication.run(WwwWeek07Application.class, args);
    }

//    @Bean
    CommandLineRunner insertData() {
        return args -> {
            Faker faker = new Faker();
            Device device = faker.device();

            for (int i = 0; i < 300; i++) {
                Product product = new Product(
                        device.modelName(),
                        faker.lorem().paragraph(10),
                        "item",
                        device.manufacturer(),
                        ProductStatus.ACTIVE
                );

                Product product1 = productRepository.save(product);
                Optional<Product> productOptional = productRepository.findById(product1.getProduct_id());

                ProductPrice productPrice = new ProductPrice(
                        productOptional.get(),
                        LocalDateTime.now(),
                        faker.number().numberBetween(1000, 1000000),
                        faker.lorem().paragraph(2)
                );

                productPriceRepository.save(productPrice);

                ProductImage productImage = new ProductImage(
                        "https://picsum.photos/800",
                        null,
                        productOptional.get()
                );

                productImageRepository.save(productImage);
            }

            for (int i = 0; i < 10; i++) {
                Customer customer = new Customer(
                        faker.name().fullName(),
                        faker.internet().emailAddress(),
                        faker.phoneNumber().cellPhone(),
                        faker.address().fullAddress()
                );

                customerRepository.save(customer);
            }

            for (int i = 1; i <= 10; i++) {
                Cart cart = new Cart(
                        new Customer(1L),
                        productRepository.findById(Long.parseLong(String.valueOf(i))).get(),
                        10
                );

                cartRepository.save(cart);
            }
        };
    }
}
