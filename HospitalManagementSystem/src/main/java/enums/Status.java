/**
 * The Status enum represents the fixed options for appointment statuses.
 * Each status indicates a specific state in the appointment lifecycle.
 * @author Lee Jia Qian Valerie
 * @version 1.0
 * @since 2024-11-15
 */
package enums;

public enum Status {

    /** Represents a free appointment slot created by a doctor. */
    FREE("FREE"),

    /** Represents a pending status when a patient books an appointment. */
    PENDING("PENDING"),

    /** Represents a confirmed status when a doctor accepts an appointment. */
    CONFIRMED("CONFIRMED"),

    /** Represents a cancelled status when a doctor cancels an appointment. */
    CANCELLED("CANCELLED"),

    /** Represents a completed status when a doctor finishes creating the medical record after an appointment. */
    COMPLETED("COMPLETED"),

    /** Represents a dispensed status when a pharmacist dispenses medication. */
    DISPENSED("DISPENSED");

    // Display value for the status.
    private final String displayValue;

    /**
     * Constructs a Status enum with the specified display value.
     *
     * @param displayValue the display value associated with the status.
     */
    Status(String displayValue) {
        this.displayValue = displayValue;
    }

    /**
     * Returns the display value of the status.
     *
     * @return the display value of the status.
     */
    public String getDisplayValue() {
        return displayValue;
    }
}