/**
 * The Doctor class represents a doctor in the hospital management system.
 * It extends the {@link Staff} class and includes an additional attribute for the doctor ID.
 * 
 * <p>This class provides getter and setter methods for managing the doctor ID.</p>
 * 
 * @author Lee Jia Qian Valerie
 * @version 1.0
 * @since 2024-11-15
 */
package model;

public class Doctor extends Staff {

    /** Unique identifier for the doctor. */
    private String doctorId;

    /**
     * Gets the unique identifier for the doctor.
     *
     * @return the doctor ID.
     */
    public String getDoctorId() {
        return doctorId;
    }

    /**
     * Sets the unique identifier for the doctor.
     *
     * @param doctorId the doctor ID to set.
     */
    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }
}
