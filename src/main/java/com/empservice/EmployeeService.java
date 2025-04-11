package com.empservice;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

/*
 * Service class for loading data from csv, analyzing salaries, etc 
 */
public class EmployeeService {
	private Map<Integer, Employee> employees = new HashMap<>();

	// read the data from the CSV file and load into the map
	public void loadEmployeesFromFile(String filePath) throws IOException {
		Files.lines(new File(filePath).toPath()).filter(a -> !a.startsWith("Id")).forEach(a -> {
			String[] params = a.split(",");
			int id = Integer.parseInt(params[0]);
			String firstName = params[1];
			String lastName = params[2];
			double salary = Double.parseDouble(params[3]);
			Integer managerId = (params.length > 4 && !params[4].trim().isBlank()) ? Integer.valueOf(params[4]) : null;

			Employee emp = new Employee(id, firstName, lastName, salary, managerId);
			employees.put(id, emp);
		});

		sortData();
	}

	// sort the data according to reporting hierarchy
	public void sortData() {
		for (Employee emp : employees.values()) {
			if (emp.getManagerId() != null) {
				Employee employee = employees.get(emp.getManagerId());
				employee.getSubordinates().add(emp);
			}
		}
	}

	// find employees who are underpaid/overpaid
	public void analyzeSalaries() {
		for (Employee manager : employees.values()) {
			if (!manager.getSubordinates().isEmpty()) {
				double avgSubSalary = manager.getSubordinates().stream().mapToDouble(e -> e.getSalary()).average()
						.orElse(0);
				double minAllowed = avgSubSalary * 1.2;
				double maxAllowed = avgSubSalary * 1.5;

				if (manager.getSalary() < minAllowed) {
					System.out.printf("UNDER PAID: %s earns %.2f, should earn at least %.2f%n", manager.getFullName(),
							manager.getSalary(), minAllowed);
				} else if (manager.getSalary() > maxAllowed) {
					System.out.printf("OVER PAID: %s earns %.2f, should earn at most %.2f%n", manager.getFullName(),
							manager.getSalary(), maxAllowed);
				}
			}
		}
	}

	// report the depth of the reporting hierarchy
	public void analyzeReportingLines() {
		for (Employee emp : employees.values()) {
			int depth = getDepthToCEO(emp);
			if (depth > 4) {
				System.out.printf("REPORTING: %s has %d managers to CEO (too long by %d)%n", emp.getFullName(), depth,
						depth - 4);
			}
		}
	}

	// find depth in the reporting hierarchy
	private int getDepthToCEO(Employee emp) {
		int depth = 0;
		while (emp.getManagerId() != null) {
			emp = employees.get(emp.getManagerId());
			depth++;
		}
		return depth;
	}

	public static void main(String[] args) {
		final String path = "target/classes/employees.csv";
		try {
			EmployeeService service = new EmployeeService();
			service.loadEmployeesFromFile(path);
			service.analyzeSalaries();
			service.analyzeReportingLines();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
