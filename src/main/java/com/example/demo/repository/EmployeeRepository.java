
package com.example.demo.repository;
import java.util.List;
import com.example.demo.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    List<Employee> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
            String firstName, String lastName);

    List<Employee> findByDepartmentContainingIgnoreCase(String department);
    boolean existsByEmail(String email);
    boolean existsByEmailAndIdNot(String email, Long id);
}
