/**
 * The DateUtils class provides utility methods for handling and formatting date and time operations.
 * This class is designed to work with LocalDateTime, LocalDate, and LocalTime using specified patterns.
 * @author Lee Jia Qian Valerie
 * @version 1.0
 * @since 2024-11-20
 */
package utils;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateUtils {

    // Date and time formats used throughout the utility methods.
    private static final String dateTimeFormat = "dd-MMM-yyyy h:mm:ss a";
    private static final String dateFormat = "dd-MMM-yyyy";
    private static final String timeFormat = "h:mm:ss a";

    /**
     * Parses a datetime string into a LocalDateTime object.
     *
     * @param dateTimeString the datetime string to parse, in the format "dd-MMM-yyyy h:mm:ss a".
     * @return the LocalDateTime object, or null if parsing fails.
     */
    public static LocalDateTime extractDateTime(String dateTimeString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateTimeFormat);
        try {
            return LocalDateTime.parse(dateTimeString, formatter);
        } catch (DateTimeParseException e) {
            System.out.println("Error parsing datetime: " + e.getMessage());
            return null;
        }
    }

    /**
     * Parses a datetime string and extracts the time component as a LocalTime object.
     *
     * @param dateTimeString the datetime string to parse, in the format "dd-MMM-yyyy h:mm:ss a".
     * @return the LocalTime object, or null if parsing fails.
     */
    public static LocalTime extractTime(String dateTimeString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateTimeFormat);
        try {
            return LocalDateTime.parse(dateTimeString, formatter).toLocalTime();
        } catch (DateTimeParseException e) {
            System.out.println("Error parsing time: " + e.getMessage());
            return null;
        }
    }

    /**
     * Parses a datetime string and extracts the date component as a LocalDate object.
     *
     * @param dateTimeString the datetime string to parse, in the format "dd-MMM-yyyy h:mm:ss a".
     * @return the LocalDate object, or null if parsing fails.
     */
    public static LocalDate extractDate(String dateTimeString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateTimeFormat);
        try {
            return LocalDateTime.parse(dateTimeString, formatter).toLocalDate();
        } catch (DateTimeParseException e) {
            System.out.println("Error parsing date: " + e.getMessage());
            return null;
        }
    }

    /**
     * Formats a LocalDate object into a string in the format "dd-MMM-yyyy".
     *
     * @param date the LocalDate object to format.
     * @return the formatted date string.
     */
    public static String formatDate(LocalDate date) {
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern(dateFormat);
        return date.format(outputFormatter);
    }

    /**
     * Combines a LocalDate and LocalTime into a formatted string in the format "dd-MMM-yyyy h:mm:ss a".
     *
     * @param date the LocalDate object.
     * @param time the LocalTime object.
     * @return the combined and formatted date-time string.
     */
    public static String joinDateAsString(LocalDate date, LocalTime time) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateTimeFormat);
        return date.atTime(time).format(formatter);
    }

    /**
     * Provides the formatter used for LocalDateTime in the format "dd-MMM-yyyy h:mm:ss a".
     *
     * @return the DateTimeFormatter object for LocalDateTime.
     */
    public static DateTimeFormatter getLocalDateTimeFormatter() {
        return DateTimeFormatter.ofPattern(dateTimeFormat);
    }

    /**
     * Provides the formatter used for LocalDate in the format "dd-MMM-yyyy".
     *
     * @return the DateTimeFormatter object for LocalDate.
     */
    public static DateTimeFormatter getDateFormatter() {
        return DateTimeFormatter.ofPattern(dateFormat);
    }

    /**
     * Checks if the given datetime string represents a date and time in the future.
     *
     * @param dateTime the datetime string to check, in the format "dd-MMM-yyyy h:mm:ss a".
     * @return true if the datetime is in the future, false otherwise.
     */
    public static boolean isUpcoming(String dateTime) {
        LocalDate date = extractDate(dateTime);
        LocalTime time = extractTime(dateTime);

        assert date != null;
        assert time != null;

        return (date.equals(LocalDate.now()) && time.isAfter(LocalTime.now())) || date.isAfter(LocalDate.now());
    }
}
