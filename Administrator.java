package project;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Administrator {
    private String adminId;
    private String name;
    private List<Staff> staffList = new ArrayList<>();
    private List<Medication> inventory = new ArrayList<>();
    private List<Patient> patients = new ArrayList<>();
    private List<Doctor> doctors = new ArrayList<>();
    private ArrayList<Appointment> appointmentList = new ArrayList<>();

    
    // Constructor
    public Administrator(String adminId, String name) {
        this.adminId = adminId;
        this.name = name;
    }

    // View and manage hospital staff
    public void viewStaffList() {
        for (Staff staff : staffList) {
            System.out.println(staff);
        }
    }
    
 // Modify addStaff method to accept individual parameters
    public void addStaff(String staffID, String staffName, String staffRole, String staffGender, int staffAge) {
        Staff newStaff = new Staff(staffID, staffName, staffRole, staffGender, staffAge);
        staffList.add(newStaff); // Assuming staffList is a collection that holds Staff objects
        System.out.println("Staff added successfully!");
    }

 // Method to handle input for adding staff
    public static void addStaffInput(Administrator admin, Scanner scanner) {
        System.out.print("Enter staff ID: ");
        String staffID = scanner.nextLine();
        System.out.print("Enter staff name: ");
        String staffName = scanner.nextLine();
        System.out.print("Enter staff role (Doctor/Pharmacist): ");
        String staffRole = scanner.nextLine();
        System.out.print("Enter staff gender (Male/Female): ");
        String staffGender = scanner.nextLine();
        System.out.print("Enter staff age: ");
        int staffAge = scanner.nextInt();
        scanner.nextLine(); // consume newline

        admin.addStaff(staffID, staffName, staffRole, staffGender, staffAge);
    }

    
    

    // Method to handle input for removing staff
    public static void removeStaffInput(Administrator admin, Scanner scanner) {
        System.out.print("Enter staff ID to remove: ");
        String staffID = scanner.nextLine();

        admin.removeStaff(staffID);
    }


    public void updateStaff(String staffId, String staffName, String staffRole, String staffGender, int staffAge) {
        for (Staff staff : staffList) {
            if (staff.getId().equals(staffId)) {
                staff.setName(staffName); // Update staff name
                staff.setRole(staffRole); // Update staff role
                staff.setGender(staffGender); // Update staff gender
                staff.setAge(staffAge); // Update staff age
                System.out.println("Staff updated successfully.");
                return;
            }
        }
        System.out.println("Staff not found.");
    }
    public static void updateStaffInput(Administrator admin, Scanner scanner) {
        System.out.print("Enter staff ID to update: ");
        String staffID = scanner.nextLine();

        System.out.print("Enter new staff name: ");
        String staffName = scanner.nextLine();
        System.out.print("Enter new staff role (Doctor/Pharmacist): ");
        String staffRole = scanner.nextLine();
        System.out.print("Enter new staff gender (Male/Female): ");
        String staffGender = scanner.nextLine();
        System.out.print("Enter new staff age: ");
        int staffAge = scanner.nextInt();
        scanner.nextLine(); // consume newline

        admin.updateStaff(staffID, staffName, staffRole, staffGender, staffAge);
    }


    public void removeStaff(String staffId) {
        staffList.removeIf(staff -> staff.getId().equals(staffId));
        System.out.println("Staff removed successfully.");
    }

    // View appointments
    public void viewAppointments() {
        if (appointmentList.isEmpty()) {
            System.out.println("No appointments to display.");
            return;
        }
        
        for (Appointment appointment : appointmentList) {
            System.out.println("Patient ID: " + appointment.getPatientId());
            System.out.println("Doctor ID: " + appointment.getDoctorId());
            System.out.println("Status: " + appointment.getStatus());
            System.out.println("Date & Time: " + appointment.getDateTime());
            System.out.println("Outcome: " + appointment.getOutcome());
            System.out.println("----------------------");
        }
    }
    public static void addMedicationInput(Administrator admin, Scanner scanner) {
        System.out.print("Enter medication name: ");
        String name = scanner.nextLine();
        System.out.print("Enter stock level: ");
        int stockLevel = scanner.nextInt();
        System.out.print("Enter low stock level: ");
        int lowStockLevel = scanner.nextInt();
        scanner.nextLine(); // consume newline

        Medication medication = new Medication(name, stockLevel, lowStockLevel);
        admin.addMedication(medication);
    }

    public static void removeMedicationInput(Administrator admin, Scanner scanner) {
        System.out.print("Enter medication name to remove: ");
        String medicationName = scanner.nextLine();
        admin.removeMedication(medicationName);
    }

    public static void updateStockLevelInput(Administrator admin, Scanner scanner) {
        System.out.print("Enter medication name: ");
        String medicationName = scanner.nextLine();
        System.out.print("Enter new stock level: ");
        int newStockLevel = scanner.nextInt();
        scanner.nextLine(); // consume newline
        admin.updateStockLevel(medicationName, newStockLevel);
    }

    public static void updateLowStockLevelInput(Administrator admin, Scanner scanner) {
        System.out.print("Enter medication name: ");
        String medicationName = scanner.nextLine();
        System.out.print("Enter new low stock level: ");
        int newLowStockLevel = scanner.nextInt();
        scanner.nextLine(); // consume newline
        admin.updateLowStockLevel(medicationName, newLowStockLevel);
    }

    
    public static void addAppointmentInput(Administrator admin, Scanner scanner) {
        System.out.print("Enter Patient ID: ");
        String patientId = scanner.nextLine();
        
        System.out.print("Enter Doctor ID: ");
        String doctorId = scanner.nextLine();
        
        System.out.print("Enter Appointment Status (confirmed/cancelled/completed): ");
        String status = scanner.nextLine();
        
        System.out.print("Enter Date and Time (yyyy-MM-ddTHH:mm): ");
        String dateTimeInput = scanner.nextLine();
        LocalDateTime dateTime = LocalDateTime.parse(dateTimeInput);
        
        System.out.print("Enter Appointment Outcome: ");
        String outcome = scanner.nextLine();
        
        admin.addAppointment(patientId, doctorId, status, dateTime, outcome);
    }

    
    public void addAppointment(String patientId, String doctorId, String status, LocalDateTime dateTime, String outcome) {
        Appointment newAppointment = new Appointment(patientId, doctorId, status, dateTime, outcome);
        appointmentList.add(newAppointment);
        System.out.println("Appointment added successfully.");
    }



    // Helper methods to find Patient and Doctor by ID
    private Patient findPatientById(String patientId) {
        for (Patient patient : patients) {
            if (patient.getPatientId().equals(patientId)) {
                return patient;
            }
        }
        return null; // Patient not found
    }

    private Doctor findDoctorById(String doctorId) {
        for (Doctor doctor : doctors) {
            if (doctor.getDoctorId().equals(doctorId)) {
                return doctor;
            }
        }
        return null; // Doctor not found
    }

    // Manage medication inventory
    public void viewInventory() {
        for (Medication medication : inventory) {
            System.out.println(medication);
        }
    }

    public void addMedication(Medication medication) {
        inventory.add(medication);
        System.out.println("Medication added successfully.");
    }

    public void removeMedication(String medicationName) {
        inventory.removeIf(medication -> medication.getName().equalsIgnoreCase(medicationName));
        System.out.println("Medication removed successfully.");
    }

    public void updateStockLevel(String medicationName, int newStockLevel) {
        for (Medication medication : inventory) {
            if (medication.getName().equalsIgnoreCase(medicationName)) {
                medication.setStockLevel(newStockLevel);
                System.out.println("Stock level updated successfully.");
                return;
            }
        }
        System.out.println("Medication not found.");
    }

    public void updateLowStockLevel(String medicationName, int newLowStockLevel) {
        for (Medication medication : inventory) {
            if (medication.getName().equalsIgnoreCase(medicationName)) {
                medication.setLowStockLevel(newLowStockLevel);
                System.out.println("Low stock level updated successfully.");
                return;
            }
        }
        System.out.println("Medication not found.");
    }

    // Approve replenishment requests
    public void approveReplenishment(String medicationName) {
        for (Medication medication : inventory) {
            if (medication.getName().equalsIgnoreCase(medicationName)) {
                int replenishmentAmount = medication.getLowStockLevel() * 2; // Example logic
                medication.setStockLevel(medication.getStockLevel() + replenishmentAmount);
                System.out.println("Replenishment approved. Stock updated.");
                return;
            }
        }
        System.out.println("Medication not found.");
    }
    
 // Method to handle input for approving replenishment requests
    public static void approveReplenishmentInput(Administrator admin, Scanner scanner) {
        System.out.print("Enter the name of the medication to approve replenishment: ");
        String medicationName = scanner.nextLine();
        admin.approveReplenishment(medicationName);
    }


    public static void main(String[] args) {
        Administrator admin = new Administrator("A001", "Admin");
        Scanner scanner = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\n--- Administrator Menu ---");
            System.out.println("1. View and Manage Hospital Staff");
            System.out.println("2. View Appointments Details");
            System.out.println("3. View and Manage Medication Inventory");
            System.out.println("4. Approve Replenishment Requests");
            System.out.println("5. Logout");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    manageStaff(admin, scanner); // Now references the correct methods
                    break;
                case 2:
                    admin.viewAppointments();
                    break;
                case 3:
                    manageInventory(admin, scanner);
                    break;
                case 4:
                    approveReplenishmentInput(admin, scanner);
                    break;
                case 5:
                    System.out.println("Logging out...");
                    break;
                case 6:
                    addAppointmentInput(admin, scanner);
            
                    break;
                default:
                    System.out.println("Invalid choice! Please try again.");
            }
        } while (choice != 5);

        scanner.close();
    }

    // Method to manage staff
    public static void manageStaff(Administrator admin, Scanner scanner) {
        int staffChoice;

        do {
            System.out.println("\n--- Manage Staff ---");
            System.out.println("1. Add Staff");
            System.out.println("2. Update Staff");
            System.out.println("3. Remove Staff");
            System.out.println("4. Back to Main Menu");
            System.out.print("Enter your choice: ");
            staffChoice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (staffChoice) {
                case 1:
                    addStaffInput(admin, scanner);
                    break;
                case 2:
                    updateStaffInput(admin, scanner);
                    break;
                case 3:
                    removeStaffInput(admin, scanner);
                    break;
                case 4:
                    System.out.println("Returning to main menu...");
                    break;
                default:
                    System.out.println("Invalid choice! Please try again.");
            }
        } while (staffChoice != 4);
    }


    // Method to manage medication inventory
    public static void manageInventory(Administrator admin, Scanner scanner) {
        System.out.println("\n--- Manage Medication Inventory ---");
        System.out.println("1. Add Medication");
        System.out.println("2. Remove Medication");
        System.out.println("3. Update Stock Level");
        System.out.println("4. Update Low Stock Alert Level");
        System.out.println("5. View Medication Inventory");
        System.out.println("6. Back to Main Menu");
        System.out.print("Enter your choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        switch (choice) {
            case 1:
                addMedicationInput(admin, scanner);
                break;
            case 2:
                removeMedicationInput(admin, scanner);
                break;
            case 3:
                updateStockLevelInput(admin, scanner);
                break;
            case 4:
                updateLowStockLevelInput(admin, scanner);
                break;
            case 5:
                System.out.println("\nMedication Inventory:");
                admin.viewInventory();
                break;
            case 6:
                return;
            default:
                System.out.println("Invalid choice! Please try again.");
        }
    }

}
