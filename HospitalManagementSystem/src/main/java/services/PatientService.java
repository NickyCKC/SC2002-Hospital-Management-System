/**
 * Provides services for patients, including accessing medical records, updating contact information,
 * and managing appointments.
 * @author ARIEL KWOK DAI HUI
 * @version 1.0
 * @since 2024-11-15
 */

package services;

import constants.FilePath;
import model.*;
import repository.AppointmentRepository;
import repository.PatientRepository;
import repository.StaffRepository;
import utils.DateUtils;
import utils.Validator;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PatientService {
    private Patient patientInfo = new Patient();

    private final PatientRepository patientRepository = new PatientRepository();
    private final AppointmentRepository appointmentRepository = new AppointmentRepository();
    private final StaffRepository staffRepository = new StaffRepository();

    /**
     * Default constructor for initializing patient services.
     *
     * @throws IOException if data cannot be loaded from repositories.
     */
    public PatientService() throws IOException {
    }

    /**
     * Displays the patient's medical records, including personal details, diagnoses, treatments, 
     * and prescriptions.
     */
    public void displayMedicalRecords() {
        System.out.println("\n-- Medical Record --");
        System.out.println("Patient ID : " + patientInfo.getPatientId());
        System.out.println("Patient Name : " + patientInfo.getPatientName());
        System.out.println("Date of Birth : " + DateUtils.formatDate(patientInfo.getDateOfBirth()));
        System.out.println("Gender : " + patientInfo.getGender());
        System.out.println("Blood Type : " + patientInfo.getBloodType());
        System.out.println("Contact No. : " + patientInfo.getContactNo());
        System.out.println("Email : " + patientInfo.getEmail());

        for (MedicalRecord record : patientInfo.getMedicalRecords()) {
            System.out.println("---------------------------------------");
            System.out.println("Diagnosis : " + record.getDiagnoses());
            System.out.println("Treatment : " + record.getTreatment());
            if (record.getPrescription() != null) {
                System.out.println("Prescription : " + record.getPrescriptionAmount() + "x " + record.getPrescription());
            }
            System.out.println("---------------------------------------");
        }
    }

    /**
     * Loads patient information based on the hospital ID.
     *
     * @param hospitalId the unique identifier for the patient.
     * @throws IOException if patient data cannot be retrieved.
     */
    public void loadPatientInfo(String hospitalId) throws IOException {
        patientInfo = patientRepository.loadPatientInfo(hospitalId);
    }

    /**
     * Displays options to update the patient's contact information.
     *
     * @param scanner a {@code Scanner} for user input.
     * @throws IOException if the contact information cannot be updated.
     */
    public void displayUpdateContactInformation(Scanner scanner) throws IOException {
        System.out.println("\n-- Update Contact Information --");
        System.out.println("1 Contact Number");
        System.out.println("2 Email");

        int choice = Validator.validateIntegerInput(scanner);

        while (true) {
            if (choice == 1) {
                System.out.print("Enter New Contact Number: ");
            } else {
                System.out.print("Enter New Email Address: ");
            }

            String input = Validator.validateStringInput(scanner);

            if (choice == 1 && Validator.validateContactNo(input)) {
                updateContactNo(input);
                break;
            } else if (choice == 2 && Validator.validateEmail(input)) {
                updateEmail(input);
                break;
            }
        }
    }

    /**
     * Updates the patient's contact number.
     *
     * @param contactNo the new contact number.
     * @throws IOException if the contact number cannot be updated.
     */
    public void updateContactNo(String contactNo) throws IOException {
        patientInfo.setContactNo(contactNo);
        patientRepository.updatePatientInfo(patientInfo);
    }

    /**
     * Updates the patient's email address.
     *
     * @param email the new email address.
     * @throws IOException if the email address cannot be updated.
     */
    public void updateEmail(String email) throws IOException {
        patientInfo.setEmail(email);
        patientRepository.updatePatientInfo(patientInfo);
    }

    /**
     * Displays the list of upcoming appointments for the patient, with options to reschedule.
     *
     * @param scanner a {@code Scanner} for user input.
     * @throws IOException if upcoming appointments cannot be retrieved.
     */
    public void displayUpcomingAppointments(Scanner scanner) throws IOException {
        List<Appointment> upcomingAppointments = appointmentRepository.getUpcomingAppointmentsForPatient(patientInfo);

        if (upcomingAppointments.isEmpty()) {
            System.out.println("No Upcoming Appointments!");
            return;
        }

        System.out.println("\n--- Upcoming Appointments ---");
        for (Appointment upcomingAppointment : upcomingAppointments) {
            System.out.println(" Appointment with " + upcomingAppointment.getDoctorId()
                    + " at " + upcomingAppointment.getAppointmentTime()
                    + " | Status: " + upcomingAppointment.getAppointmentStatus());
        }

        System.out.println("\n------------------------------");
        System.out.println("1 Reschedule");
        System.out.println("2 Back");

        int input = Validator.validateIntegerInput(scanner);

        if (input == 1) {
            rescheduleAppointment(scanner);
        }
    }

    /**
     * Displays a list of doctors for the patient to choose from for appointment booking.
     *
     * @param scanner a {@code Scanner} for user input.
     * @return {@code true} if a time slot is successfully booked; {@code false} otherwise.
     * @throws IOException if the list of doctors cannot be retrieved.
     */
    public boolean displayDoctorsChoiceMenu(Scanner scanner) throws IOException {
        System.out.println("\n---Doctor Choice Menu---");

        List<String> doctorList = staffRepository.getAllDoctors();

        for (int i = 0; i < doctorList.size(); i++) {
            System.out.println((i + 1) + " - " + doctorList.get(i));
        }

        int doctorChoice = Validator.validateIntegerInput(scanner);
        return checkAppointmentSlots(scanner, doctorList.get(doctorChoice - 1));
    }

    /**
     * Checks available appointment slots for a selected doctor and books a slot if available.
     *
     * @param scanner a {@code Scanner} for user input.
     * @param doctorChoice the ID of the selected doctor.
     * @return {@code true} if a slot is successfully booked; {@code false} otherwise.
     * @throws IOException if appointment slots cannot be retrieved.
     */
    public boolean checkAppointmentSlots(Scanner scanner, String doctorChoice) throws IOException {
        System.out.println("\n-- Available Appointment Slots---");
        List<LocalDate> availableDates = new ArrayList<>();
        availableDates.add(LocalDate.now().plusDays(1));
        availableDates.add(LocalDate.now().plusDays(2));

        for (int i = 0; i < availableDates.size(); i++) {
            System.out.println((i + 1) + " - " + availableDates.get(i));
        }
        int dateChoice = Validator.validateIntegerInput(scanner);

        List<LocalTime> timeSlotsAvailable = appointmentRepository.getAvailableTimeSlots(doctorChoice, availableDates.get(dateChoice - 1));

        if (timeSlotsAvailable.isEmpty()) {
            System.out.println("No Time Slots Available for " + doctorChoice);
            return false;
        }

        System.out.println("\n--- Time Slots Available for " + availableDates.get(dateChoice - 1) + "---");
        for (int i = 0; i < timeSlotsAvailable.size(); i++) {
            System.out.println((i + 1) + " - " + timeSlotsAvailable.get(i));
        }

        System.out.println("\n--- Book Slot for " + availableDates.get(dateChoice - 1) + "---");
        int selectedTime = Validator.validateIntegerInput(scanner);

        appointmentRepository.bookAppointment(patientInfo.getPatientId(), doctorChoice, availableDates.get(dateChoice - 1), timeSlotsAvailable.get(selectedTime - 1));
        return true;
    }

    /**
 * Reschedules an upcoming appointment for the patient by freeing the current time slot
 * and allowing the patient to select a new appointment with a doctor.
 *
 * @param scanner a {@code Scanner} for user input.
 * @throws IOException if the appointment list or doctors' schedules cannot be retrieved.
 */
public void rescheduleAppointment(Scanner scanner) throws IOException {
    List<Appointment> appointmentList = appointmentRepository.getUpcomingAppointmentsForPatient(patientInfo);

    System.out.println("\n--- Upcoming Appointments ---");
    for (int i = 0; i < appointmentList.size(); i++) {
        System.out.println((i + 1) + " Appointment with " + appointmentList.get(i).getDoctorId()
                + " at " + appointmentList.get(i).getAppointmentTime()
                + " | Status: " + appointmentList.get(i).getAppointmentStatus());
    }
    System.out.print("Reschedule: ");
    int appointmentToReschedule = Validator.validateIntegerInput(scanner);

    boolean reschedule = displayDoctorsChoiceMenu(scanner);

    if (reschedule) {
        appointmentRepository.freeAppointmentSlot((appointmentList.get(appointmentToReschedule - 1)));
    }
}

/**
 * Cancels an upcoming appointment for the patient by freeing the current time slot.
 *
 * @param scanner a {@code Scanner} for user input.
 * @throws IOException if the appointment list cannot be retrieved.
 */
public void cancelAppointment(Scanner scanner) throws IOException {
    List<Appointment> appointmentList = appointmentRepository.getUpcomingAppointmentsForPatient(patientInfo);

    System.out.println("\n--- Upcoming Appointments ---");
    for (int i = 0; i < appointmentList.size(); i++) {
        System.out.println((i + 1) + " Appointment with " + appointmentList.get(i).getDoctorId()
                + " at " + appointmentList.get(i).getAppointmentTime()
                + " | Status: " + appointmentList.get(i).getAppointmentStatus());
    }
    System.out.print("Cancel: ");
    int appointmentToCancel = Validator.validateIntegerInput(scanner);

    appointmentRepository.cancelAppointmentSlot((appointmentList.get(appointmentToCancel - 1)));
}

/**
 * Displays the patient's past appointments, showing the diagnosis, treatment, prescription, 
 * and the doctor who handled the appointment.
 *
 * @param scanner a {@code Scanner} for user input (not used in this method but included for consistency).
 * @throws IOException if the patient's medical records cannot be retrieved.
 */
public void displayPastAppointments(Scanner scanner) throws IOException {
    List<MedicalRecord> pastAppointmentMedicalRecords = new ArrayList<>();

    for (MedicalRecord record : patientInfo.getMedicalRecords()) {
        if (record.getPastAppointment() != null) {
            pastAppointmentMedicalRecords.add(record);
        }
    }

    System.out.println("\n---- Past Appointments ----");

    for (MedicalRecord medicalRecord : pastAppointmentMedicalRecords) {
        System.out.println("Appointment was on " + medicalRecord.getPastAppointment().getAppointmentTime());
        System.out.println("Diagnosis: " + medicalRecord.getDiagnoses());
        System.out.println("Treatment: " + medicalRecord.getTreatment());
        System.out.println("Prescription: " + medicalRecord.getPrescriptionAmount() + "x " + medicalRecord.getPrescription());
        System.out.println("Done by: " + medicalRecord.getPastAppointment().getDoctorId());
        System.out.println("---------------------------");
    }
}

}
