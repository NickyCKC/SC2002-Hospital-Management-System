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
}