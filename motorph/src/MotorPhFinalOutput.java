package com.mycompany.motorph;

import java.io.BufferedReader;
import java.io.FileReader;
import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class MotorPhFinalOutput{
    public static void main(String[] args){
        Scanner input = new Scanner(System.in);

        String employeeFile = "resources/MPHD1 Employee Details.csv";
        String attendanceFile = "resources/employeesMPHD4.csv";

        System.out.println("========== MOTORPH PAYROLL SYSTEM ==========");

        System.out.print("Enter Employee ID: ");
        String inputId = input.nextLine();

        System.out.print("Enter Password: ");
        String inputPassword = input.nextLine();

        boolean loginSuccess = false;
        boolean isAdmin = false;
        String employeeName = "";
        double hourlyRate = 0;

        String adminId = "13579";
        String adminPassword = "12345";

        // ================= ADMIN LOGIN =================
        if (inputId.equals(adminId) && inputPassword.equals(adminPassword)) {
            loginSuccess = true;
            isAdmin = true;
        }

        // ================= EMPLOYEE LOGIN =================
        if (!loginSuccess) {
            try (BufferedReader br = new BufferedReader(new FileReader(employeeFile))) {
                br.readLine(); // skip header
                String line;

                while ((line = br.readLine()) != null) {
                    String[] data = line.split(",");
                    if (inputId.equals(data[0]) && inputPassword.equals(data[2])) {
                        loginSuccess = true;
                        employeeName = data[1];
                        hourlyRate = Double.parseDouble(data[19].replace("\"","").trim());
                        break;
                    }
                }

            } catch (Exception e) {
                System.out.println("Error reading employee file.");
                return;
            }
        }

        if (!loginSuccess) {
            System.out.println("Login Failed");
            return;
        }

        System.out.println("\nLogin Successful");
        if (isAdmin)
            System.out.println("Welcome Administrator");
        else
            System.out.println("Welcome " + employeeName);

        // ================= ADMIN SECTION =================
        if (isAdmin) {

            boolean running = true;

            while (running) {
                System.out.println("\n====== ADMIN MENU ======");
                System.out.println("1 Compute Employee Salary");
                System.out.println("2 View Employee Attendance");
                System.out.println("3 Logout");

                int choice = input.nextInt();

                switch (choice) {
                    case 1: // Compute Employee Salary
                        System.out.print("Enter Employee ID: ");
                        String empId = input.next();
                        System.out.print("Enter Month (1-12): ");
                        int month = input.nextInt();

                        double empRate = 0;
                        String empName = "";

                        try (BufferedReader br = new BufferedReader(new FileReader(employeeFile))) {
                            br.readLine(); // skip header
                            String line;
                            while ((line = br.readLine()) != null) {
                                String[] data = line.split(",");
                                if (data[0].equals(empId)) {
                                    empName = data[1];
                                    empRate = Double.parseDouble(data[19].replace("\"","").trim());
                                    break;
                                }
                            }
                        } catch (Exception e) {
                            System.out.println("Error reading employee file.");
                            break;
                        }

                        if (empRate == 0) {
                            System.out.println("Employee not found or invalid rate.");
                            break;
                        }

                        double[] hours = computeMonthlyHours(empId, month, attendanceFile);
                        double firstHalf = hours[0];
                        double secondHalf = hours[1];

                        double gross1 = firstHalf * empRate;
                        double gross2 = secondHalf * empRate;
                        double totalGross = gross1 + gross2;

                        System.out.println("\nEmployee: " + empName);
                        displayPayroll(month, firstHalf, secondHalf, gross1, gross2);
                        calculateAndPrintDeductions(totalGross);

                        break;

                    case 2: // View Employee Attendance
                        System.out.println("\nEMPLOYEE ATTENDANCE LIST");
                        System.out.println("---------------------------------------------------------------------------------------------------------------");
                        System.out.printf("%-12s %-18s %-18s %-12s %-12s %-10s %-10s\n",
                                "Emp No.", "Last Name", "First Name", "Birthday", "Date", "Clock In", "Clock Out");
                        System.out.println("---------------------------------------------------------------------------------------------------------------");

                        try (BufferedReader br = new BufferedReader(new FileReader(attendanceFile))) {
                            br.readLine();
                            String line;
                            while ((line = br.readLine()) != null) {
                                String[] data = line.split(",");
                                System.out.printf("%-12s %-18s %-18s %-12s %-12s %-10s %-10s\n",
                                        data[0], data[1].replace("\"",""), data[2].replace("\"",""), data[3], data[4], data[5], data[6]);
                            }
                        } catch (Exception e) {
                            System.out.println("Error reading attendance file.");
                        }

                        break;

                    case 3: // Logout
                        running = false;
                        break;

                    default:
                        System.out.println("Invalid choice. Try again.");
                }
            }
        }

        // ================= EMPLOYEE SECTION =================
        else {
            System.out.print("Enter Month to View Payslip (1-12): ");
            int month = input.nextInt();

            double[] hours = computeMonthlyHours(inputId, month, attendanceFile);
            double firstHalf = hours[0];
            double secondHalf = hours[1];

            double gross1 = firstHalf * hourlyRate;
            double gross2 = secondHalf * hourlyRate;

            double totalGross = gross1 + gross2;

            displayPayroll(month, firstHalf, secondHalf, gross1, gross2);
            calculateAndPrintDeductions(totalGross);
            System.out.println("DEBUG Hourly Rate: " + hourlyRate);
        }

        input.close();
    }

    // ================= READ ATTENDANCE =================
    static double[] computeMonthlyHours(String employeeId, int month, String filePath) {
        double firstHalf = 0;
        double secondHalf = 0;

        DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("H:mm");

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            br.readLine();
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (!data[0].equals(employeeId)) continue;

                String[] dateParts = data[4].split("/");
                int recordMonth = Integer.parseInt(dateParts[0]);
                int day = Integer.parseInt(dateParts[1]);
                int year = Integer.parseInt(dateParts[2]);

                if (year != 2024 || recordMonth != month) continue;

                LocalTime login = LocalTime.parse(data[5].trim(), timeFormat);
                LocalTime logout = LocalTime.parse(data[6].trim(), timeFormat);

                double hours = computeHours(login, logout);
                if (day <= 15) firstHalf += hours;
                else secondHalf += hours;
            }

        } catch (Exception e) {
            System.out.println("Error reading attendance file.");
        }

        return new double[]{firstHalf, secondHalf};
    }

    // ================= HOURS COMPUTATION =================
    static double computeHours(LocalTime login, LocalTime logout) {
        LocalTime graceTime = LocalTime.of(8, 10);
        LocalTime cutoffTime = LocalTime.of(17, 0);

        if (logout.isAfter(cutoffTime)) logout = cutoffTime;

        long minutesWorked = Duration.between(login, logout).toMinutes();
        if (minutesWorked > 60) minutesWorked -= 60;
        else minutesWorked = 0;

        double hours = minutesWorked / 60.0;
        if (!login.isAfter(graceTime)) return 8.0;

        return Math.min(hours, 8.0);
    }

    // ================= PAYROLL DISPLAY =================
    static void displayPayroll(int month, double firstHalf, double secondHalf,
                               double gross1, double gross2) {

        String monthName = switch (month) {
            case 1 -> "January";
            case 2 -> "February";
            case 3 -> "March";
            case 4 -> "April";
            case 5 -> "May";
            case 6 -> "June";
            case 7 -> "July";
            case 8 -> "August";
            case 9 -> "September";
            case 10 -> "October";
            case 11 -> "November";
            case 12 -> "December";
            default -> "Unknown";
        };

        System.out.println("\nCutoff Date: " + monthName + " 1 to 15");
        System.out.println("Total Hours Worked : " + firstHalf);
        System.out.println("Gross Salary: PHP " + gross1);

        System.out.println("\nCutoff Date: " + monthName + " 16 to 30");
        System.out.println("Total Hours Worked : " + secondHalf);
        System.out.println("Gross Salary: PHP " + gross2); 
    }

    // ================= DEDUCTIONS =================
    public static void calculateAndPrintDeductions(double grossSalary) {

        double[] grossSalaryLimits = {3249,3250,3750,4250,4750,5250,5750,6250,6750,7250,7750,8250,8750,9250,9750,10250,10750,11250,11750,12250,12750,13250,13750,14250,14750,15250,15750,16250,16750,17250,17750,18250,18750,19250,19750,20250,20750,21250,21750,22250,22750,23250,23750,24250,24750};
        double[] ratesSSS = {135.00,157.50,180.00,202.50,225.00,247.50,270.00,292.50,315.00,337.50,360.00,382.50,405.00,427.50,450.00,472.50,495.00,517.50,540.00,562.50,585.00,607.50,630.00,652.50,675.00,697.50,720.00,742.50,765.00,787.50,810.00,832.50,855.00,877.50,900.00,922.50,945.00,967.50,990.00,1012.50,1035.00,1057.50,1080.00,1102.50,1125.00};

        double sssDeduction = 0;
        for (int i = 0; i < grossSalaryLimits.length; i++) {
            if (grossSalary <= grossSalaryLimits[i]) {
                sssDeduction = ratesSSS[i];
                break;
            }
        }
        if (grossSalary > grossSalaryLimits[grossSalaryLimits.length - 1])
            sssDeduction = ratesSSS[ratesSSS.length - 1];

        double philHealthTaxDeduction = grossSalary * 0.03;

        double pagIbigRates = (grossSalary >= 1000 && grossSalary <= 1500) ? 0.03 : (grossSalary >= 1500 ? 0.04 : 0);
        double pagIbigTaxDeduction = grossSalary * pagIbigRates;

        double witholdingTaxRates;
        if (grossSalary <= 20832) witholdingTaxRates = 0;
        else if (grossSalary < 33333) witholdingTaxRates = (grossSalary - 20833) * 0.20;
        else if (grossSalary < 66667) witholdingTaxRates = 2500 + (grossSalary - 33333) * 0.25;
        else if (grossSalary < 166667) witholdingTaxRates = 10833 + (grossSalary - 66667) * 0.30;
        else if (grossSalary < 666667) witholdingTaxRates = 40833.33 + (grossSalary - 166667) * 0.32;
        else witholdingTaxRates = 200833.33 + (grossSalary - 666667) * 0.35;

        double overallTaxDeduction = sssDeduction + philHealthTaxDeduction + pagIbigTaxDeduction + witholdingTaxRates;
        double finalSalary = grossSalary - overallTaxDeduction;

        System.out.println("\nDeductions:");
        System.out.printf("SSS Deduction: %.2f\n", sssDeduction);
        System.out.printf("PhilHealth Deduction: %.2f\n", philHealthTaxDeduction);
        System.out.printf("Pag-Ibig Deduction: %.2f\n", pagIbigTaxDeduction);
        System.out.printf("Withholding Tax: %.2f\n", witholdingTaxRates);
        System.out.printf("\nNet Pay: %.2f\n", finalSalary);
    }
}