////package project;
import java.util.*;
//import project.MedicalRecord;
//import project.Appointment;

//add menu


public class Doctor {
    private String id;
    private String name;
    private String gender;
    private int age;
    private Map<String, MedicalRecord> medicalRecords;
    private List<Availability> schedule;
    private List<Appointment> appointments;

    public Doctor(String id, String name, String gender, int age) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.age = age;
        this.medicalRecords = new HashMap<>();
        this.schedule = new ArrayList<>();
        this.appointments = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public MedicalRecord viewMedicalRecord(String patientId) {
        return medicalRecords.get(patientId);
    }

    public void updateMedicalRecord(String patientId, String diagnosis, String prescription, String treatmentPlan) {
        MedicalRecord record = medicalRecords.get(patientId);
        if (record != null) {
            List<String> pastDiagnoses = record.getPastDiagnoses();
            pastDiagnoses.add(diagnosis);
            record.setPastDiagnoses(pastDiagnoses);

            List<String> pastTreatments = record.getPastTreatments();
            pastTreatments.add(treatmentPlan);
            record.setPastTreatments(pastTreatments);
        } else {
            medicalRecords.put(patientId, new MedicalRecord(patientId, "", "", "", new ArrayList<>(), "", new ArrayList<>(), new ArrayList<>()));
        }
    }

    public List<Availability> viewSchedule() {
        return schedule;
    }

    public void setAvailability(String startTime, String endTime) {
        schedule.add(new Availability(startTime, endTime));
    }

    public String acceptAppointment(String appointmentId) {
        for (Appointment appointment : appointments) {
            if (appointment.getDoctorId().equals(this.id) && appointment.getStatus().equals("pending")) {
                appointment.setStatus("accepted");
                return "Appointment accepted";
            }
        }
        return "Appointment not found";
    }

    public String declineAppointment(String appointmentId) {
        for (Appointment appointment : appointments) {
            if (appointment.getDoctorId().equals(this.id) && appointment.getStatus().equals("pending")) {
                appointment.setStatus("declined");
                return "Appointment declined";
            }
        }
        return "Appointment not found";
    }

    public List<Appointment> viewUpcomingAppointments() {
        List<Appointment> upcomingAppointments = new ArrayList<>();
        for (Appointment appointment : appointments) {
            if (appointment.getDoctorId().equals(this.id) && "accepted".equals(appointment.getStatus())) {
                upcomingAppointments.add(appointment);
            }
        }
        return upcomingAppointments;
    }

    public String recordAppointmentOutcome(String appointmentId, String date, String serviceType, List<Medication> medications, String notes) {
        for (Appointment appointment : appointments) {
            if (appointment.getDoctorId().equals(this.id) && appointment.getStatus().equals("accepted")) {
                String outcome = "Date: " + date + ", Service Type: " + serviceType + ", Medications: " + medications + ", Notes: " + notes;
                appointment.setOutcome(outcome);
                return "Appointment outcome recorded";
            }
        }
        return "Appointment not found";
    }

    @Override
    public String toString() {
        return "Doctor " + name + ", Gender: " + gender + ", Age: " + age;
    }

    // Inner classes for Availability and Medication
    class Availability {
        private String startTime;
        private String endTime;

        public Availability(String startTime, String endTime) {
            this.startTime = startTime;
            this.endTime = endTime;
        }

        // Getters and setters
    }

    class Medication {
        private String name;
        private String status;

        public Medication(String name) {
            this.name = name;
            this.status = "pending";
        }

        // Getters and setters
    }
    // Menu for testing
    public void menu() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\nDoctor Menu:");
            System.out.println("1. Set Availability");
            System.out.println("2. View Schedule");
            System.out.println("3. Update Medical Record");
            System.out.println("4. View Medical Record");
            System.out.println("5. Accept Appointment");
            System.out.println("6. Decline Appointment");
            System.out.println("7. Record Appointment Outcome");
            System.out.println("8. View Upcoming Appointments");
            System.out.println("9. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    System.out.print("Enter start time: ");
                    String startTime = scanner.nextLine();
                    System.out.print("Enter end time: ");
                    String endTime = scanner.nextLine();
                    setAvailability(startTime, endTime);
                    System.out.println("Availability set.");
                    break;
                case 2:
                    List<Availability> schedule = viewSchedule();
                    System.out.println("Doctor's Schedule:");
                    for (Availability availability : schedule) {
                        System.out.println("Start Time: " + availability.startTime + ", End Time: " + availability.endTime);
                    }
                    break;
                case 3:
                    System.out.print("Enter patient ID: ");
                    String patientId = scanner.nextLine();
                    System.out.print("Enter diagnosis: ");
                    String diagnosis = scanner.nextLine();
                    System.out.print("Enter prescription: ");
                    String prescription = scanner.nextLine();
                    System.out.print("Enter treatment plan: ");
                    String treatmentPlan = scanner.nextLine();
                    updateMedicalRecord(patientId, diagnosis, prescription, treatmentPlan);
                    System.out.println("Medical record updated.");
                    break;
                case 4:
                    System.out.print("Enter patient ID: ");
                    patientId = scanner.nextLine();
                    MedicalRecord record = viewMedicalRecord(patientId);
                    if (record != null) {
                        System.out.println("Medical Record:");
                        System.out.println("Past Diagnoses: " + record.getPastDiagnoses());
                        System.out.println("Past Treatments: " + record.getPastTreatments());
                    } else {
                        System.out.println("No medical record found for patient ID: " + patientId);
                    }
                    break;
                case 5:
                    System.out.print("Enter appointment ID: ");
                    String appointmentId = scanner.nextLine();
                    System.out.println(acceptAppointment(appointmentId));
                    break;
                case 6:
                    System.out.print("Enter appointment ID: ");
                    appointmentId = scanner.nextLine();
                    System.out.println(declineAppointment(appointmentId));
                    break;
                case 7:
                    System.out.print("Enter appointment ID: ");
                    appointmentId = scanner.nextLine();
                    System.out.print("Enter date: ");
                    String date = scanner.nextLine();
                    System.out.print("Enter service type: ");
                    String serviceType = scanner.nextLine();
                    System.out.print("Enter medications (comma separated): ");
                    String meds = scanner.nextLine();
                    List<Medication> medications = new ArrayList<>();
                    for (String med : meds.split(",")) {
                        medications.add(new Medication(med.trim()));
                    }
                    System.out.print("Enter notes: ");
                    String notes = scanner.nextLine();
                    System.out.println(recordAppointmentOutcome(appointmentId, date, serviceType, medications, notes));
                    break;
                case 8:
                    List<Appointment> upcomingAppointments = viewUpcomingAppointments();
                    System.out.println("Upcoming Appointments:");
                    for (Appointment appt : upcomingAppointments) {
                        System.out.println("DateTime: " + appt.getDateTime() + ", Status: " + appt.getStatus());
                    }
                    break;
                case 9:
                    System.out.println("Exiting...");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    public static void main(String[] args) {
        Doctor doctor = new Doctor("doctor1", "Dr. Smith", "Male", 45);
        doctor.menu();
    }
}