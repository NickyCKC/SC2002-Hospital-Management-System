package services;

import constants.FilePath;
import enums.Gender;
import enums.Role;
import enums.Status;
import model.Appointment;
import model.MedicalRecord;
import model.Medication;
import model.Staff;
import repository.*;
import utils.Validator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * AdminService handles various administrative operations related to hospital staff,
 * medication inventory, and appointment management.
 * <p>
 * It provides functionalities such as viewing and managing hospital staff, handling
 * medication inventory, and managing appointments and replenish requests.
 * </p>
 *
 * @author CHARMAINE LIEW YINGSUI
 * @version 1.0
 * @since 2024-11-15
 */
public class AdminService {

    private final AccountRepository accountRepository = new AccountRepository();
    private final StaffRepository staffRepository = new StaffRepository();
    private final MedicationRepository medicationRepository = new MedicationRepository();
    private final AppointmentRepository appointmentRepository = new AppointmentRepository();
    private final MedicalRecordRepository medicalRecordRepository = new MedicalRecordRepository();

    private List<Staff> staffList = staffRepository.getAllStaff();

    /**
     * Constructs an AdminService instance, initializing the repository objects.
     * 
     * @throws IOException if an error occurs while initializing repositories
     */
    public AdminService() throws IOException {
    }

    // Staff Management

    /**
     * Displays the list of hospital staff and provides filtering options.
     * 
     * @param scanner Scanner object to read user input
     */
    public void displayStaffList(Scanner scanner) {
        staffList = staffRepository.getAllStaff();
        List<Staff> filteredStaffList = staffList;

        boolean exit = false;

        do {
            // Show Staff List
            System.out.println("\n--- Staff List ---");
            for (Staff staff : filteredStaffList) {
                System.out.println(staff.getHospitalId() + " | " + staff.getRole() + " | " + staff.getGender() + " | " + staff.getAge());
            }

            System.out.println("\n--- Staff Filter ---");
            System.out.println("1. Filter By Role");
            System.out.println("2. Filter By Gender");
            System.out.println("3. Filter By Age");
            System.out.println("4. Back");
            System.out.print("Enter your choice: ");
            int choice = Validator.validateIntegerInput(scanner);

            switch (choice) {
                case 1:
                    System.out.println("Enter Role (Doctor/Pharmacist): ");
                    String role = Validator.validateStringInput(scanner);
                    filteredStaffList = filterStaffByRole(staffList, role);
                    break;
                case 2:
                    System.out.println("Enter Gender (Male or Female): ");
                    String gender = Validator.validateStringInput(scanner);
                    filteredStaffList = filterStaffByGender(staffList, gender);
                    break;
                case 3:
                    System.out.println("Enter lower age limit (included in range to display staff): ");
                    int lower = Validator.validateIntegerInput(scanner);
                    System.out.println("Enter upper age limit (included in range to display staff): ");
                    int upper = Validator.validateIntegerInput(scanner);
                    filteredStaffList = filterStaffByAge(staffList, upper, lower);
                    break;
                case 4:
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid choice! Please try again.");
            }
        } while (!exit);
    }

    /**
     * Prompts the user for input to create a new staff member and saves it.
     * 
     * @param scanner Scanner object to read user input
     * @throws Exception if an error occurs while saving the new staff member
     */
    public void displayCreateNewStaffInput(Scanner scanner) throws Exception {
        System.out.print("Enter Hospital ID: ");
        String staffID = Validator.validateStringInput(scanner);

        // check for Doctor or Pharmacist
        String staffRole;
        do {
            System.out.print("Enter Staff Role (Doctor/Pharmacist): ");
            staffRole = scanner.nextLine();

        } while (!staffRole.toUpperCase().equals(Role.DOCTOR.getDisplayValue()) && !staffRole.toUpperCase().equals(Role.PHARMACIST.getDisplayValue()));

        // check for Male or Female
        String staffGender;
        do {
            System.out.print("Enter Staff Gender (Male/Female): ");
            staffGender = scanner.nextLine();

        } while (!staffGender.toUpperCase().equals(Gender.MALE.getDisplayValue()) && !staffGender.toUpperCase().equals(Gender.FEMALE.getDisplayValue()));

        System.out.print("Enter Staff Age: ");
        int staffAge = Validator.validateIntegerInput(scanner);

        Staff newStaff = new Staff();
        newStaff.setHospitalId(staffID);
        newStaff.setRole(staffRole.toUpperCase());
        newStaff.setGender(staffGender.toUpperCase());
        newStaff.setAge(staffAge);

        accountRepository.saveNewAccount(newStaff);
        staffRepository.saveNewStaff(newStaff);
        staffList = staffRepository.getAllStaff();
    }

    // Method to handle input for removing staff

    /**
     * Allows the user to remove a staff member from the list.
     * 
     * @param scanner Scanner object to read user input
     * @throws IOException if an error occurs during staff removal
     */
    public void displayRemoveStaffInput(Scanner scanner) throws IOException {
        if (staffList.isEmpty()) {
            System.out.println("No staff found");
            return;
        }

        // Reveal Staff Choices to Remove
        int choices = 0;
        for (int i = 0; i < staffList.size(); i++) {
            System.out.println("[" + (i+1) + "] " + staffList.get(i).getHospitalId());
            choices++;
        }

        System.out.print("Enter (1 - " + choices + ") to remove staff");

        // Remove Staff
        int staffToRemove = Validator.validateIntegerInput(scanner);
        removeStaff(staffList.get(staffToRemove - 1).getHospitalId());

        // reset the staff list since the changes to the staff list
        staffList = staffRepository.getAllStaff();
    }

    /**
     * Removes a staff member from the system based on their hospital ID.
     * 
     * @param hospitalId The hospital ID of the staff member to be removed
     * @throws IOException if an error occurs during staff removal
     */
    private void removeStaff(String hospitalId) throws IOException {
        accountRepository.deleteAccount(hospitalId);
        staffRepository.deleteStaff(hospitalId);
    }
    
    // Method to handle input for updating staff

    /**
     * Allows the admin to update a staff details from the list.
     * 
     * @param scanner Scanner object to read user input
     * @throws IOException if an error occurs during staff removal
     */
    
    public void displayUpdateStaffInput (Scanner scanner) throws IOException {
        if (staffList.isEmpty()) {
            System.out.println("No staff found");
            return;
        }

        // Reveal Staff Choices to Remove
        int choices = 0;
        for (int i = 0; i < staffList.size(); i++) {
            System.out.println("[" + (i+1) + "] " + staffList.get(i).getHospitalId());
            choices++;
        }

        System.out.print("Enter (1 - " + choices + ") to edit staff");
        Staff staffToEdit = staffList.get(Validator.validateIntegerInput(scanner) - 1);

        // check for Doctor or Pharmacist
        String staffRole;

        while (true) {
            System.out.print("Enter Staff Role (Doctor/Pharmacist): ");
            staffRole = Validator.validateStringInput(scanner);

            if (staffRole.toUpperCase().equals(Role.DOCTOR.getDisplayValue()) || staffRole.toUpperCase().equals(Role.PHARMACIST.getDisplayValue())) {
                staffToEdit.setRole(staffRole.toUpperCase());
                break;
            }
        }

        // check for Male or Female
        String staffGender;
        while (true) {
            System.out.print("Enter Staff Gender (Male/Female): ");
            staffGender = scanner.nextLine();

            if (staffGender.toUpperCase().equals(Gender.MALE.getDisplayValue()) || staffGender.toUpperCase().equals(Gender.FEMALE.getDisplayValue())) {
                staffToEdit.setGender(staffGender.toUpperCase());
                break;
            }
        }

        System.out.print("Enter New Staff Age: ");
        staffToEdit.setAge(Validator.validateIntegerInput(scanner));

        staffRepository.updateStaff(staffToEdit);
        staffList = staffRepository.getAllStaff();
    }

    // Filter methods

    /**
     * Filters the staff list by role.
     * 
     * @param staffList The original list of staff members
     * @param role The role to filter by (e.g., Doctor, Pharmacist)
     * @return A list of staff filtered by role
     */
    public List<Staff> filterStaffByRole(List<Staff> staffList, String role) {
        List<Staff> filteredStaffList = new ArrayList<>();

        for (Staff staff : staffList) {
            if (staff.getRole().equals(role.toUpperCase())) {
                filteredStaffList.add(staff);
            }
        }

        return filteredStaffList;
    }

    /**
     * Filters the staff list by gender.
     * 
     * @param staffList The original list of staff members
     * @param gender The gender to filter by (Male, Female)
     * @return A list of staff filtered by gender
     */
    public List<Staff> filterStaffByGender(List<Staff> staffList, String gender) {
        List<Staff> filteredStaffList = new ArrayList<>();

        for (Staff staff : staffList) {
            if (staff.getGender().equals(gender.toUpperCase())) {
                filteredStaffList.add(staff);
            }
        }

        return filteredStaffList;
    }

    /**
     * Filters the staff list by age range.
     * 
     * @param staffList The original list of staff members
     * @param upper The upper age limit
     * @param lower The lower age limit
     * @return A list of staff filtered by age range
     */
    public List<Staff> filterStaffByAge(List<Staff> staffList, int upper, int lower) {
        List<Staff> filteredStaffList = new ArrayList<>();

        for (Staff staff : staffList) {
            if (staff.getAge() >= lower && staff.getAge() <= upper) {
                filteredStaffList.add(staff);
            }
        }

        return filteredStaffList;
    }
    

    /**
     * Displays a list of all medicines in the inventory, showing their name, current stock, and low stock level.
     * @param scanner The scanner object used for user input. This parameter is not used in this method.
     * @throws IOException If there is an issue retrieving the medication list from the repository.
     */
    public void displayMedicalInventory (Scanner scanner) throws IOException {
        List<Medication> medicineList = medicationRepository.getMedicationList();

        System.out.println("\n--- Medicine List ---");
        for (Medication medicalInventory : medicineList) {
            System.out.println(medicalInventory.getMedicineName() + " | " + medicalInventory.getCurrentStock() + " | " + medicalInventory.getLowStockLevel());
        }
    }

    /**
     * Prompts the user to input details for a new medicine and adds it to the inventory.
     * @param scanner The scanner object used for user input.
     * @throws Exception If there is an issue during input validation or adding the new medicine.
     */
    public void displayCreateNewMedicineInput (Scanner scanner) throws Exception {
        System.out.print("Enter Medicine Name: ");
        String medicineName = Validator.validateStringInput(scanner);

        // check for Doctor or Pharmacist
        System.out.print("Enter Current Stock for " + medicineName +  ":");
        int currentStock = Validator.validateIntegerInput(scanner);

        System.out.print("Enter Low Stock Alert Level for " + medicineName + ":");
        int lowStockAlertLevel = Validator.validateIntegerInput(scanner);

        Medication newMedicine = new Medication();
        newMedicine.setMedicineName(medicineName);
        newMedicine.setCurrentStock(currentStock);
        newMedicine.setLowStockLevel(lowStockAlertLevel);
        newMedicine.setReplenishAmount(0);

        medicationRepository.addNewMedication(newMedicine);
    }

    /**
     * Allows an admin to update the stock and low stock alert level of an existing medicine in the inventory.
     * @param scanner The scanner object used for user input.
     * @throws IOException If there is an issue retrieving or updating the medication data from the repository.
     */
    public void displayUpdateMedicineInput (Scanner scanner) throws IOException {
        List<Medication> medicineList = medicationRepository.getMedicationList();

        if (medicineList.isEmpty()) {
            System.out.println("No medicine found");
            return;
        }

        // Reveal Medicine Choices to Update
        int choices = 0;
        for (int i = 0; i < medicineList.size(); i++) {
            System.out.println("[" + (i+1) + "] " + medicineList.get(i).getMedicineName());
            choices++;
        }

        System.out.print("Enter (1 - " + choices + ") to edit medicine:");
        Medication medicationToEdit = medicineList.get((Validator.validateIntegerInput(scanner) - 1));

        // check for new value for current stock
        int currentStock;
        while (true) {
            System.out.print("Enter current stock count: ");
            currentStock = Validator.validateIntegerInput(scanner);
            if(currentStock > 0) {
                medicationToEdit.setCurrentStock(currentStock);
                break;
            }
        }

        // check for new value for low stock alert level
        int lowStockAlertLevel;
        while (true) {
            System.out.print("Enter new low stock alert level count: ");
            lowStockAlertLevel = Validator.validateIntegerInput(scanner);
            if(lowStockAlertLevel > 0) {
                medicationToEdit.setLowStockLevel(lowStockAlertLevel);
                break;
            }
        }

        medicationRepository.updateMedication(medicationToEdit);
    }

    /**
     * Prompts the admin to select and remove a medicine from the inventory.
     * @param scanner The scanner object used for user input.
     * @throws IOException If there is an issue retrieving or removing the medication data from the repository.
     */
    public void displayRemoveMedicineInput (Scanner scanner) throws IOException {
        List<Medication> medicineList = medicationRepository.getMedicationList();

        if (medicineList.isEmpty()) {
            System.out.println("No medication found");
            return;
        }

        // Reveal Medicine Choices to Remove
        int choices = 0;
        for (int i = 0; i < medicineList.size(); i++) {
            System.out.println("[" + (i+1) + "] " + medicineList.get(i).getMedicineName());
            choices++;
        }

        System.out.print("Enter (1 - " + choices + ") to remove medicine: ");

        // Remove Staff
        int medicineToRemove = Validator.validateIntegerInput(scanner);
        medicationRepository.removeMedication(medicineList.get(medicineToRemove - 1).getMedicineName());
    }

    /**
     * Approves replenish requests for medicines that have requested stock replenishment.
     * Displays the replenish request details and prompts the user to approve a request.
     * @param scanner The scanner object used for user input.
     * @throws IOException If there is an issue retrieving the replenish request or updating the request status.
     */
    public void approveReplenishRequest(Scanner scanner) throws IOException {
        List<Medication> medicationList = medicationRepository.getMedicationList();
        List<Medication> replenishRequestList = new ArrayList<>();

        for (Medication medication : medicationList) {
            if (medication.getReplenishAmount() > 0) {
                replenishRequestList.add(medication);
                System.out.println((replenishRequestList.size()) + " " + medication.getMedicineName() + " | Requested Replenish Amount: " + medication.getReplenishAmount());
            }
        }

        if (replenishRequestList.isEmpty()) {
            System.out.println("No replenish request found.");
        } else {
            System.out.println("Approve Replenish Request for: ");
            int medicationChoice = Validator.validateIntegerInput(scanner);
            if (medicationChoice > 0 && medicationChoice <= replenishRequestList.size()) {
                List<List<String>> medicalInventoryFile = ExcelReaderWriter.read(FilePath.MEDICAL_INVENTORY_FILE_PATH);
                medicationRepository.approveReplenishRequest(replenishRequestList.get((medicationChoice-1)));
            }
        }
    }

    /**
     * Displays details of all appointments, including the doctor, patient, and status, and outcome for completed appointments.
     * @throws IOException If there is an issue retrieving the appointment or medical record data.
     */
    public void displayAppointmentDetails () throws IOException {
        List<Appointment> allAppointments = appointmentRepository.getAppointmentList();

        // Appointment Details Format (Doctor ID, Status, Appointment Holder(PatientID), Status, Outcome records for completed appointments
        System.out.println("\n--- All Appointments ---");
        for (Appointment appointment : allAppointments) {
            if (appointment.getAppointmentStatus().equals(Status.FREE.getDisplayValue())) {
                continue;
            }
            System.out.println("\n---- " + appointment.getAppointmentTime() + "------");
            System.out.println("Doctor ID: " + appointment.getDoctorId());
            System.out.println("Status: " + appointment.getAppointmentStatus());
            if (!appointment.getAppointmentStatus().equals(Status.FREE.getDisplayValue())) {
                System.out.println("Patient ID: " + appointment.getPatientId());
            }

            if (appointment.getAppointmentStatus().equals(Status.COMPLETED.getDisplayValue())) {
                System.out.println("\nAppointment Outcome: ");
                MedicalRecord medicalRecord = medicalRecordRepository.getMedicalRecordByAppointmentId(appointment.getAppointmentId());
                System.out.println("Appointment was on " + medicalRecord.getPastAppointment().getAppointmentTime());
                System.out.println("Diagnosis: " + medicalRecord.getDiagnoses());
                System.out.println("Treatment: " + medicalRecord.getTreatment());
                System.out.println("Prescription: " + medicalRecord.getPrescriptionAmount() + "x " + medicalRecord.getPrescription());
                System.out.println("Done By Doctor: " + medicalRecord.getPastAppointment().getDoctorId());
            }

            System.out.println("---------------------------------");
        }
    }

}
