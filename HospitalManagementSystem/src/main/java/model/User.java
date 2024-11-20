/**
 * The User class represents a generic user in the hospital management system.
 * It contains shared attributes such as login information, gender, age, and role,
 * which are common across all types of users (e.g., staff, patients).
 * 
 * <p>This class provides getter and setter methods to manage user information.</p>
 * 
 * @author Lee Jia Qian Valerie
 * @version 1.0
 * @since 2024-11-15
 */
package model;

public class User {

    /** The hospital ID used for login. */
    private String hospitalId;

    /** The password used for login. */
    private String password;

    /** The gender of the user. */
    private String gender;

    /** The age of the user. */
    private int age;

    /** The role of the user (e.g., Administrator, Doctor, Patient, Pharmacist). */
    private String role;

    /**
     * Gets the gender of the user.
     *
     * @return the user's gender.
     */
    public String getGender() {
        return gender;
    }

    /**
     * Sets the gender of the user.
     *
     * @param gender the user's gender to set.
     */
    public void setGender(String gender) {
        this.gender = gender;
    }

    /**
     * Gets the password of the user.
     *
     * @return the user's password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password of the user.
     *
     * @param password the user's password to set.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Gets the hospital ID of the user.
     *
     * @return the user's hospital ID.
     */
    public String getHospitalId() {
        return hospitalId;
    }

    /**
     * Sets the hospital ID of the user.
     *
     * @param hospitalId the user's hospital ID to set.
     */
    public void setHospitalId(String hospitalId) {
        this.hospitalId = hospitalId;
    }

    /**
     * Gets the age of the user.
     *
     * @return the user's age.
     */
    public int getAge() {
        return age;
    }

    /**
     * Sets the age of the user.
     *
     * @param age the user's age to set.
     */
    public void setAge(int age) {
        this.age = age;
    }

    /**
     * Gets the role of the user.
     *
     * @return the user's role.
     */
    public String getRole() {
        return role;
    }

    /**
     * Sets the role of the user.
     *
     * @param role the user's role to set.
     */
    public void setRole(String role) {
        this.role = role;
    }
}
