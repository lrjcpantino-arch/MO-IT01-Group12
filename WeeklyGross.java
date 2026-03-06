/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mavenproject2;

/**
 *}

 * @author Hazel Ann
 */
 import java.util.Scanner;
 
public class MotorPh1 {
 public static void main(String[] args) {
        
             Scanner input = new Scanner(System.in);
 
        System.out.println("=== Weekly Payroll Calculator ===");
 
        System.out.print("Enter Weekly Hours Worked: ");

        double weeklyHours = input.nextDouble();
 
        System.out.print("Enter Hourly Rate: ");

        double hourlyRate = input.nextDouble();
 
        double weeklyGross = weeklyHours * hourlyRate;
 
        System.out.println();

        System.out.println("----- Payroll Summary -----");

        System.out.println("Weekly Hours: " + weeklyHours);

        System.out.println("Hourly Rate: " + hourlyRate);

        System.out.println("Weekly Gross Pay: " + weeklyGross);
 
        input.close();
 
    }   
}
