/**
 * The Gender enum represents the gender of an individual.
 * Each gender has a display value for easy representation.
 * @author Lee Jia Qian Valerie
 * @version 1.0
 * @since 2024-11-15
 */

package enums;

public enum Gender {
	
	/** Represents the male gender. */
    MALE("MALE"),
    
    /** Represents the female gender. */
    FEMALE("FEMALE");

	// Display value for the gender.
    private final String displayValue;

    /**
     * Constructs a Gender enum with the specified display value.
     *
     * @param displayValue the display value associated with the gender.
     */    
    
    Gender(String displayValue) {
        this.displayValue = displayValue;
    }
    
    /**
     * Returns the display value of the gender.
     *
     * @return the display value of the gender.
     */

    public String getDisplayValue() {
        return displayValue;
    }
}
