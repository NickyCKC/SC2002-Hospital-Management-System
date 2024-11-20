/**
 * The FilePath class contains constants representing file paths used in the application.
 * These paths point to various resources required by the Hospital Management System.
 * 
 * <p>This class is designed to be non-instantiable and serves only as a holder for constant values.</p>
 * 
 * @author Lee Jia Qian Valerie
 * @version 1.0
 * @since 2024-11-15
 */

package constants;

public final class FilePath {
	/**
     * Private constructor to prevent instantiation of this utility class.
     */
	private FilePath() { } // No need to instantiate the class, we can hide its constructor

	// Update to file path of people using
	
	/** Path to the Accounts Excel file. */
	public static final String ACCOUNT_FILE_PATH = "C:\\NTU\\Y2S1\\SC2002\\HospitalManagementSystem_Final\\HospitalManagementSystem\\src\\main\\resources\\Accounts.xlsx";
	
	/** Path to the Staff Information Excel file. */
	public static final String STAFF_INFO_FILE_PATH = "C:\\NTU\\Y2S1\\SC2002\\HospitalManagementSystem_Final\\HospitalManagementSystem\\src\\main\\resources\\Staff_Info.xlsx";
	
	/** Path to the Patient Information Excel file. */
	public static final String PATIENT_INFO_FILE_PATH = "C:\\NTU\\Y2S1\\SC2002\\HospitalManagementSystem_Final\\HospitalManagementSystem\\src\\main\\resources\\Patient_Info.xlsx";
	
	/** Path to the Medical Records Excel file. */
	public static final String MEDICAL_RECORDS_FILE_PATH = "C:\\NTU\\Y2S1\\SC2002\\HospitalManagementSystem_Final\\HospitalManagementSystem\\src\\main\\resources\\Medical_Records.xlsx";
	
	/** Path to the Appointments Excel file. */
	public static final String APPOINTMENTS_FILE_PATH = "C:\\NTU\\Y2S1\\SC2002\\HospitalManagementSystem_Final\\HospitalManagementSystem\\src\\main\\resources\\Appointments.xlsx";
	
	/** Path to the Medical Inventory Excel file. */
	public static final String MEDICAL_INVENTORY_FILE_PATH = "C:\\NTU\\Y2S1\\SC2002\\HospitalManagementSystem_Final\\HospitalManagementSystem\\src\\main\\resources\\Medicine_List.xlsx";

}