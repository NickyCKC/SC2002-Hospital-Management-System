import java.util.*;

public class Doctor {
    // Declare private variables for id, name, gender, age, medicalRecords, schedule, and appointments
    private String id;
    private String name;
    private String gender;
    private int age;
    private Map<String, List<MedicalRecord>> medicalRecords;
    private List<Availability> schedule;
    private List<Appointment> appointments;

    // Constructor to initialize the variables
    public Doctor(String id, String name, String gender, int age) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.age = age;
        this.medicalRecords = new HashMap<>();
        this.schedule = new ArrayList<>();
        this.appointments = new ArrayList<>();
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

    // Method to view medical records of a patient
    public List<MedicalRecord> viewMedicalRecords(String patientId) {
        return medicalRecords.getOrDefault(patientId, new ArrayList<>());
    }

    // Method to update medical record of a patient
    public void updateMedicalRecord(String patientId, String diagnosis, String prescription, String treatmentPlan) {
        medicalRecords.computeIfAbsent(patientId, k -> new ArrayList<>())
                      .add(new MedicalRecord(diagnosis, prescription, treatmentPlan));
    }

    // Method to view schedule of the doctor
    public List<Availability> viewSchedule() {
        return schedule;
    }

    // Method to set availability of the doctor
    public void setAvailability(String startTime, String endTime) {
        schedule.add(new Availability(startTime, endTime));
    }

    // Method to accept an appointment
    public String acceptAppointment(String appointmentId) {
        for (Appointment appointment : appointments) {
            if (appointment.getId().equals(appointmentId)) {
                appointment.setStatus("accepted");
                return "Appointment accepted";
            }
        }
        return "Appointment not found";
    }

    // Method to decline an appointment
    public String declineAppointment(String appointmentId) {
        for (Appointment appointment : appointments) {
            if (appointment.getId().equals(appointmentId)) {
                appointment.setStatus("declined");
                return "Appointment declined";
            }
        }
        return "Appointment not found";
    }

    // Method to view upcoming appointments
    public List<Appointment> viewUpcomingAppointments() {
        List<Appointment> upcomingAppointments = new ArrayList<>();
        for (Appointment appointment : appointments) {
            if ("accepted".equals(appointment.getStatus())) {
                upcomingAppointments.add(appointment);
            }
        }
        return upcomingAppointments;
    }

    // Method to record the outcome of an appointment
    public String recordAppointmentOutcome(String appointmentId, String date, String serviceType, List<Medication> medications, String notes) {
        for (Appointment appointment : appointments) {
            if (appointment.getId().equals(appointmentId)) {
                appointment.setOutcome(new AppointmentOutcome(date, serviceType, medications, notes));
                return "Appointment outcome recorded";
            }
        }
        return "Appointment not found";
    }

    // Override the toString method to display the doctor's details
    @Override
    public String toString() {
        return "Doctor " + name + ", Gender: " + gender + ", Age: " + age;
    }

    // Inner classes for MedicalRecord, Availability, Appointment, Medication, and AppointmentOutcome
    class MedicalRecord {
        private String diagnosis;
        private String prescription;
        private String treatmentPlan;

        // Constructor to initialize the variables
        public MedicalRecord(String diagnosis, String prescription, String treatmentPlan) {
            this.diagnosis = diagnosis;
            this.prescription = prescription;
            this.treatmentPlan = treatmentPlan;
        }

        // Getters and setters
    }

    class Availability {
        private String startTime;
        private String endTime;

        // Constructor to initialize the variables
        public Availability(String startTime, String endTime) {
            this.startTime = startTime;
            this.endTime = endTime;
        }

        // Getters and setters
    }

    class Appointment {
        private String id;
        private String status;
        private AppointmentOutcome outcome;

        // Constructor to initialize the variables
        public Appointment(String id) {
            this.id = id;
            this.status = "pending";
        }

        // Getter and setter methods for id
        public String getId() {
            return id;
        }

        // Getter and setter methods for status
        public void setStatus(String status) {
            this.status = status;
        }

        public String getStatus() {
            return status;
        }

        // Getter and setter methods for outcome
        public void setOutcome(AppointmentOutcome outcome) {
            this.outcome = outcome;
        }

        // Getters and setters
    }

    class Medication {
        private String name;
        private String status;

        // Constructor to initialize the variables
        public Medication(String name) {
            this.name = name;
            this.status = "pending";
        }

        // Getters and setters
    }

    class AppointmentOutcome {
        private String date;
        private String serviceType;
        private List<Medication> medications;
        private String notes;

        // Constructor to initialize the variables
        public AppointmentOutcome(String date, String serviceType, List<Medication> medications, String notes) {
            this.date = date;
            this.serviceType = serviceType;
            this.medications = medications;
            this.notes = notes;
        }

        // Getters and setters
    }
}