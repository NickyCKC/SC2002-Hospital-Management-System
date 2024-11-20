/**
 * The MedicalRecord class represents a medical record in the hospital management system.
 * It contains details about a patient's diagnoses, treatments, prescriptions, and associated past appointments.
 * 
 * <p>This class provides getter and setter methods for managing the medical record details.</p>
 * 
 * @author Lee Jia Qian Valerie
 * @version 1.0
 * @since 2024-11-15
 */
package model;

public class MedicalRecord {

    /** Unique identifier for the patient associated with the medical record. */
    private String patientId;

    /** Diagnoses for the patient. */
    private String diagnoses;

    /** Treatment details for the patient. */
    private String treatment;

    /** Prescription details for the patient. */
    private String prescription;

    /** Amount of the prescribed medication. */
    private int prescriptionAmount;

    /** Status of the medical record. */
    private String status;

    /** Unique identifier for the medical record. */
    private int id;

    /** The past appointment associated with this medical record. */
    private Appointment pastAppointment;

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
     * Gets the diagnoses for the patient.
     *
     * @return the diagnoses.
     */
    public String getDiagnoses() {
        return diagnoses;
    }

    /**
     * Sets the diagnoses for the patient.
     *
     * @param diagnoses the diagnoses to set.
     */
    public void setDiagnoses(String diagnoses) {
        this.diagnoses = diagnoses;
    }

    /**
     * Gets the treatment details for the patient.
     *
     * @return the treatment details.
     */
    public String getTreatment() {
        return treatment;
    }

    /**
     * Sets the treatment details for the patient.
     *
     * @param treatment the treatment details to set.
     */
    public void setTreatment(String treatment) {
        this.treatment = treatment;
    }

    /**
     * Gets the prescription details for the patient.
     *
     * @return the prescription details.
     */
    public String getPrescription() {
        return prescription;
    }

    /**
     * Sets the prescription details for the patient.
     *
     * @param prescription the prescription details to set.
     */
    public void setPrescription(String prescription) {
        this.prescription = prescription;
    }

    /**
     * Gets the status of the medical record.
     *
     * @return the status.
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets the status of the medical record.
     *
     * @param status the status to set.
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Gets the amount of the prescribed medication.
     *
     * @return the prescription amount.
     */
    public int getPrescriptionAmount() {
        return prescriptionAmount;
    }

    /**
     * Sets the amount of the prescribed medication.
     *
     * @param prescriptionAmount the prescription amount to set.
     */
    public void setPrescriptionAmount(int prescriptionAmount) {
        this.prescriptionAmount = prescriptionAmount;
    }

    /**
     * Gets the unique identifier for the medical record.
     *
     * @return the record ID.
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the unique identifier for the medical record.
     *
     * @param id the record ID to set.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the past appointment associated with this medical record.
     *
     * @return the past appointment.
     */
    public Appointment getPastAppointment() {
        return pastAppointment;
    }

    /**
     * Sets the past appointment associated with this medical record.
     *
     * @param pastAppointment the past appointment to set.
     */
    public void setPastAppointment(Appointment pastAppointment) {
        this.pastAppointment = pastAppointment;
    }
}
