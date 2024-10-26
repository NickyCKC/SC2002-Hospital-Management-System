//package project;

import java.util.List;

public class MedicalRecord {
    private String patientId;
    private String name;
    private String dateOfBirth;
    private String gender;
    private List<String> contactInfo;
    private String bloodType;
    private List<String> pastDiagnoses;
    private List<String> pastTreatments;

    public MedicalRecord(String patientId, String name, String dateOfBirth, String gender, List<String> contactInfo, String bloodType, List<String> pastDiagnoses, List<String> pastTreatments) {
        this.patientId = patientId;
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.contactInfo = contactInfo;
        this.bloodType = bloodType;
        this.pastDiagnoses = pastDiagnoses;
        this.pastTreatments = pastTreatments;
    }

    // Getter and Setter methods
    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public List<String> getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(List<String> contactInfo) {
        this.contactInfo = contactInfo;
    }

    public String getBloodType() {
        return bloodType;
    }

    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
    }

    public List<String> getPastDiagnoses() {
        return pastDiagnoses;
    }

    public void setPastDiagnoses(List<String> pastDiagnoses) {
        this.pastDiagnoses = pastDiagnoses;
    }

    public List<String> getPastTreatments() {
        return pastTreatments;
    }

    public void setPastTreatments(List<String> pastTreatments) {
        this.pastTreatments = pastTreatments;
    }

    public String viewFullMedicalRecord() {
        return "Patient ID: " + patientId + "\nName: " + name + "\nDate of Birth: " + dateOfBirth + "\nGender: " + gender + "\nContact Info: " + contactInfo + "\nBlood Type: " + bloodType + "\nPast Diagnoses: " + pastDiagnoses + "\nPast Treatments: " + pastTreatments;
    }
}