Project Overview
The Employee Payroll and Information System is a Java-based application designed to simulate a basic employee management and payroll processing system. The system demonstrates how employee information can be stored, accessed, and processed to calculate working hours and salary details.
The system begins with a login page prototype that distinguishes between administrative users and employees. Depending on the user role, the system provides access to different types of information and system functions.
Purpose of the System
The purpose of this project is to demonstrate how programming concepts in Java can be used to manage employee data and perform payroll-related calculations. The system allows users to view employee information and calculate weekly salary details based on recorded working hours and deductions.
System Features the Login Page Prototype
The system includes a login interface that allows users to log into the system. Users are categorized into two roles:
Admin – can access and view employee records in bulk.


Employee – can view only their own employee information.


This component acts as the entry point of the system.
2. Employee Information Display
The system retrieves and displays employee information in the following format:
Name of Employee


Number of Employee


Birthday


Employee information is stored in a CSV file and read by the Java program when requested.
The Weekly Hours Calculation
The system calculates the total number of hours an employee has worked during a week. This is done by summing the hours worked each day or from provided weekly input values.
The Gross Weekly Salary Calculation
The program calculates the gross weekly salary by multiplying the total weekly hours worked by the employee's hourly wage.
Formula used:
Gross Salary = Hours Worked × Hourly Rate
5. Net Weekly Salary Calculation
The system calculates the net week salary by subtracting generic deductions from the gross salary. These deductions may include taxes or other standard deductions.
Formula used:
Net Salary = Gross Salary − Total Deductions
This provides a simplified simulation of a payroll calculation system.
Technologies Used
Java Programming Language


Apache NetBeans IDE


CSV (Comma-Separated Values) File for employee data storage


Console-based interface for system interaction


File Structure
EmployeePayrollSystem
│
├── src
│ └── com.mycompany.employeeinfo
│ └── EmployeeInfo.java
│
├── employees.csv
└── README.md
CSV File Structure
The employee data file contains the following fields:
employeeNumber,employeeName,birthday
Example:
1001,Maria Santos,1995-03-12
1002,Juan Cruz,1993-07-08
1003,Ana Reyes,1998-11-20
The system reads this file to retrieve employee information.
How the System Works
The user accesses the system through the login page.


The system determines whether the user is an Admin or an Employee.


Employee information is retrieved from the CSV file.


The system calculates the weekly hours worked.


The program calculates the gross salary based on hours worked.


The program subtracts deductions to determine the net salary.


Learning Objectives
This project demonstrates the following programming concepts:
File reading using CSV files


Java methods and modular programming


Basic payroll calculations


Role-based access simulation


Console-based system interaction
