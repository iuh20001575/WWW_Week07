package vn.edu.iuh.fit.backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.edu.iuh.fit.backend.enums.ProductStatus;
import vn.edu.iuh.fit.backend.models.Cart;
import vn.edu.iuh.fit.backend.models.Employee;
import vn.edu.iuh.fit.backend.models.Product;
import vn.edu.iuh.fit.backend.repositories.CartRepository;

import java.util.List;
import java.util.Optional;

@Service
public class CartServices {
    @Autowired
    private CartRepository cartRepository;

    public List<Cart> findByEmployee(long employeeId) {
        return cartRepository.findByEmployee(employeeId, ProductStatus.ACTIVE);
    }

    public Optional<Cart> findByEmployeeAndProduct(long empId, long proId) {
        return cartRepository.findByEmployeeAndProduct(new Employee(empId), new Product(proId));
    }
}
