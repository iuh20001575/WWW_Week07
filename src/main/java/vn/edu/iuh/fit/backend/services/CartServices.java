package vn.edu.iuh.fit.backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.edu.iuh.fit.backend.enums.ProductStatus;
import vn.edu.iuh.fit.backend.models.Cart;
import vn.edu.iuh.fit.backend.repositories.CartRepository;

import java.util.List;

@Service
public class CartServices {
    @Autowired
    private CartRepository cartRepository;

    public List<Cart> findByEmployee(long employeeId) {
        return cartRepository.findByEmployee(employeeId, ProductStatus.ACTIVE);
    }
}
