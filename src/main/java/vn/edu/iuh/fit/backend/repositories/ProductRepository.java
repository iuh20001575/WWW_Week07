package vn.edu.iuh.fit.backend.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.edu.iuh.fit.backend.enums.ProductStatus;
import vn.edu.iuh.fit.backend.models.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("FROM Product p WHERE p.status = :status and p.product_id in (SELECT pi.product.product_id FROM ProductImage pi WHERE pi.product = p) and p.product_id in (SELECT pp.product.product_id FROM ProductPrice pp WHERE pp.product = p)")
    Page<Product> findToRender(@Param("status") ProductStatus status, Pageable pageable);
}