package vn.edu.iuh.fit.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.iuh.fit.backend.models.Cart;
import vn.edu.iuh.fit.backend.pks.CartPK;

public interface CartRepository extends JpaRepository<Cart, CartPK> {
}