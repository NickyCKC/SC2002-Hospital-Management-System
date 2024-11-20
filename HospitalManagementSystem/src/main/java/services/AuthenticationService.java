package services;

import model.User;
import repository.AccountRepository;


/**
 * The AuthenticationService class handles user authentication operations.
 * It interacts with the {@link AccountRepository} to validate user credentials and retrieve user details.
 * 
 * @author Lee Jia Qian Valerie
 * @version 1.0
 * @since 2024-11-15
 */

public class AuthenticationService {

    /** Repository for managing account-related data. */
    private final AccountRepository accountRepository = new AccountRepository();

    /**
     * Authenticates a user by validating their hospital ID and password.
     *
     * @param hospitalId the user's hospital ID.
     * @param password   the user's password.
     * @return a {@link User} object if authentication is successful, or null if the credentials are invalid.
     * @throws Exception if an error occurs during authentication.
     */
    public User login(String hospitalId, String password) throws Exception {
        return accountRepository.login(hospitalId, password);
    }
}
