
package com.example.demo.controller;

import com.example.demo.repository.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.UUID;
@SpringBootTest
@AutoConfigureMockMvc
class EmployeeControllerIntegrationTest {
    private Long extractEmployeeId(String response) {

        Matcher matcher = Pattern.compile("\"id\"\\s*:\\s*(\\d+)")
                .matcher(response);

        if (!matcher.find()) {
            throw new AssertionError("Employee ID not found in response");
        }

        return Long.parseLong(matcher.group(1));
    }
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Test
    void getAllEmployeesShouldReturn200() throws Exception {

        mockMvc.perform(get("/api/employees"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(
                        MediaType.APPLICATION_JSON
                ));
    }
    @Test
    void createEmployeeShouldReturn200() throws Exception {
        String email = "david-" + UUID.randomUUID() + "@gmail.com";
        String employeeJson = """
            {
                "firstName": "David",
                "lastName": "Smith",
                "email": "%s",
                "department": "IT",
                "salary": 50000
            }
            """.formatted(email);

        mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(employeeJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("David"))
                .andExpect(jsonPath("$.email")
                        .value(email));
    }
    @Test
    void getEmployeeByIdShouldReturn200() throws Exception {
        String email = "alex-" + UUID.randomUUID() + "@gmail.com";
        // First create an employee
        String employeeJson = """
            {
                "firstName": "Alex",
                "lastName": "Brown",
                "email": "%s",
                "department": "HR",
                "salary": 45000
            }
            """.formatted(email);

        String response = mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(employeeJson))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long employeeId = extractEmployeeId(response);

        // Retrieve the employee
        mockMvc.perform(get("/api/employees/" + employeeId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Alex"))
                .andExpect(jsonPath("$.email")
                        .value(email));
    }
    @Test
    void deleteEmployeeShouldReturn204() throws Exception {
        String email = "david-" + UUID.randomUUID() + "@gmail.com";
        String employeeJson = """
            {
                "firstName": "Mark",
                "lastName": "Taylor",
                "email": "%s",
                "department": "Finance",
                "salary": 55000
            }
            """.formatted(email);

        String response = mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(employeeJson))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long employeeId = extractEmployeeId(response);

        mockMvc.perform(delete("/api/employees/" + employeeId))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/employees/" + employeeId))
                .andExpect(status().isNotFound());
    }
    @Test
    void updateEmployeeShouldReturn200() throws Exception {

        String email = "update-" + UUID.randomUUID() + "@gmail.com";

        String createJson = """
            {
                "firstName": "John",
                "lastName": "Smith",
                "email": "%s",
                "department": "IT",
                "salary": 50000
            }
            """.formatted(email);

        String response = mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createJson))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long employeeId = extractEmployeeId(response);

        String updateJson = """
            {
                "firstName": "David",
                "lastName": "Smith",
                "email": "%s",
                "department": "HR",
                "salary": 60000
            }
            """.formatted(email);

        mockMvc.perform(put("/api/employees/" + employeeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("David"))
                .andExpect(jsonPath("$.department").value("HR"));
    }
    @Test
    void createEmployeeWithInvalidEmailShouldReturn400() throws Exception {

        String employeeJson = """
            {
                "firstName": "John",
                "lastName": "Smith",
                "email": "invalid-email",
                "department": "IT",
                "salary": 50000
            }
            """;

        mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(employeeJson))
                .andExpect(status().isBadRequest());
    }
}
