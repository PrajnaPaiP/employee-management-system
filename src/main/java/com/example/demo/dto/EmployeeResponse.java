
package com.example.demo.dto;

import java.math.BigDecimal;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Response object containing employee details")
public class EmployeeResponse {

    @Schema(description = "Unique employee ID", example = "1")
    private Long id;

    @Schema(description = "Employee first name", example = "John")
    private String firstName;

    @Schema(description = "Employee last name", example = "Smith")
    private String lastName;

    @Schema(description = "Employee email address", example = "john.smith@gmail.com")
    private String email;

    @Schema(description = "Employee department", example = "IT")
    private String department;

    @Schema(description = "Employee salary", example = "50000.00")
    private BigDecimal salary;

    public EmployeeResponse(
            Long id,
            String firstName,
            String lastName,
            String email,
            String department,
            BigDecimal salary) {

        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.department = department;
        this.salary = salary;
    }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getDepartment() {
        return department;
    }

    public BigDecimal getSalary() {
        return salary;
    }
}
