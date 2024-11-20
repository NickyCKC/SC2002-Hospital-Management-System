/**
 * The Patient class represents a patient in the hospital management system.
 * It extends the {@link Staff} class and includes additional attributes specific to patients,
 * such as patient ID, name, date of birth, blood type, email, contact number, and medical records.
 * 
 * <p>This class provides getter and setter methods for managing patient information.</p>
 * 
 * @author Lee Jia Qian Valerie
 * @version 1.0
 * @since 2024-11-15
 */
package model;

import java.time.LocalDate;
import java.util.List;

public class Patient extends Staff {

    /** Unique identifier for the patient. */
    private String patientId;

    /** Full name of the patient. */
    private String patientName;

    /** Date of birth of the patient. */
    private LocalDate dateOfBirth;

    /** Blood type of the patient. */
    private String bloodType;

    /** Email address of the patient. */
    private String email;

    /** Contact number of the patient. */
    private String contactNo;

    /** List of medical records associated with the patient. */
    private List<MedicalRecord> medicalRecords;

    /**
     * Gets the unique identifier for the patient.
     *
     * @return the patient ID.
     */
    public String getPatientId() {
        return patientId;
    }

    /**
     * Sets the unique identifier for the patient.
     *
     * @param patientId the patient ID to set.
     */
    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    /**
     * Gets the full name of the patient.
     *
     * @return the patient's full name.
     */
    public String getPatientName() {
        return patientName;
    }

    /**
     * Sets the full name of the patient.
     *
     * @param patientName the patient's full name to set.
     */
    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    /**
     * Gets the date of birth of the patient.
     *
     * @return the patient's date of birth.
     */
    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    /**
     * Sets the date of birth of the patient.
     *
     * @param dateOfBirth the patient's date of birth to set.
     */
    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    /**
     * Gets the blood type of the patient.
     *
     * @return the patient's blood type.
     */
    public String getBloodType() {
        return bloodType;
    }

    /**
     * Sets the blood type of the patient.
     *
     * @param bloodType the patient's blood type to set.
     */
    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
    }

    /**
     * Gets the email address of the patient.
     *
     * @return the patient's email address.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email address of the patient.
     *
     * @param email the patient's email address to set.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the contact number of the patient.
     *
     * @return the patient's contact number.
     */
    public String getContactNo() {
        return contactNo;
    }

    /**
     * Sets the contact number of the patient.
     *
     * @param contactNo the patient's contact number to set.
     */
    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    /**
     * Gets the list of medical records associated with the patient.
     *
     * @return the list of medical records.
     */
    public List<MedicalRecord> getMedicalRecords() {
        return medicalRecords;
    }

    /**
     * Sets the list of medical records associated with the patient.
     *
     * @param medicalRecords the list of medical records to set.
     */
    public void setMedicalRecords(List<MedicalRecord> medicalRecords) {
        this.medicalRecords = medicalRecords;
    }
}
