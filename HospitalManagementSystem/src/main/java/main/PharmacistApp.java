/**
 * The PharmacistApp class provides a console-based application interface for pharmacists.
 * It allows pharmacists to view completed appointments, manage the medical inventory,
 * and submit medication replenishment requests.
 * 
 * @author Lee Jia Qian Valerie
 * @version 1.0
 * @since 2024-11-15
 */
package main;

import services.PharmacistService;
import utils.Validator;

import java.io.IOException;
import java.util.Scanner;

public class PharmacistApp {

    /** Scanner for reading user input. */
    private final Scanner scanner;

    /** Service layer handling pharmacist-related operations. */
    private final PharmacistService pharmacistService = new PharmacistService();

    /**
     * Constructor for PharmacistApp. Initializes the application and displays the pharmacist menu.
     *
     * @param scanner the Scanner object for user input.
     * @throws Exception if an error occurs during initialization.
     */
    public PharmacistApp(Scanner scanner) throws Exception {
        this.scanner = scanner;
        displayPharmacistMenu();
    }

    /**
     * Displays the pharmacist menu and handles menu navigation for various pharmacist-related operations.
     *
     * @throws IOException if an error occurs while processing menu options.
     */
    public void displayPharmacistMenu() throws IOException {
        boolean loggedOut = false;

        do {
            System.out.println("\nWelcome to Pharmacist Menu!");
            System.out.println("1 View Completed Appointments");
            System.out.println("2 View Medicine Inventory");
            System.out.println("3 Submit Medicine Replenish Request"); // display all medication that are low on stock and key the amount of medication to replenish
            System.out.println("4 Logout");

            int choice = Validator.validateIntegerInput(scanner);

            switch (choice) {
                case 1:
                    pharmacistService.displayAllAppointmentOutcomes(scanner);
                    break;
                case 2:
                    pharmacistService.displayMedicalInventory(scanner);
                    break;
                case 3:
                    pharmacistService.submitReplenishRequest(scanner);
                    break;
                case 4:
                    loggedOut = true;
                    System.out.println("Logging out...");
                    break;
                default:
                    System.out.println("Invalid choice! Please try again.");
            }
        } while (!loggedOut);
    }
}
