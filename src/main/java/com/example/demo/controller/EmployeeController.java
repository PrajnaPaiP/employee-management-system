
package com.example.demo.controller;

import com.example.demo.dto.EmployeeRequest;
import com.example.demo.dto.EmployeeResponse;
import com.example.demo.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
@Tag(name = "Employee Management", description = "APIs for managing employee records")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    // Create employee
    @PostMapping
    @Operation(summary = "Create Employee",
            description = "Creates a new employee record.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Employee created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid employee details"),
            @ApiResponse(responseCode = "409", description = "Email already exists")
    })
    public ResponseEntity<EmployeeResponse> createEmployee(
            @Valid @RequestBody EmployeeRequest request) {

        return ResponseEntity.ok(
                employeeService.saveEmployee(request)
        );
    }

    // Get all employees
    @GetMapping
    @Operation(summary = "Get All Employees",
            description = "Retrieves all employee records.")
    @ApiResponse(responseCode = "200", description = "Employees retrieved successfully")
    public ResponseEntity<List<EmployeeResponse>> getAllEmployees() {

        return ResponseEntity.ok(
                employeeService.getAllEmployees());
    }

    // Get employee by ID
    @GetMapping("/{id}")
    @Operation(summary = "Get Employee By ID",
            description = "Retrieves an employee using their ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Employee found"),
            @ApiResponse(responseCode = "404", description = "Employee not found")
    })
    public ResponseEntity<EmployeeResponse> getEmployeeById(
            @Parameter(description = "Employee ID", example = "1")
            @PathVariable Long id) {

        return ResponseEntity.ok(
                employeeService.getEmployeeById(id));
    }

    // Update employee
    @PutMapping("/{id}")
    @Operation(summary = "Update Employee",
            description = "Updates an existing employee record.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Employee updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid employee details"),
            @ApiResponse(responseCode = "404", description = "Employee not found"),
            @ApiResponse(responseCode = "409", description = "Email already exists")
    })
    public ResponseEntity<EmployeeResponse> updateEmployee(
            @Parameter(description = "Employee ID", example = "1")
            @PathVariable Long id,
            @Valid @RequestBody EmployeeRequest request) {

        return ResponseEntity.ok(
                employeeService.updateEmployee(id, request));
    }

    // Delete employee
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete Employee",
            description = "Deletes an employee using their ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Employee deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Employee not found")
    })
    public ResponseEntity<String> deleteEmployee(
            @Parameter(description = "Employee ID", example = "1")
            @PathVariable Long id) {

        employeeService.deleteEmployee(id);

        return ResponseEntity.noContent().build();
    }

    // Search employees by name
    @GetMapping("/search/name")
    @Operation(summary = "Search Employees By Name",
            description = "Searches employees by first name or last name.")
    @ApiResponse(responseCode = "200", description = "Search completed successfully")
    public ResponseEntity<List<EmployeeResponse>> searchByName(
            @Parameter(description = "Employee name to search", example = "John")
            @RequestParam String name) {

        return ResponseEntity.ok(
                employeeService.searchEmployeesByName(name));
    }

    // Search employees by department
    @GetMapping("/search/department")
    @Operation(summary = "Search Employees By Department",
            description = "Retrieves employees belonging to a department.")
    @ApiResponse(responseCode = "200", description = "Search completed successfully")
    public ResponseEntity<List<EmployeeResponse>> searchByDepartment(
            @Parameter(description = "Department name", example = "IT")
            @RequestParam String department) {

        return ResponseEntity.ok(
                employeeService.searchEmployeesByDepartment(department));
    }

    // Pagination and sorting
    @GetMapping("/page")
    @Operation(summary = "Get Employees With Pagination",
            description = "Retrieves employees with pagination and sorting options.")
    @ApiResponse(responseCode = "200", description = "Employees retrieved successfully")
    public ResponseEntity<Page<EmployeeResponse>> getEmployeesWithPagination(
            @Parameter(description = "Page number (starts from 0)", example = "0")
            @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "Number of records per page", example = "5")
            @RequestParam(defaultValue = "5") int size,

            @Parameter(description = "Field used for sorting", example = "id")
            @RequestParam(defaultValue = "id") String sortBy,

            @Parameter(description = "Sorting direction: asc or desc", example = "asc")
            @RequestParam(defaultValue = "asc") String direction) {

        Sort.Direction sortDirection =
                Sort.Direction.fromString(direction);

        PageRequest pageable = PageRequest.of(
                page,
                size,
                Sort.by(sortDirection, sortBy)
        );

        return ResponseEntity.ok(
                employeeService.getAllEmployees(pageable)
        );
    }
}
