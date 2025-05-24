package com.unittesting.service;

import com.unittesting.entity.Employee;
import com.unittesting.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeService employeeService;

    private Employee employee1;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        employee1 = new Employee(1L, "John Doe", "john@example.com", "Developer");
    }

    @Test
    void testCreateEmployee() {
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee1);

        Employee created = employeeService.createEmployee(employee1);

        assertNotNull(created);
        assertEquals("John Doe", created.getName());
        verify(employeeRepository, times(1)).save(employee1);
    }

    @Test
    void testGetAllEmployees() {
        List<Employee> employees = Arrays.asList(employee1);
        when(employeeRepository.findAll()).thenReturn(employees);

        List<Employee> result = employeeService.getAllEmployees();

        assertEquals(1, result.size());
        verify(employeeRepository, times(1)).findAll();
    }

    @Test
    void testGetEmployeeById_Found() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee1));

        Optional<Employee> result = Optional.ofNullable(employeeService.getEmployeeById(1L));

        assertTrue(result.isPresent());
        assertEquals("John Doe", result.get().getName());
        verify(employeeRepository, times(1)).findById(1L);
    }

//    @Test
//    void testGetEmployeeById_NotFound() {
//        when(employeeRepository.findById(2L)).thenReturn(Optional.empty());
//
//        Optional<Employee> result = Optional.ofNullable(employeeService.getEmployeeById(2L));
//
//        assertFalse(result.isPresent());
//        verify(employeeRepository, times(1)).findById(2L);
//    }

    @Test
    void testUpdateEmployee_Success() {
        Employee updatedInfo = new Employee(null, "Jane Doe", "jane@example.com", "Manager");

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee1));
        when(employeeRepository.save(any(Employee.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Employee updated = employeeService.updateEmployee(1L, updatedInfo);

        assertEquals("Jane Doe", updated.getName());
        assertEquals("jane@example.com", updated.getEmail());
        verify(employeeRepository, times(1)).findById(1L);
        verify(employeeRepository, times(1)).save(any(Employee.class));
    }

//    @Test
//    void testUpdateEmployee_NotFound() {
//        Employee updatedInfo = new Employee(null, "Jane Doe", "jane@example.com", "Manager");
//
//        when(employeeRepository.findById(1L)).thenReturn(Optional.empty());
//
//        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
//            employeeService.updateEmployee(1L, updatedInfo);
//        });
//
//        assertEquals("Employee not found with id 1", thrown.getMessage());
//        verify(employeeRepository, times(1)).findById(1L);
//        verify(employeeRepository, never()).save(any(Employee.class));
//    }

    @Test
    void testDeleteEmployee() {
        doNothing().when(employeeRepository).deleteById(1L);

        employeeService.deleteEmployee(1L);

        verify(employeeRepository, times(1)).deleteById(1L);
    }
}
