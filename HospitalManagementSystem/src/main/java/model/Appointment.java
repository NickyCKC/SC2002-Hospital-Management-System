package model;

/**
 * The Appointment class represents an appointment in the hospital management system.
 * It contains details about the appointment such as the appointment ID, patient ID,
 * doctor ID, appointment time, and the appointment status.
 * 
 * <p>The appointment time is stored as a string in the format "d/M/yyyy h:mm:ss a".</p>
 * 
 * @author Lee Jia Qian Valerie
 * @version 1.0
 * @since 2024-11-15
 */

public class Appointment {

    /** Unique identifier for the appointment. */
    private int appointmentId;

    /** ID of the patient associated with the appointment. */
    private String patientId;

    /** ID of the doctor associated with the appointment. */
    private String doctorId;

    /** Time of the appointment in the format "d/M/yyyy h:mm:ss a". */
    private String appointmentTime;

    /** Current status of the appointment. */
    private String appointmentStatus;

    /**
     * Gets the unique identifier for the appointment.
     *
     * @return the appointment ID.
     */
    public int getAppointmentId() {
        return appointmentId;
    }

    /**
     * Sets the unique identifier for the appointment.
     *
     * @param appointmentId the appointment ID to set.
     */
    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    /**
     * Gets the patient ID associated with the appointment.
     *
     * @return the patient ID.
     */
    public String getPatientId() {
        return patientId;
    }

    /**
     * Sets the patient ID associated with the appointment.
     *
     * @param patientId the patient ID to set.
     */
    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    /**
     * Gets the doctor ID associated with the appointment.
     *
     * @return the doctor ID.
     */
    public String getDoctorId() {
        return doctorId;
    }

    /**
     * Sets the doctor ID associated with the appointment.
     *
     * @param doctorId the doctor ID to set.
     */
    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    /**
     * Gets the time of the appointment.
     *
     * @return the appointment time as a string.
     */
    public String getAppointmentTime() {
        return appointmentTime;
    }

    /**
     * Sets the time of the appointment.
     *
     * @param appointmentTime the appointment time to set.
     */
    public void setAppointmentTime(String appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    /**
     * Gets the current status of the appointment.
     *
     * @return the appointment status.
     */
    public String getAppointmentStatus() {
        return appointmentStatus;
    }

    /**
     * Sets the current status of the appointment.
     *
     * @param appointmentStatus the appointment status to set.
     */
    public void setAppointmentStatus(String appointmentStatus) {
        this.appointmentStatus = appointmentStatus;
    }
}
