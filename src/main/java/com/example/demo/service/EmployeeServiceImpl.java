
package com.example.demo.service;
import com.example.demo.exception.EmployeeNotFoundException;
import com.example.demo.entity.Employee;
import com.example.demo.repository.EmployeeRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.example.demo.dto.EmployeeRequest;
import com.example.demo.dto.EmployeeResponse;
import com.example.demo.exception.DuplicateEmailException;
@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }
    private Employee mapToEntity(EmployeeRequest request) {
        Employee employee = new Employee();

        employee.setFirstName(request.getFirstName());
        employee.setLastName(request.getLastName());
        employee.setEmail(request.getEmail());
        employee.setDepartment(request.getDepartment());
        employee.setSalary(request.getSalary());

        return employee;
    }
    // Convert Employee entity into EmployeeResponse
    private EmployeeResponse mapToResponse(Employee employee) {
        return new EmployeeResponse(
                employee.getId(),
                employee.getFirstName(),
                employee.getLastName(),
                employee.getEmail(),
                employee.getDepartment(),
                employee.getSalary()
        );
    }
    @Override
    public EmployeeResponse saveEmployee(EmployeeRequest request) {
        if (employeeRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateEmailException(
                    "Email already exists: " + request.getEmail()
            );
        }
        Employee employee = mapToEntity(request);
        return mapToResponse(employeeRepository.save(employee));
    }

    @Override
    public List<EmployeeResponse> getAllEmployees() {
        return employeeRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public EmployeeResponse  getEmployeeById(Long id) {

        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() ->
                        new EmployeeNotFoundException(
                                "Employee not found with ID: " + id
                        ));
        return mapToResponse(employee);
    }

    @Override
    public EmployeeResponse updateEmployee(Long id, EmployeeRequest request) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() ->
                        new EmployeeNotFoundException(
                                "Employee not found with ID: " + id
                        ));
        if (employeeRepository.existsByEmailAndIdNot(request.getEmail(), id)) {
            throw new DuplicateEmailException(
                    "Email already exists: " + request.getEmail()
            );
        }
        employee.setFirstName(request.getFirstName());
        employee.setLastName(request.getLastName());
        employee.setEmail(request.getEmail());
        employee.setDepartment(request.getDepartment());
        employee.setSalary(request.getSalary());

        return mapToResponse(employeeRepository.save(employee));
    }

    @Override
    public void deleteEmployee(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() ->
                        new EmployeeNotFoundException(
                                "Employee not found with ID: " + id
                        ));

        employeeRepository.delete(employee);
    }
    @Override
    public List<EmployeeResponse> searchEmployeesByName(String name) {
        return employeeRepository
                .findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
                        name, name)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<EmployeeResponse> searchEmployeesByDepartment(String department) {
        return employeeRepository
                .findByDepartmentContainingIgnoreCase(department)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }
    @Override
    public Page<EmployeeResponse> getAllEmployees(Pageable pageable) {
        return employeeRepository.findAll(pageable)
                .map(this::mapToResponse);
    }
}
