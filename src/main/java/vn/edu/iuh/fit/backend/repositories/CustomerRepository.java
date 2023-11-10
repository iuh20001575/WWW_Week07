package vn.edu.iuh.fit.backend.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.iuh.fit.backend.models.Customer;

import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
