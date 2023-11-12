package vn.edu.iuh.fit.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import vn.edu.iuh.fit.backend.models.Product;
import vn.edu.iuh.fit.backend.models.ProductPrice;
import vn.edu.iuh.fit.backend.pks.ProductPricePK;

import java.util.List;
import java.util.Optional;

public interface ProductPriceRepository extends JpaRepository<ProductPrice, ProductPricePK> {
    @Query("FROM ProductPrice WHERE product = :product ORDER BY price_date_time desc limit 1")
    Optional<ProductPrice> findNewPrice(Product product);

    List<ProductPrice> findByProduct(Product product);
}