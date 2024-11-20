/**
 * The PatientApp class provides a console-based application interface for patients.
 * It allows patients to manage their medical records, contact information, and appointments.
 * 
 * @author Lee Jia Qian Valerie
 * @version 1.0
 * @since 2024-11-15
 */
package main;

import services.PatientService;
import utils.Validator;

import java.io.IOException;
import java.util.Scanner;

public class PatientApp {

    /** Scanner for reading user input. */
    private final Scanner scanner;

    /** Service layer handling patient-related operations. */
    private final PatientService patientService = new PatientService();

    /**
     * Constructor for PatientApp. Initializes the application by loading the patient's information
     * and displays the patient menu.
     *
     * @param hospitalId the hospital ID of the logged-in patient.
     * @param scanner    the Scanner object for user input.
     * @throws Exception if an error occurs during initialization.
     */
    public PatientApp(String hospitalId, Scanner scanner) throws Exception {
        this.scanner = scanner;
        patientService.loadPatientInfo(hospitalId);
        displayPatientMenu();
    }

    /**
     * Displays the patient menu and handles menu navigation for various patient-related operations.
     *
     * @throws IOException if an error occurs while processing menu options.
     */
    public void displayPatientMenu() throws IOException {
        boolean loggedOut = false;

        do {
            System.out.println("\nWelcome to Patient Menu!");
            System.out.println("1 View Medical Records");
            System.out.println("2 Update Contact Information");
            System.out.println("3 View Upcoming Appointments");
            System.out.println("4 Book Appointment");
            System.out.println("5 Reschedule Appointment");
            System.out.println("6 Cancel Appointment");
            System.out.println("7 View Appointment Outcome Records");
            System.out.println("8 Logout");

            int choice = Validator.validateIntegerInput(scanner);

            switch (choice) {
                case 1:
                    patientService.displayMedicalRecords();
                    break;
                case 2:
                    patientService.displayUpdateContactInformation(scanner);
                    break;
                case 3:
                    patientService.displayUpcomingAppointments(scanner);
                    break;
                case 4:
                    patientService.displayDoctorsChoiceMenu(scanner);
                    break;
                case 5:
                    patientService.rescheduleAppointment(scanner);
                    break;
                case 6:
                    patientService.cancelAppointment(scanner);
                    break;
                case 7:
                    patientService.displayPastAppointments(scanner);
                    break;
                case 8:
                    loggedOut = true;
                    System.out.println("Logging out...");
                    break;
                default:
                    System.out.println("Invalid choice! Please try again.");
            }
        } while (!loggedOut);
    }
}
