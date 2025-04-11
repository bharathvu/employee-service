# Employee Structure Analyzer

This Java 11 program analyzes employee data from a CSV file and reports:

1. **Salary rule violations** — Managers who are overpaid or underpaid compared to their direct subordinates.
2. **Reporting line violations** — Employees who are too far removed from the CEO (more than 4 managers above them).

---

## 📂 Problem Statement

> BIG COMPANY wants to improve its organizational structure. They want to ensure:
> 
> - Every **manager earns 20% to 50% more** than the average salary of their **direct subordinates**.
> - No **employee should have more than 4 levels** of management between themselves and the **CEO**.
>
> You are given a CSV file containing the company’s org chart.

---

## 📋 Sample CSV Format

```csv
Id,firstName,lastName,salary,managerId
123,Joe,Doe,60000,
124,Martin,Chekov,45000,123
125,Bob,Ronstad,47000,123
300,Alice,Hasacat,50000,124
305,Brett,Hardleaf,34000,300
