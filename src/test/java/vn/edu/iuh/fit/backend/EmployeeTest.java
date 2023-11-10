package vn.edu.iuh.fit.backend;

import jakarta.annotation.PostConstruct;
import net.datafaker.Faker;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import vn.edu.iuh.fit.backend.enums.EmployeeStatus;
import vn.edu.iuh.fit.backend.models.Employee;
import vn.edu.iuh.fit.backend.repositories.EmployeeRepository;

import java.time.LocalDate;

@SpringBootTest
class EmployeeTest {
    @Autowired
    private EmployeeRepository employeeRepository;

    @PostConstruct
    void insert() {
        Faker faker = new Faker();

        for (int i = 0; i < 100; i++) {
            Employee employee = new Employee(
                    faker.name().fullName(),
                    LocalDate.now(),
                    faker.internet().emailAddress(),
                    faker.phoneNumber().cellPhone(),
                    faker.address().fullAddress(),
                    EmployeeStatus.values()[i % EmployeeStatus.values().length]
            );

            employeeRepository.save(employee);
        }
    }

    @Test
    void test() {
        Assertions.assertFalse(employeeRepository.findAll().isEmpty());
    }
}