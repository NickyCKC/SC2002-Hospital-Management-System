/**
 * The DoctorApp class provides a console-based application interface for doctors.
 * It allows doctors to view their patient list, available schedules, and upcoming appointments.
 * 
 * @author Lee Jia Qian Valerie
 * @version 1.0
 * @since 2024-11-15
 */
package main;

import services.DoctorService;
import utils.Validator;

import java.io.IOException;
import java.util.Scanner;

public class DoctorApp {

    /** Scanner for reading user input. */
    private final Scanner scanner;

    /** Service layer handling doctor-related operations. */
    private final DoctorService doctorService = new DoctorService();

    /**
     * Constructor for DoctorApp. Initializes the application by loading the doctor's information
     * and displays the doctor menu.
     *
     * @param hospitalId the hospital ID of the logged-in doctor.
     * @param scanner    the Scanner object for user input.
     * @throws Exception if an error occurs during initialization.
     */
    public DoctorApp(String hospitalId, Scanner scanner) throws Exception {
        this.scanner = scanner;
        doctorService.loadDoctorInfo(hospitalId);
        displayDoctorMenu();
    }

    /**
     * Displays the doctor menu and handles menu navigation for various doctor-related operations.
     *
     * @throws IOException if an error occurs while processing menu options.
     */
    public void displayDoctorMenu() throws IOException {
        boolean loggedOut = false;

        do {
            System.out.println("\nWelcome to Doctor Menu!");
            System.out.println("1 View Patient List");
            System.out.println("2 View Available Schedule");
            System.out.println("3 View Upcoming Appointments");
            System.out.println("4 Logout");

            int choice = Validator.validateIntegerInput(scanner);

            switch (choice) {
                case 1:
                    doctorService.viewPatientList(scanner);
                    break;
                case 2:
                    doctorService.viewSchedule(scanner);
                    break;
                case 3:
                    doctorService.displayUpcomingAppointments(scanner);
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
