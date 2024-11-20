/**
 * The Role enum represents the fixed roles within the system.
 * Each role has a display value for easy representation.
 * @author Lee Jia Qian Valerie
 * @version 1.0
 * @since 2024-11-15
 */
package enums;

public enum Role { // fixed options for roles which are patient, doctor, pharmacist and administrator
	
	/** Represents the role of a patient. */
	PATIENT("PATIENT"),
	
	/** Represents the role of a doctor. */
    DOCTOR("DOCTOR"),
    
    /** Represents the role of a pharmacist. */
    PHARMACIST("PHARMACIST"),
    
    /** Represents the role of an administrator. */
    ADMINISTRATOR("ADMINISTRATOR"),;

	// Display value for the role.
    private final String displayValue;
    
    /**
     * Constructs a Role enum with the specified display value.
     *
     * @param displayValue the display value associated with the role.
     */
    Role (String displayValue) {
        this.displayValue = displayValue;
    }

    
    /**
     * Returns the display value of the role.
     *
     * @return the display value of the role.
     */
    public String getDisplayValue() {
        return displayValue;
    }
}
