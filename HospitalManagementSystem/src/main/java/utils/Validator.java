/**
 * The Validator class provides utility methods for validating user inputs.
 * This includes validation for integers, strings, contact numbers, and email addresses.
 * @author Lee Jia Qian Valerie
 * @version 1.0
 * @since 2024-11-15
 */
package utils;

import java.util.Scanner;

public class Validator {

    /**
     * Validates integer input from the user.
     * The method ensures that the input is an integer and prompts the user until a valid input is provided.
     *
     * @param scanner the Scanner object to read user input.
     * @return the validated integer input.
     */
    public static int validateIntegerInput(Scanner scanner) {
        int input;
        while (true) {
            if (scanner.hasNextInt()) {
                input = scanner.nextInt();
                break;  // Input is valid, exit the loop
            } else {
                System.out.println("Invalid input. Try again.");
                scanner.next();  // Clear the invalid input
            }
        }
        return input;
    }

    /**
     * Validates string input from the user.
     * Ensures that the input is non-empty and prompts the user until a valid string is provided.
     *
     * @param scanner the Scanner object to read user input.
     * @return the validated non-empty string input.
     */
    public static String validateStringInput(Scanner scanner) {
        scanner = new Scanner(System.in); // Ensures new scanner instance is used for string input.
        while (true) {
            String input = scanner.nextLine().trim();
            if (!input.isEmpty()) {
                return input;
            } else {
                System.out.println("Invalid input. Please enter some text.");
            }
        }
    }

    /**
     * Validates an 8-digit contact number.
     * Ensures the input consists of exactly 8 digits.
     *
     * @param contactNo the contact number as a string.
     * @return true if the contact number is valid, false otherwise.
     */
    public static boolean validateContactNo(String contactNo) {
        // Define the regex pattern: only digits, exactly 8 characters long
        String regex = "^\\d{8}$";

        // Check if the contact number matches the pattern
        if (!contactNo.matches(regex)) {
            System.out.println("Invalid Phone Number! (8 Digits)");
        }

        return contactNo.matches(regex);
    }

    /**
     * Validates an email address.
     * Ensures the input follows a basic email format using regex.
     *
     * @param email the email address as a string.
     * @return true if the email is valid, false otherwise.
     */
    public static boolean validateEmail(String email) {
        // Regex pattern for a basic email format
        String regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";

        // Check if the email matches the pattern
        if (!email.matches(regex)) {
            System.out.println("Invalid Email");
        }

        return email.matches(regex);
    }
}
