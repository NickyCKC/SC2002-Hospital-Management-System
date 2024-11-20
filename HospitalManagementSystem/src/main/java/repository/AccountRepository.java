package repository;

import constants.FilePath;
import model.Staff;
import model.User;
import security.Encryptor;
import services.ExcelReaderWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * The AccountRepository class manages the storage and retrieval of user account data
 * in the hospital management system. It interacts with an Excel file for account-related
 * operations such as login authentication, password management, and account creation.
 *
 * <p>Key Features:</p>
 * <ul>
 *   <li>Login functionality with password encryption.</li>
 *   <li>Creation of new user accounts with default credentials.</li>
 *   <li>Deletion of user accounts.</li>
 *   <li>Role assignment for staff based on account details.</li>
 *   <li>Password change functionality with validation.</li>
 * </ul>
 *
 * <p>This repository uses the {@link ExcelReaderWriter} service for reading and writing
 * account data from/to the Excel file and {@link Encryptor} for encrypting passwords.</p>
 *
 * @author CHARMAINE LIEW
 * @version 1.0
 * @since 2024-11-15
 */

public class AccountRepository {

    /** Path to the Excel file storing account data. */
    private final String accountFilePath = FilePath.ACCOUNT_FILE_PATH;

    /**
     * Authenticates a user by verifying their hospital ID and password.
     *
     * @param hospitalId the user's hospital ID.
     * @param password   the user's password.
     * @return a {@link User} object containing user information if login is successful, otherwise {@code null}.
     * @throws Exception if an error occurs during password encryption or file reading.
     */
    public User login(String hospitalId, String password) throws Exception {
        List<List<String>> accounts = ExcelReaderWriter.read(accountFilePath);

        for (int i = 1; i < accounts.size(); i++) {
            if (accounts.get(i).get(0).equals(hospitalId)) {
                if (Encryptor.encrypt(password).equals(accounts.get(i).get(1))) {
                    User userInfo = new User();
                    userInfo.setHospitalId(accounts.get(i).get(0));
                    userInfo.setRole(accounts.get(i).get(2));

                    if (password.equals("password")) {
                        boolean changedSuccessfully = false;
                        while (!changedSuccessfully) {
                            changedSuccessfully = changePassword(hospitalId);
                        }
                    }
                    return userInfo;
                }
            }
        }
        return null;
    }

    /**
     * Saves a new user account to the Excel file.
     *
     * @param loginDetails a {@link User} object containing the new account details.
     * @throws Exception if an error occurs during file writing or password encryption.
     */
    public void saveNewAccount(User loginDetails) throws Exception {
        List<String> loginDetailsLine = new ArrayList<>();
        loginDetailsLine.add(loginDetails.getHospitalId());
        loginDetailsLine.add(Encryptor.encrypt("password")); // Default Password: "password"
        loginDetailsLine.add(loginDetails.getRole().toUpperCase());

        List<List<String>> accountsFile = ExcelReaderWriter.read(accountFilePath);
        accountsFile.add(loginDetailsLine);

        ExcelReaderWriter.write(accountsFile, FilePath.ACCOUNT_FILE_PATH);
    }

    /**
     * Deletes a user account from the Excel file.
     *
     * @param hospitalId the hospital ID of the account to delete.
     * @throws IOException if an error occurs during file reading or writing.
     */
    public void deleteAccount(String hospitalId) throws IOException {
        List<List<String>> accountsFile = ExcelReaderWriter.read(accountFilePath);
        for (int i = 1; i < accountsFile.size(); i++) {
            if (accountsFile.get(i).get(0).equals(hospitalId)) {
                accountsFile.remove(i);
                break;
            }
        }
        ExcelReaderWriter.write(accountsFile, FilePath.ACCOUNT_FILE_PATH);
    }

    /**
     * Loads and assigns roles for a list of staff members based on account details.
     *
     * @param staffList a list of {@link Staff} objects to assign roles to.
     * @return the updated list of staff with assigned roles.
     * @throws IOException if an error occurs during file reading.
     */
    public List<Staff> loadStaffRoles(List<Staff> staffList) throws IOException {
        List<List<String>> accountDetails = ExcelReaderWriter.read(accountFilePath);
        for (Staff staff : staffList) {
            for (int i = 1; i < accountDetails.size(); i++) {
                if (accountDetails.get(i).get(0).equals(staff.getHospitalId())) {
                    staff.setRole(accountDetails.get(i).get(2));
                }
            }
        }
        return staffList;
    }

    /**
     * Prompts the user to change their password.
     *
     * @param hospitalId the hospital ID of the user.
     * @return {@code true} if the password was successfully changed, {@code false} otherwise.
     * @throws Exception if an error occurs during password encryption or file writing.
     */
    private boolean changePassword(String hospitalId) throws Exception {
        System.out.println("Enter your new password:");
        Scanner scanner = new Scanner(System.in);
        String newPassword = scanner.nextLine();
        if (newPassword.isEmpty() || newPassword.equals("password")) {
            System.out.println("Invalid password.");
            return false;
        } else {
            return savePassword(newPassword, hospitalId);
        }
    }

    /**
     * Updates the password for a user in the Excel file.
     *
     * @param newPassword the new password to set.
     * @param hospitalId  the hospital ID of the user.
     * @return {@code true} if the password was successfully updated.
     * @throws Exception if an error occurs during password encryption or file writing.
     */
    private boolean savePassword(String newPassword, String hospitalId) throws Exception {
        String encryptedPassword = Encryptor.encrypt(newPassword);
        List<List<String>> accounts = ExcelReaderWriter.read(FilePath.ACCOUNT_FILE_PATH);

        for (int i = 1; i < accounts.size(); i++) {
            if (accounts.get(i).get(0).equals(hospitalId)) {
                accounts.get(i).set(1, encryptedPassword);
                ExcelReaderWriter.write(accounts, FilePath.ACCOUNT_FILE_PATH);
                break;
            }
        }
        return true;
    }
}
