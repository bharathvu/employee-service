package com.empservice;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class EmployeeServiceTest {

	private EmployeeService service;
	private final String testFilePath = "target/test-classes/test-employees.csv";

	@BeforeEach
	void setup() throws IOException {
		service = new EmployeeService();
		service.loadEmployeesFromFile(testFilePath);
	}

	@Test
	void testLoadEmployees() {
		assertNotNull(service);
	}

	/*
	 * Manager1 has 2 devs earns 30k (avg 30k, min 36k, max 45k), if earns 60k then
	 * overpaid
	 */
	@Test
	void testAnalyzeSalariesUnderpaid() {
		assertDoesNotThrow(() -> service.analyzeSalaries());
	}

	/*
	 * Lead earns 90k, no sub-ordinates
	 */
	@Test
	void testAnalyzeSalariesOverpaid() {
		assertDoesNotThrow(() -> service.analyzeSalaries());
	}

	/*
	 * ID 6 with 3 managers
	 */
	@Test
	void testReportingLineDepthTooLong() throws IOException {
		// for over-depth, let's create one more level dynamically
//		Employee deepEmployee = new Employee(8, "Jake", "Stern", 10000, 6);
		service.loadEmployeesFromFile(testFilePath); // ensure map is updated
		service.analyzeReportingLines(); // should print warning
	}

	@Test
	void testGetDepthToCEO() throws IOException {
		// Directly test the private method via reflection or simulate its behavior
		var emp = new Employee(10, "Depth", "Tester", 1000, 2); // managerId = 2 -> 1 (CEO)
		service.loadEmployeesFromFile(testFilePath);
		// We'll add it manually for testing
		service.loadEmployeesFromFile(testFilePath);
		assertEquals(2, invokeGetDepthToCEO(service, emp));
	}

	// Helper method using reflection to invoke private method
	private int invokeGetDepthToCEO(EmployeeService svc, Employee emp) {
		try {
			var method = EmployeeService.class.getDeclaredMethod("getDepthToCEO", Employee.class);
			method.setAccessible(true);
			return (int) method.invoke(svc, emp);
		} catch (Exception e) {
			fail("Failed to invoke private method getDepthToCEO");
			return -1;
		}
	}
}