
package com.example.demo.service;

import com.example.demo.dto.EmployeeRequest;
import com.example.demo.dto.EmployeeResponse;
import com.example.demo.entity.Employee;
import com.example.demo.exception.DuplicateEmailException;
import com.example.demo.exception.EmployeeNotFoundException;
import com.example.demo.repository.EmployeeRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
@ExtendWith(MockitoExtension.class)
class EmployeeServiceImplTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    @Test
    void getEmployeeByIdShouldThrowExceptionWhenNotFound() {

        when(employeeRepository.findById(100L))
                .thenReturn(Optional.empty());

        assertThrows(
                EmployeeNotFoundException.class,
                () -> employeeService.getEmployeeById(100L)
        );

        verify(employeeRepository).findById(100L);
    }

    @Test
    void saveEmployeeShouldThrowExceptionWhenEmailAlreadyExists() {

        EmployeeRequest request = new EmployeeRequest();
        request.setEmail("test@gmail.com");

        when(employeeRepository.existsByEmail("test@gmail.com"))
                .thenReturn(true);

        assertThrows(
                DuplicateEmailException.class,
                () -> employeeService.saveEmployee(request)
        );

        verify(employeeRepository).existsByEmail("test@gmail.com");
    }

    @Test
    void saveEmployeeShouldCreateEmployeeSuccessfully() {

        EmployeeRequest request = new EmployeeRequest();
        request.setFirstName("John");
        request.setLastName("Smith");
        request.setEmail("john@gmail.com");
        request.setDepartment("IT");
        request.setSalary(new BigDecimal("50000"));

        Employee savedEmployee = new Employee();
        savedEmployee.setId(1L);
        savedEmployee.setFirstName("John");
        savedEmployee.setLastName("Smith");
        savedEmployee.setEmail("john@gmail.com");
        savedEmployee.setDepartment("IT");
        savedEmployee.setSalary(new BigDecimal("50000"));

        when(employeeRepository.existsByEmail("john@gmail.com"))
                .thenReturn(false);

        when(employeeRepository.save(any(Employee.class)))
                .thenReturn(savedEmployee);

        EmployeeResponse response = employeeService.saveEmployee(request);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("john@gmail.com", response.getEmail());

        verify(employeeRepository).save(any(Employee.class));
    }
    @Test
    void getAllEmployeesShouldReturnEmployeeList() {

        Employee employee = new Employee();
        employee.setId(1L);
        employee.setFirstName("John");
        employee.setLastName("Smith");
        employee.setEmail("john@gmail.com");
        employee.setDepartment("IT");
        employee.setSalary(new BigDecimal("50000"));

        when(employeeRepository.findAll())
                .thenReturn(List.of(employee));

        List<EmployeeResponse> response = employeeService.getAllEmployees();

        assertEquals(1, response.size());
        assertEquals("John", response.get(0).getFirstName());

        verify(employeeRepository).findAll();
    }@Test
    void updateEmployeeShouldUpdateSuccessfully() {

        Employee employee = new Employee();
        employee.setId(1L);
        employee.setFirstName("John");
        employee.setLastName("Smith");
        employee.setEmail("john@gmail.com");
        employee.setDepartment("IT");
        employee.setSalary(new BigDecimal("50000"));

        EmployeeRequest request = new EmployeeRequest();
        request.setFirstName("David");
        request.setLastName("Smith");
        request.setEmail("david@gmail.com");
        request.setDepartment("HR");
        request.setSalary(new BigDecimal("60000"));

        when(employeeRepository.findById(1L))
                .thenReturn(Optional.of(employee));

        when(employeeRepository.existsByEmailAndIdNot("david@gmail.com", 1L))
                .thenReturn(false);

        when(employeeRepository.save(any(Employee.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        EmployeeResponse response = employeeService.updateEmployee(1L, request);

        assertEquals("David", response.getFirstName());
        assertEquals("david@gmail.com", response.getEmail());
        assertEquals("HR", response.getDepartment());
    }
    @Test
    void updateEmployeeShouldThrowExceptionWhenEmailAlreadyExists() {

        Employee employee = new Employee();
        employee.setId(1L);
        employee.setEmail("john@gmail.com");

        EmployeeRequest request = new EmployeeRequest();
        request.setEmail("other@gmail.com");

        when(employeeRepository.findById(1L))
                .thenReturn(Optional.of(employee));

        when(employeeRepository.existsByEmailAndIdNot("other@gmail.com", 1L))
                .thenReturn(true);

        assertThrows(
                DuplicateEmailException.class,
                () -> employeeService.updateEmployee(1L, request)
        );

        verify(employeeRepository, never()).save(any(Employee.class));
    }
    @Test
    void deleteEmployeeShouldDeleteSuccessfully() {

        Employee employee = new Employee();
        employee.setId(1L);

        when(employeeRepository.findById(1L))
                .thenReturn(Optional.of(employee));

        employeeService.deleteEmployee(1L);

        verify(employeeRepository).delete(employee);
    }
    @Test
    void searchEmployeesByNameShouldReturnMatchingEmployees() {

        Employee employee = new Employee();
        employee.setId(1L);
        employee.setFirstName("John");
        employee.setLastName("Smith");
        employee.setEmail("john@gmail.com");
        employee.setDepartment("IT");
        employee.setSalary(new BigDecimal("50000"));

        when(employeeRepository
                .findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
                        "John", "John"))
                .thenReturn(List.of(employee));

        List<EmployeeResponse> response =
                employeeService.searchEmployeesByName("John");

        assertEquals(1, response.size());
        assertEquals("John", response.get(0).getFirstName());
    }
    @Test
    void getAllEmployeesWithPaginationShouldReturnPage() {

        Employee employee = new Employee();
        employee.setId(1L);
        employee.setFirstName("John");
        employee.setLastName("Smith");
        employee.setEmail("john@gmail.com");
        employee.setDepartment("IT");
        employee.setSalary(new BigDecimal("50000"));

        Pageable pageable = PageRequest.of(0, 5);

        Page<Employee> employeePage =
                new PageImpl<>(List.of(employee));

        when(employeeRepository.findAll(pageable))
                .thenReturn(employeePage);

        Page<EmployeeResponse> response =
                employeeService.getAllEmployees(pageable);

        assertEquals(1, response.getTotalElements());
        assertEquals("John", response.getContent().get(0).getFirstName());
    }
}
