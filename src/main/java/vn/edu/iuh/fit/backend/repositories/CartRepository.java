package vn.edu.iuh.fit.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.edu.iuh.fit.backend.enums.ProductStatus;
import vn.edu.iuh.fit.backend.models.Cart;
import vn.edu.iuh.fit.backend.models.Employee;
import vn.edu.iuh.fit.backend.models.Product;
import vn.edu.iuh.fit.backend.pks.CartPK;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, CartPK> {
    @Query("FROM Cart c WHERE c.employee.id = :employeeId AND c.product.status = :status")
    List<Cart> findByEmployee(@Param("employeeId") long employeeId, @Param("status") ProductStatus status);

    void deleteByEmployee(Employee employee);

    Optional<Cart> findByEmployeeAndProduct(Employee employee, Product product);
}