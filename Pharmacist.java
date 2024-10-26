package hospital;

import java.util.*;

public class Pharmacist {
    // Declare private variables for id, name, gender, age, prescriptions, and inventory
    private String id;
    private String name;
    private String gender;
    private int age;
    private Map<String, List<Prescription>> prescriptions;
    private Map<String, Medication> inventory;

    // Constructor to initialize the variables
    public Pharmacist(String id, String name, String gender, int age) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.age = age;
        this.prescriptions = new HashMap<>();
        this.inventory = new HashMap<>();
    }

    // Getter and setter methods for id
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    // Getter and setter methods for name
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Getter and setter methods for gender
    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    // Getter and setter methods for age
    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    // Method to view the appointment outcome and prescribed medications for a patient
    public List<Prescription> viewAppointmentOutcome(String patientId) {
        return prescriptions.getOrDefault(patientId, new ArrayList<>());
    }

    // Method to update the status of a prescription (e.g., from pending to dispensed)
    public String updatePrescriptionStatus(String patientId, String medicationName, String newStatus) {
        List<Prescription> patientPrescriptions = prescriptions.get(patientId);
        if (patientPrescriptions != null) {
            for (Prescription prescription : patientPrescriptions) {
                if (prescription.getMedication().getName().equals(medicationName)) {
                    prescription.getMedication().setStatus(newStatus);
                    return "Prescription status updated";
                }
            }
        }
        return "Prescription not found";
    }

    // Method to view the current inventory of medications
    public List<Medication> viewInventory() {
        return new ArrayList<>(inventory.values());
    }

    // Method to submit a replenishment request for low-stock medications
    public String submitReplenishmentRequest(String medicationName, int quantity) {
        Medication medication = inventory.get(medicationName);
        if (medication != null && medication.getStockLevel() < medication.getLowStockAlertLevel()) {
            medication.setReplenishmentRequest(quantity);
            return "Replenishment request submitted";
        }
        return "Medication stock is sufficient or medication not found";
    }

    // Method to update the inventory when replenishment is approved
    public String updateInventory(String medicationName, int quantity) {
        Medication medication = inventory.get(medicationName);
        if (medication != null) {
            medication.setStockLevel(medication.getStockLevel() + quantity);
            return "Inventory updated";
        }
        return "Medication not found";
    }

    // Inner class for Prescription, Medication
    class Prescription {
        private Medication medication;
        private String dosage;
        private String duration;

        // Constructor to initialize the variables
        public Prescription(Medication medication, String dosage, String duration) {
            this.medication = medication;
            this.dosage = dosage;
            this.duration = duration;
        }

        // Getter and setter methods
        public Medication getMedication() {
            return medication;
        }

        public void setMedication(Medication medication) {
            this.medication = medication;
        }

        public String getDosage() {
            return dosage;
        }

        public void setDosage(String dosage) {
            this.dosage = dosage;
        }

        public String getDuration() {
            return duration;
        }

        public void setDuration(String duration) {
            this.duration = duration;
        }
    }

    class Medication {
        private String name;
        private int stockLevel;
        private int lowStockAlertLevel;
        private int replenishmentRequest;
        private String status;

        // Constructor to initialize the variables
        public Medication(String name, int stockLevel, int lowStockAlertLevel) {
            this.name = name;
            this.stockLevel = stockLevel;
            this.lowStockAlertLevel = lowStockAlertLevel;
            this.status = "pending";
        }

        // Getter and setter methods
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getStockLevel() {
            return stockLevel;
        }

        public void setStockLevel(int stockLevel) {
            this.stockLevel = stockLevel;
        }

        public int getLowStockAlertLevel() {
            return lowStockAlertLevel;
        }

        public void setLowStockAlertLevel(int lowStockAlertLevel) {
            this.lowStockAlertLevel = lowStockAlertLevel;
        }

        public int getReplenishmentRequest() {
            return replenishmentRequest;
        }

        public void setReplenishmentRequest(int replenishmentRequest) {
            this.replenishmentRequest = replenishmentRequest;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}
