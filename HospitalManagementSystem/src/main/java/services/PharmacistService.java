/**
 * The PharmacistService class provides services and operations for pharmacists,
 * including managing medication inventory, handling replenishment requests, and
 * dispensing medications based on appointment outcomes.
 * 
 * <p>This service interacts with multiple repositories such as {@link PatientRepository},
 * {@link AppointmentRepository}, {@link MedicalRecordRepository}, and {@link MedicationRepository}
 * to perform its operations.</p>
 * 
 * <p>Key functionalities:</p>
 * <ul>
 *   <li>Displaying appointment outcomes and dispensing medications.</li>
 *   <li>Viewing the current medical inventory.</li>
 *   <li>Submitting replenishment requests for low-stock medications.</li>
 * </ul>
 * 
 * @author NICHOLAS CHANG CHIA KUAN
 * @version 1.0
 * @since 2024-11-15
 */
package services;

import model.MedicalRecord;
import model.Medication;
import repository.AppointmentRepository;
import repository.MedicalRecordRepository;
import repository.MedicationRepository;
import repository.PatientRepository;
import utils.Validator;

import java.io.IOException;
import java.util.*;

public class PharmacistService {

    /** Repository for patient data. */
    private final PatientRepository patientRepository = new PatientRepository();

    /** Repository for appointment data. */
    private final AppointmentRepository appointmentRepository = new AppointmentRepository();

    /** Repository for medical records. */
    private final MedicalRecordRepository medicalRecordRepository = new MedicalRecordRepository();

    /** Repository for medication inventory. */
    private final MedicationRepository medicationRepository = new MedicationRepository();

    /**
     * Constructor for PharmacistService.
     *
     * @throws IOException if an error occurs during initialization.
     */
    public PharmacistService() throws IOException {}

    /**
     * Displays all appointment outcomes and provides an option to dispense medications.
     *
     * @param scanner the Scanner object for user input.
     * @throws IOException if an error occurs during data retrieval or updating.
     */
    public void displayAllAppointmentOutcomes(Scanner scanner) throws IOException {
        List<MedicalRecord> appointmentOutcomes = medicalRecordRepository.getAllAppointmentOutcomes();

        if (appointmentOutcomes.isEmpty()) {
            System.out.println("Nothing to dispense!");
            return;
        }

        System.out.println("\n--- Appointment Outcomes ---");
        for (int i = 0; i < appointmentOutcomes.size(); i++) {
            System.out.println((i + 1) + " - " + appointmentOutcomes.get(i).getPatientId());
            System.out.println("Diagnosis: " + appointmentOutcomes.get(i).getDiagnoses());
            System.out.println("Treatment: " + appointmentOutcomes.get(i).getTreatment());
            System.out.println("Prescription: " + appointmentOutcomes.get(i).getPrescriptionAmount() + "x "
                    + appointmentOutcomes.get(i).getPrescription());
        }
        System.out.println((appointmentOutcomes.size() + 1) + " Back");

        int dispenseChoice = Validator.validateIntegerInput(scanner);
        if (dispenseChoice > 0 && dispenseChoice <= appointmentOutcomes.size()) {
            boolean dispensed = medicationRepository.dispenseMedication(
                appointmentOutcomes.get(dispenseChoice - 1).getPrescription(),
                appointmentOutcomes.get(dispenseChoice - 1).getPrescriptionAmount()
            );
            if (dispensed) {
                medicalRecordRepository.dispenseMedication(appointmentOutcomes.get(dispenseChoice - 1));
                System.out.println("Dispensed");
            } else {
                System.out.println("Not enough medication to dispense");
            }
        } else if (dispenseChoice == (appointmentOutcomes.size() + 1)) {
            return;
        } else {
            System.out.println("Invalid choice");
        }
    }

    /**
     * Displays the current medication inventory with details like stock levels and alert thresholds.
     *
     * @param scanner the Scanner object for user input.
     * @throws IOException if an error occurs during data retrieval.
     */
    public void displayMedicalInventory(Scanner scanner) throws IOException {
        List<Medication> medicineList = medicationRepository.getMedicationList();

        System.out.println("\n--- Medication List ---");
        System.out.println("Medication Name | Stock Level | Alert Level");
        for (Medication medicalInventory : medicineList) {
            System.out.println(medicalInventory.getMedicineName() + " | " + medicalInventory.getCurrentStock() + " | " + medicalInventory.getLowStockLevel());
        }
    }

    /**
     * Submits a replenishment request for low-stock medications.
     *
     * @param scanner the Scanner object for user input.
     * @throws IOException if an error occurs during data retrieval or updating.
     */
    public void submitReplenishRequest(Scanner scanner) throws IOException {
        List<Medication> medicationList = medicationRepository.getMedicationList();
        List<Medication> restockList = new ArrayList<>();

        System.out.println("\n--- Medication List ---");
        System.out.println("Medication Name | Stock Level | Alert Level");
        for (Medication medication : medicationList) {
            if (medication.isLowStock()) {
                restockList.add(medication);
                System.out.println((restockList.size()) + " - " + medication.getMedicineName() + " | " + medication.getCurrentStock() + " | " + medication.getLowStockLevel());
            }
        }

        if (restockList.isEmpty()) {
            System.out.println("None of the medications are at a low stock level");
            return;
        }

        System.out.println((restockList.size() + 1) + " - Back");
        System.out.print("Submit Replenish Request for: ");
        int replenishRequest = Validator.validateIntegerInput(scanner);

        if (replenishRequest == (restockList.size() + 1)) {
            return;
        }

        if (replenishRequest > 0 && replenishRequest <= restockList.size()) {
            System.out.println("Replenish Amount for " + restockList.get(replenishRequest - 1).getMedicineName() + ": ");
            int replenishAmount = Validator.validateIntegerInput(scanner);
            medicationRepository.submitReplenishRequest(restockList.get(replenishRequest - 1), replenishAmount);
            System.out.println("Replenish Request Submitted!");
        } else {
            System.out.println("Invalid Input!");
        }
    }
}
