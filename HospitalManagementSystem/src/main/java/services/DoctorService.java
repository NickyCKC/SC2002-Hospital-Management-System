package services;

import enums.Status;
import model.*;
import repository.*;
import utils.DateUtils;
import utils.Validator;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

/**
 * This class provides services related to the doctor's operations, including managing medical records, appointments, and schedules.
 * It facilitates viewing and updating patient information, managing appointment timeslots, and handling prescriptions.
 * <p>
 * The DoctorService class allows the doctor to interact with patient data, view and modify their medical records, manage their daily schedule,
 * and handle appointment requests and updates.
 * </p>
 * 
 * @author LI LIYI
 * @version 1.0
 * @since 2024-11-15
 */
public class DoctorService {
    private Doctor doctorInfo = new Doctor();

    private final StaffRepository staffRepository = new StaffRepository();
    private final PatientRepository patientRepository = new PatientRepository();
    private final MedicalRecordRepository medicalRecordRepository = new MedicalRecordRepository();
    private final AppointmentRepository appointmentRepository = new AppointmentRepository();
    private final MedicationRepository medicationRepository = new MedicationRepository();

    /**
     * Constructor for the DoctorService class, initializing necessary repositories.
     * 
     * @throws IOException If there is an issue initializing repositories or loading data.
     */
    public DoctorService() throws IOException {
    }

    /**
     * Loads the doctor's information based on the provided hospital ID.
     * 
     * @param hospitalId The hospital ID to identify the doctor.
     * @throws IOException If there is an issue retrieving the doctor's information from the repository.
     */
    public void loadDoctorInfo (String hospitalId) throws IOException {
        doctorInfo = staffRepository.getDoctorByHospitalId(hospitalId);
    }

    // Medical Record Management

    /**
     * Displays the list of patients under the doctor's care and allows the doctor to select a patient for further actions.
     * 
     * @param scanner The scanner object used for user input.
     * @throws IOException If there is an issue retrieving patient data.
     */
    public void viewPatientList(Scanner scanner) throws IOException {
        List<Patient> patientList = patientRepository.retrievePatientsUnderDoctor(doctorInfo);

        System.out.println("\n -- Patients under your care --");
        for (int i = 0; i < patientList.size(); i++) {
            System.out.println("(" + (i+1) + ") " + patientList.get(i).getPatientName());
        }
        System.out.println("(" + (patientList.size() + 1) + ") Back");

        // Options on what to do after selecting the patient, update etc...
        while (true) {
            int choice = Validator.validateIntegerInput(scanner);
            if (choice == patientList.size() + 1) break;
            if (choice <= patientList.size()) {
                displayUpdatePatientMenu(patientList.get(choice-1), scanner);
                break;
            }
        }
    }

    /**
     * Displays and updates a patient's medical records.
     * 
     * @param patient The patient whose records need to be updated.
     * @param scanner The scanner object used for user input.
     * @throws IOException If there is an issue retrieving or updating the patient's medical records.
     */
    public void displayUpdatePatientMenu (Patient patient, Scanner scanner) throws IOException {
        System.out.println("\n --- Patient Medical Records ---");
        System.out.println("Patient ID : " + patient.getPatientId());
        System.out.println("Patient Name : " + patient.getPatientName());
        System.out.println("Date of Birth : " + DateUtils.formatDate(patient.getDateOfBirth()));
        System.out.println("Gender : " + patient.getGender());
        System.out.println("Blood Type : " + patient.getBloodType());
        System.out.println("Contact No. : " + patient.getContactNo());
        System.out.println("Email : " + patient.getEmail());

        for (int i = 0; i < patient.getMedicalRecords().size(); i++) {
            System.out.println("----------------------------------------");
            System.out.println("Diagnosis : " + patient.getMedicalRecords().get(i).getDiagnoses());
            System.out.println("Treatment : " + patient.getMedicalRecords().get(i).getTreatment());
            if (patient.getMedicalRecords().get(i).getPrescription()!= null) {
                System.out.println("Prescription : " + patient.getMedicalRecords().get(i).getPrescriptionAmount() + "x " + patient.getMedicalRecords().get(i).getPrescription());
            }
            System.out.println("---------------------------------------");
        }

        System.out.println("1 - Add New Diagnosis");
        System.out.println("2 - Back");

        int choice = Validator.validateIntegerInput(scanner);
        if (choice == 1) {
            addMedicalRecord(patient.getPatientId(), scanner);
        } else if (choice != 2) {
           System.out.println("Invalid Choice");
        }

    }

    /**
     * Displays and manages the doctor's schedule for a specific date or the next available days.
     * 
     * @param scanner The scanner object used for user input.
     * @throws IOException If there is an issue retrieving the doctor's schedule from the repository.
     */
    public void viewSchedule(Scanner scanner) throws IOException {
        viewSchedule(scanner, null);
    }

    /**
     * Displays and manages the doctor's schedule for a specific date, allowing the doctor to view, approve/decline appointments, or add/remove time slots.
     * 
     * @param scanner The scanner object used for user input.
     * @param dateSelected The specific date to view the schedule for (optional).
     * @throws IOException If there is an issue retrieving the doctor's schedule from the repository.
     */
    public void viewSchedule(Scanner scanner, LocalDate dateSelected) throws IOException {
        LocalDate date = dateSelected;

        System.out.println("\n-- Schedule ---");

        if (date == null) {
            List<LocalDate> availableDates = new ArrayList<>();
            availableDates.add(LocalDate.now());
            availableDates.add(LocalDate.now().plusDays(1));
            availableDates.add(LocalDate.now().plusDays(2));

            for (int i = 0; i < availableDates.size(); i++) {
                System.out.println((i + 1) + " - " + availableDates.get(i));
            }
            int dateChoice = Validator.validateIntegerInput(scanner);

            date = availableDates.get((dateChoice-1));
        }

        List<Appointment> scheduleList = appointmentRepository.getDoctorSchedule(doctorInfo, date);

        if (scheduleList.isEmpty()) {
            System.out.println("Schedule is free for " + date);
        } else{
            // Sort times in ascending order (earliest to latest)
            scheduleList.sort(Comparator.comparing(appointment -> LocalDateTime.parse(appointment.getAppointmentTime(), DateUtils.getLocalDateTimeFormatter())));

            // Print sorted times
            for (Appointment appointment : scheduleList) {
                System.out.println("\n---------------------------");
                System.out.println("Appointment Time: " + appointment.getAppointmentTime());
                System.out.println("Status: " + appointment.getAppointmentStatus());
                if (appointment.getAppointmentStatus().equals(Status.PENDING.getDisplayValue()) ||
                        appointment.getAppointmentStatus().equals(Status.CONFIRMED.getDisplayValue())) {
                    System.out.println("Patient ID: " + appointment.getPatientId());
                }

                System.out.println("---------------------------");
            }
        }

        displayUpdateScheduleMenu(scheduleList, date, scanner);
    }

    /**
     * Displays the menu options for updating the doctor's schedule, such as approving/declining appointments or adding/removing time slots.
     * 
     * @param schedule The list of appointments for the selected date.
     * @param selectedDate The date for which the schedule is being updated.
     * @param scanner The scanner object used for user input.
     * @throws IOException If there is an issue modifying the doctor's schedule.
     */
    private void displayUpdateScheduleMenu(List<Appointment> schedule, LocalDate selectedDate, Scanner scanner) throws IOException {
        System.out.println("1 Approve/Decline Appointment");
        System.out.println("2 Add Timeslot");
        System.out.println("3 Remove Timeslot");
        System.out.println("4 Back");

        int choice = Validator.validateIntegerInput(scanner);

        if (choice == 1) {
            displayApprovalOfAppointment(schedule, selectedDate, scanner);
        }
        else if (choice == 2) {
            displayAddTimeSlotMenu(schedule, selectedDate, scanner);
        }
        else if (choice == 3) {
            displayRemoveTimeSlot(schedule, selectedDate, scanner);
        }
    }

    // Other methods...

    /**
     * Displays a menu for adding a new timeslot to the doctor's schedule.
     * It generates a list of available timeslots for the selected date 
     * and allows the user to choose one to add.
     * 
     * @param schedule The current schedule of the doctor.
     * @param selectedDate The date for which timeslots are being added.
     * @param scanner The scanner object used to take user input.
     * @throws IOException If an error occurs while interacting with the appointment repository.
     */
    private void displayAddTimeSlotMenu(List<Appointment> schedule, LocalDate selectedDate, Scanner scanner) throws IOException {
        System.out.println("\n--- Available Timeslots on " + selectedDate + " ---");
        List<LocalTime> generatedTimeSlots = generateTimeSlots(schedule);

        if(generatedTimeSlots.isEmpty()) {
            System.out.println("There are no available time slots for " + selectedDate);
            return;
        }

        for (int i = 0; i < generatedTimeSlots.size(); i++) {
            System.out.println((i + 1) + " - " + generatedTimeSlots.get(i));
        }

        int timeSlotChoice = Validator.validateIntegerInput(scanner);
        if (timeSlotChoice < 1 || timeSlotChoice > generatedTimeSlots.size()) return;

        // Creation of Time Slot
        Appointment newSchedule = new Appointment();
        newSchedule.setDoctorId(doctorInfo.getDoctorId());
        newSchedule.setPatientId(Status.FREE.getDisplayValue());
        newSchedule.setAppointmentTime(DateUtils.joinDateAsString(selectedDate, generatedTimeSlots.get(timeSlotChoice-1)));
        newSchedule.setAppointmentStatus(Status.FREE.getDisplayValue());
        appointmentRepository.addTimeSlotToSchedule(newSchedule);
        System.out.println("Schedule Updated!: ");
        viewSchedule(scanner, selectedDate);
    }

    /**
     * Generates a list of available time slots by removing the already occupied time slots
     * from the full list of hourly slots between 8 AM and 5 PM.
     * 
     * @param schedule The current schedule of the doctor to check against.
     * @return A list of available time slots.
     */
    private List<LocalTime> generateTimeSlots(List<Appointment> schedule) {
        List<LocalTime> unavailableTimeSlots = new ArrayList<>();
        List<LocalTime> allTimeSlots = new ArrayList<>();

        for (Appointment appointment : schedule) {
            unavailableTimeSlots.add(DateUtils.extractTime(appointment.getAppointmentTime()));
        }

        // Generate all hourly time slots from 8 AM to 5 PM
        LocalTime startTime = LocalTime.of(8, 0);
        LocalTime endTime = LocalTime.of(17, 0); // 5 PM

        while (!startTime.isAfter(endTime.minusHours(1))) {
            allTimeSlots.add(startTime);
            startTime = startTime.plusHours(1);
        }

        // Remove unavailable slots from allTimeSlots
        allTimeSlots.removeAll(unavailableTimeSlots);

        return allTimeSlots;
    }

    /**
     * Displays a menu to remove an existing timeslot from the doctor's schedule.
     * It lists all the free (unoccupied) timeslots for the selected date and allows the user
     * to choose one to remove.
     * 
     * @param schedule The current schedule of the doctor.
     * @param selectedDate The date for which a timeslot is being removed.
     * @param scanner The scanner object used to take user input.
     * @throws IOException If an error occurs while interacting with the appointment repository.
     */
    public void displayRemoveTimeSlot(List<Appointment> schedule, LocalDate selectedDate, Scanner scanner) throws IOException {
        List<Appointment> availableTimeSlotsToRemove = new ArrayList<>();

        for (Appointment appointment : schedule) {
            if (appointment.getAppointmentStatus().equals(Status.FREE.getDisplayValue())) {
                availableTimeSlotsToRemove.add(appointment);
                System.out.println(availableTimeSlotsToRemove.size() + " - " + appointment.getAppointmentTime());
            }
        }

        if (availableTimeSlotsToRemove.isEmpty()) {
            System.out.println("You cannot remove any time slots for " + selectedDate);
            viewSchedule(scanner, selectedDate);
            return;
        }

        int choice = Validator.validateIntegerInput(scanner);
        if (choice > 0 && choice <= availableTimeSlotsToRemove.size()) {
            appointmentRepository.removeTimeSlotFromSchedule(availableTimeSlotsToRemove.get(choice-1));
            System.out.println("Time Slot Removed From Schedule!");
            viewSchedule(scanner, selectedDate);
        }
    }

    /**
     * Displays a menu for approving or declining pending appointments for a selected date.
     * The user can approve a pending appointment, which will update its status to confirmed,
     * or decline it, which will free up the corresponding timeslot.
     * 
     * @param schedule The doctor's schedule for the selected date.
     * @param selectedDate The date for which appointments are being approved or declined.
     * @param scanner The scanner object used to take user input.
     * @throws IOException If an error occurs while interacting with the appointment repository.
     */
    public void displayApprovalOfAppointment(List<Appointment> schedule, LocalDate selectedDate, Scanner scanner) throws IOException {
        List<Appointment> pendingAppointments = new ArrayList<>();
        for (Appointment appointment : schedule) {
            if (appointment.getAppointmentStatus().equals(Status.PENDING.getDisplayValue())) {
                pendingAppointments.add(appointment);
                System.out.println(pendingAppointments.size() + " - " + appointment.getAppointmentTime() + " with " + appointment.getPatientId());
            }
        }

        if (pendingAppointments.isEmpty()) {
            System.out.println("No Pending Appointments for " + selectedDate);
            viewSchedule(scanner, selectedDate);
            return;
        }

        int choice = Validator.validateIntegerInput(scanner);

        if (choice < 1 || choice > pendingAppointments.size()) return;

        System.out.println("1 Accept"); // Accept change to Confirmed
        System.out.println("2 Decline"); // Decline straight away free up timeslot
        int approvalChoice = Validator.validateIntegerInput(scanner);

        if (approvalChoice == 1) {
            appointmentRepository.approveAppointment(pendingAppointments.get(choice-1));
            System.out.println("Appointment Approved!");
        } else if (approvalChoice == 2) {
            appointmentRepository.declineAppointment(pendingAppointments.get(choice-1));
            System.out.println("Appointment Declined!");
        } else {
            System.out.println("Invalid choice");
        }

        if (approvalChoice == 1 || approvalChoice == 2) {
            viewSchedule(scanner, selectedDate);
        }
    }

    /**
     * Displays the upcoming appointments for the doctor and allows the user to complete
     * the appointment or return to the previous menu.
     * 
     * @param scanner The scanner object used to take user input.
     * @throws IOException If an error occurs while interacting with the appointment repository.
     */
    public void displayUpcomingAppointments(Scanner scanner) throws IOException {
        List<Appointment> upcomingAppointments = appointmentRepository.getUpcomingAppointments(doctorInfo.getDoctorId());
        if (upcomingAppointments.isEmpty()) {
            System.out.println("No Upcoming Appointments for Today");
            return;
        }

        for (int i = 0; i < upcomingAppointments.size(); i++) {
            System.out.println((i+1) + " - " + upcomingAppointments.get(i).getPatientId() + " at " + upcomingAppointments.get(i).getAppointmentTime());
        }
        System.out.println((upcomingAppointments.size() + 1) + " - Back");
        int choice = Validator.validateIntegerInput(scanner);

        if (choice > 0 && choice <= upcomingAppointments.size()) {
            System.out.println("1 - Complete Appointment");
            System.out.println("2 - Back");
            int completeChoice = Validator.validateIntegerInput(scanner);
            if (completeChoice == 1) {
                addMedicalRecord(upcomingAppointments.get((choice-1)), scanner);
                appointmentRepository.completeAppointment(upcomingAppointments.get((choice-1)));
            } else {
                System.out.println("Invalid choice");
            }
        } else if (choice > (upcomingAppointments.size()+1)) {
            System.out.println("Invalid choice");
        }
    }


    /**
     * Adds a new medical record for a patient by asking the user for diagnosis, treatment, 
     * prescription, and the amount to prescribe. This method is called when only the patient ID is provided.
     * It delegates to another method that accepts the appointment and patient ID as parameters.
     * 
     * @param patientId The ID of the patient for whom the medical record is being created.
     * @param scanner The scanner object used to take user input.
     * @throws IOException If an error occurs while interacting with the medical record repository.
     */
    public void addMedicalRecord(String patientId, Scanner scanner) throws IOException {
        addMedicalRecord(null, scanner, patientId);
    }

    /**
     * Adds a new medical record for a patient by asking the user for diagnosis, treatment, 
     * prescription, and the amount to prescribe. This method is called when an appointment is provided.
     * 
     * @param appointment The appointment associated with the patient (if available).
     * @param scanner The scanner object used to take user input.
     * @throws IOException If an error occurs while interacting with the medical record repository.
     */
    public void addMedicalRecord(Appointment appointment, Scanner scanner) throws IOException {
        addMedicalRecord(appointment, scanner, appointment.getPatientId());
    }

    /**
     * Adds a new medical record for a patient by asking the user for diagnosis, treatment, 
     * prescription, and the amount to prescribe. This method is the core method for creating 
     * a medical record and it is called when both the appointment and patient ID are available.
     * It also handles user input for the prescription and ensures the inputs are valid before proceeding.
     * 
     * @param appointment The appointment associated with the patient (optional, may be null).
     * @param scanner The scanner object used to take user input.
     * @param patientId The ID of the patient for whom the medical record is being created.
     * @throws IOException If an error occurs while interacting with the medical record repository.
     */
    public void addMedicalRecord(Appointment appointment, Scanner scanner, String patientId) throws IOException {
        System.out.println("Enter Diagnosis: ");
        String diagnosis = Validator.validateStringInput(scanner);

        System.out.println("Enter Treatment: ");
        String treatment = Validator.validateStringInput(scanner);

        List<Medication> medicationList = medicationRepository.getMedicationList();
        System.out.println("Prescription Choice: ");
        for (int i = 0; i < medicationList.size(); i++) {
            System.out.println((i + 1) + " - " + medicationList.get(i).getMedicineName());
        }

        int prescriptionChoice = -111;
        do {
            if (prescriptionChoice != -111) {
                System.out.println("Invalid Input! Enter Again: ");
            }
            prescriptionChoice = Validator.validateIntegerInput(scanner);
        } while (prescriptionChoice <= 0 || prescriptionChoice > medicationList.size());

        System.out.println("Enter Amount to Prescribe: ");
        int prescriptionAmount = -111;
        do {
            if (prescriptionAmount != -111) {
                System.out.println("Invalid Amount! Enter Again: ");
            }
            prescriptionAmount = Validator.validateIntegerInput(scanner);
        } while (prescriptionAmount <= 0);

        // Create new medical record object
        MedicalRecord newMedicalRecord = new MedicalRecord();
        newMedicalRecord.setPatientId(patientId);
        newMedicalRecord.setDiagnoses(diagnosis);
        newMedicalRecord.setTreatment(treatment);
        newMedicalRecord.setPrescription(medicationList.get(prescriptionChoice - 1).getMedicineName());
        newMedicalRecord.setPrescriptionAmount(prescriptionAmount);
        newMedicalRecord.setStatus(Status.PENDING.getDisplayValue());

        if (appointment != null) {
            newMedicalRecord.setPastAppointment(appointment);
        }

        // Insert the new medical record into the repository
        medicalRecordRepository.insertNewMedicalRecord(newMedicalRecord);
    }

}
