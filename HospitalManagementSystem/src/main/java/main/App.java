package main;

import java.util.Scanner;

import enums.Role;
import model.User;
import services.AuthenticationService;
import utils.Validator;

/**
 * The App class serves as the entry point for the Hospital Management System.
 * It provides a menu-driven interface for user login and system management.
 * Based on the user's role, it redirects to the respective application module.
 * 
 * @author Lee Jia Qian Valerie
 * @version 1.0
 * @since 2024-11-15
 */

public class App {

    /** Scanner for reading user input. */
    static Scanner scanner = new Scanner(System.in);

    /** Service for handling user authentication. */
    static AuthenticationService authenticator = new AuthenticationService();

    /**
     * Main method that serves as the entry point for the application.
     *
     * @param args the command-line arguments.
     * @throws Exception if an error occurs during execution.
     */
    public static void main(String[] args) throws Exception {
        boolean systemOn = true;

        while (systemOn) {
            System.out.println("\nWelcome to Hospital Management System!");
            System.out.println("[1] Login");
            System.out.println("[2] System Shut Down");
            int choice = Validator.validateIntegerInput(scanner);

            switch (choice) {
                case 1:
                    enterLoginDetails();
                    break;
                case 2:
                    System.out.println("System Shutting Down....");
                    systemOn = false;
                    break;
                default:
                    System.out.println("Invalid choice");
                    break;
            }
        }
    }

    /**
     * Prompts the user to enter login details and authenticates the user.
     * Depending on the user's role, the appropriate application module is launched.
     *
     * @throws Exception if an error occurs during the login process or module initialization.
     */
    private static void enterLoginDetails() throws Exception {
        scanner = new Scanner(System.in);
        System.out.print("Hospital ID: ");
        String hospitalId = scanner.nextLine();  // Wait for user input
        System.out.print("Password: ");
        String password = scanner.nextLine();  // Wait for user input

        User userLoggedIn = authenticator.login(hospitalId, password);
        if (userLoggedIn == null) {
            System.out.println("Invalid ID or Password \n");
        }

        if (userLoggedIn != null) {
            if (userLoggedIn.getRole().equals(Role.ADMINISTRATOR.getDisplayValue())) {
                // Start Admin
                new AdminApp(scanner);
            }

            if (userLoggedIn.getRole().equals(Role.PATIENT.getDisplayValue())) {
                // Start Patient
                new PatientApp(userLoggedIn.getHospitalId(), scanner);
            }

            if (userLoggedIn.getRole().equals(Role.DOCTOR.getDisplayValue())) {
                // Start Doctor
                new DoctorApp(userLoggedIn.getHospitalId(), scanner);
            }

            if (userLoggedIn.getRole().equals(Role.PHARMACIST.getDisplayValue())) {
                // Start Pharmacist
                new PharmacistApp(scanner);
            }
        }
    }
}
