
package com.example.demo.service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.example.demo.entity.Employee;
import java.util.List;
import com.example.demo.dto.EmployeeRequest;
import com.example.demo.dto.EmployeeResponse;

public interface EmployeeService {

    EmployeeResponse saveEmployee(EmployeeRequest  request);

    List<EmployeeResponse> getAllEmployees();

    EmployeeResponse getEmployeeById(Long id);

    EmployeeResponse updateEmployee(Long id, EmployeeRequest  request);

    void deleteEmployee(Long id);

    List<EmployeeResponse> searchEmployeesByName(String name);

    List<EmployeeResponse> searchEmployeesByDepartment(String department);
    Page<EmployeeResponse> getAllEmployees(Pageable pageable);
}
