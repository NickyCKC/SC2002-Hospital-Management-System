package main;

import services.AdminService;
import utils.Validator;

import java.util.Scanner;

/**
 * AdminApp is the main class for the administrative interface of the hospital management system.
 * It provides functionalities for managing hospital staff, scheduled appointments, and medication inventory.
 * This class includes a menu system for the administrator to interact with various services.
 *
 * @author LEE JIA QIAN VALERIE
 * @version 1.0
 * @since 2024-11-15
 */
public class AdminApp {
    private final Scanner scanner;
    private final AdminService adminService = new AdminService();

    /**
     * Constructor for AdminApp, which initializes the scanner and displays the admin menu.
     *
     * @param scanner A Scanner object for user input.
     * @throws Exception If an error occurs during the initialization or displaying the menu.
     */
    public AdminApp(Scanner scanner) throws Exception {
        this.scanner = scanner;
        displayAdminMenu();
    }

    /**
     * Displays the main administrator menu and handles user input to navigate through options.
     * This method allows the admin to view and manage staff, appointments, inventory, and more.
     *
     * @throws Exception If an error occurs while executing any admin-related tasks.
     */
    public void displayAdminMenu() throws Exception {
        boolean loggedOut = false;

        do {
            System.out.println("\n--- Administrator Menu ---");
            System.out.println("1. View and Manage Hospital Staff");
            System.out.println("2. View All Scheduled Appointments");
            System.out.println("3. View and Manage Medication Inventory");
            System.out.println("4. Approve Replenishment Requests");
            System.out.println("5. Logout");
            System.out.print("Enter your choice: ");

            int choice = Validator.validateIntegerInput(scanner);

            switch (choice) {
                case 1:
                    displayStaffManagement(scanner); // Now references the correct methods
                    break;
                case 2:
                    adminService.displayAppointmentDetails();
                    break;
                case 3:
                    displayMedicalInventoryManagement(scanner); // Now references the correct methods
                    break;
                case 4:
                    adminService.approveReplenishRequest(scanner);
                    break;
                case 5:
                    loggedOut = true;
                    System.out.println("Logging out...");
                    break;
                default:
                    System.out.println("Invalid choice! Please try again.");
                }
        } while (!loggedOut);
    }

    /**
     * Displays the staff management menu and handles the selection of various staff management tasks.
     * This includes viewing, adding, updating, and removing staff members.
     *
     * @param scanner A Scanner object for user input.
     * @throws Exception If an error occurs while managing staff members.
     */
    private void displayStaffManagement(Scanner scanner) throws Exception {
        int staffChoice;

        do {
            System.out.println("\n--- Manage Staff ---");
            System.out.println("1. Display Staff");
            System.out.println("2. Add Staff");
            System.out.println("3. Update Staff");
            System.out.println("4. Remove Staff");
            System.out.println("5. Back to Admin Menu");
            System.out.print("Enter your choice: ");
            staffChoice = Validator.validateIntegerInput(scanner);

            switch (staffChoice) {
                case 1:
                    adminService.displayStaffList(scanner);
                    break;
                case 2:
                    adminService.displayCreateNewStaffInput(scanner);
                    break;
                case 3:
                    adminService.displayUpdateStaffInput(scanner);
                    break;
                case 4:
                    adminService.displayRemoveStaffInput(scanner);
                    break;
                case 5:
                    System.out.println("Returning to admin menu...");
                    break;
                default:
                    System.out.println("Invalid choice! Please try again.");
            }
        } while (staffChoice != 5);
    }

    /**
     * Displays the medical inventory management menu and handles the selection of various inventory tasks.
     * This includes viewing, adding, updating, and removing medicines in the inventory.
     *
     * @param scanner A Scanner object for user input.
     * @throws Exception If an error occurs while managing medical inventory.
     */
    private void displayMedicalInventoryManagement(Scanner scanner) throws Exception {
        int adminChoice;

        do {
            System.out.println("\n--- Manage Medical Inventory ---");
            System.out.println("1. View Medicine List");
            System.out.println("2. Add Medicine");
            System.out.println("3. Update Stock Levels");
            System.out.println("4. Remove Medicine");
            System.out.println("5. Back to Admin Menu");

            adminChoice = Validator.validateIntegerInput(scanner);

            switch (adminChoice) {
                case 1:
                    adminService.displayMedicalInventory(scanner);
                    break;
                case 2:
                    adminService.displayCreateNewMedicineInput(scanner);
                    break;
                case 3:
                    adminService.displayUpdateMedicineInput(scanner);
                    break;
                case 4:
                    adminService.displayRemoveMedicineInput(scanner);
                    break;
                case 5:
                    System.out.println("Returning to main menu...");
                    break;
                default:
                    System.out.println("Invalid input! Please try again.");
            }
        } while (adminChoice != 5);
    }
}
