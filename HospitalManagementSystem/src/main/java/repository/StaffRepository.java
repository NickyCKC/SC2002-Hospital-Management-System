package repository;

import constants.FilePath;
import enums.Role;
import model.Doctor;
import model.Staff;
import services.ExcelReaderWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * The {@code StaffRepository} class provides methods to manage staff records, including reading, saving, updating,
 * and deleting staff data. Staff records are loaded from and saved to an Excel file. It handles both generic staff 
 * and doctor-specific staff (extending from the {@code Staff} class).
 * <p>
 * This repository class allows operations such as adding a new staff, deleting a staff by hospital ID, and retrieving 
 * lists of all staff or only doctors.
 * </p>
 * @since 2024-11-15
 * @version 1.0
 * @author CHARMAINE
 * 
 * @see Staff
 * @see Doctor
 * @see ExcelReaderWriter
 */
public class StaffRepository {
    private final String staffInfoFilePath = FilePath.STAFF_INFO_FILE_PATH;
    private List<String> staffFileHeader = new ArrayList<>();

    private final AccountRepository accountRepository = new AccountRepository();

    // Stores all the Staffs, don't need to keep loading, but save after any changes.
    private List<Staff> staffList = new ArrayList<>();

    /**
     * Constructs a new {@code StaffRepository} object, loading staff data from the Excel file into memory.
     *
     * @throws IOException if an error occurs while reading the staff data from the file.
     */
    public StaffRepository() throws IOException {
        this.staffList = loadStaffs();
    }

    /**
     * Loads the roles of staff members (doctor or other) and updates the list of staff with the respective roles.
     * 
     * @param staffList The list of staff members to update with roles.
     * @return A list of {@link Staff} objects updated with roles.
     * @throws IOException If an error occurs while loading the staff roles.
     */
    private List<Staff> loadStaffRoles(List<Staff> staffList) throws IOException {
        return accountRepository.loadStaffRoles(staffList);
    }

    /**
     * Loads the staff records from the Excel file, creating either a {@link Doctor} or a generic {@link Staff} object 
     * depending on the available data.
     * 
     * @return A list of {@link Staff} objects.
     * @throws IOException If an error occurs while reading the staff data from the file.
     */
    private List<Staff> loadStaffs() throws IOException {
        List<Staff> staffList = new ArrayList<>();
        List<List<String>> staffFile = ExcelReaderWriter.read(staffInfoFilePath);

        for (int i = 0; i < staffFile.size(); i++) {
            if (i == 0) {
                staffFileHeader = staffFile.get(i);
                continue;
            }

            String hospitalId = staffFile.get(i).get(0);
            String gender = staffFile.get(i).get(1);
            int age = (int) Float.parseFloat(staffFile.get(i).get(2));

            if (staffFile.get(i).size() > 3) {
                // Create a Doctor object if doctor-specific data is present
                Doctor doctor = new Doctor();
                doctor.setHospitalId(hospitalId);
                doctor.setGender(gender);
                doctor.setAge(age);
                doctor.setDoctorId(staffFile.get(i).get(3)); // Assuming field (3) is the specialization
                staffList.add(doctor); // Add Doctor to the list
            } else {
                // Create a generic Staff object
                Staff staff = new Staff();
                staff.setHospitalId(hospitalId);
                staff.setGender(gender);
                staff.setAge(age);
                staffList.add(staff); // Add Staff to the list
            }
        }

        staffList = loadStaffRoles(staffList);

        return staffList;
    }

    /**
     * Saves the current list of staff to the Excel file.
     * 
     * @throws IOException If an error occurs while writing the staff data to the file.
     */
    private void saveStaffs() throws IOException {
        List<List<String>> staffFile = new ArrayList<>();

        staffFile.add(staffFileHeader);
        for (Staff staff : staffList) {
            List<String> staffRow = new ArrayList<>();
            staffRow.add(staff.getHospitalId());
            staffRow.add(staff.getGender());
            staffRow.add(String.valueOf(staff.getAge()));
            if (staff instanceof Doctor) {
                Doctor doctor = (Doctor) staff;
                staffRow.add(doctor.getDoctorId());
            }
            staffFile.add(staffRow);
        }

        ExcelReaderWriter.write(staffFile, staffInfoFilePath);
    }

    /**
     * Saves a new staff member to the repository and updates the staff Excel file.
     * 
     * @param staffInfo The {@link Staff} object containing the information to be saved.
     * @throws IOException If an error occurs while writing the new staff data to the file.
     */
    public void saveNewStaff(Staff staffInfo) throws IOException {
        List<String> staffDetailsLine = new ArrayList<>();

        // Format is hospitalId, gender, age, doctorID (If role is Doctor, generate doctorID)
        staffDetailsLine.add(staffInfo.getHospitalId());
        staffDetailsLine.add(staffInfo.getGender().toUpperCase());
        staffDetailsLine.add(String.valueOf(staffInfo.getAge()));
        if (staffInfo.getRole().toUpperCase().equals(Role.DOCTOR.getDisplayValue())) {
            staffDetailsLine.add("D100" + (staffList.size() + 1));
        }

        // Get file details
        List<List<String>> staffInfoFile = ExcelReaderWriter.read(FilePath.STAFF_INFO_FILE_PATH);
        staffInfoFile.add(staffDetailsLine);

        ExcelReaderWriter.write(staffInfoFile, FilePath.STAFF_INFO_FILE_PATH);

        staffList.add(staffInfo);
    }

    /**
     * Deletes a staff member from the repository based on their hospital ID.
     * 
     * @param hospitalId The hospital ID of the staff member to delete.
     * @throws IOException If an error occurs while deleting the staff or updating the file.
     */
    public void deleteStaff(String hospitalId) throws IOException {
        for (Staff staff : staffList) {
            if (staff.getHospitalId().equals(hospitalId)) {
                staffList.remove(staff);
                break;
            }
        }

        saveStaffs();
    }

    /**
     * Returns the list of all staff members.
     * 
     * @return A list of all {@link Staff} objects in the repository.
     */
    public List<Staff> getAllStaff() {
        return staffList;
    }

    /**
     * Updates the information of an existing staff member.
     * 
     * @param newStaffInfo The {@link Staff} object containing the updated staff information.
     * @throws IOException If an error occurs while updating the staff data and saving it.
     */
    public void updateStaff(Staff newStaffInfo) throws IOException {
        for (Staff staff : staffList) {
            if (staff.getHospitalId().equals(newStaffInfo.getHospitalId())) {
                staff = newStaffInfo;
                break;
            }
        }
        saveStaffs();
    }

    /**
     * Retrieves a {@link Doctor} by their hospital ID.
     * 
     * @param hospitalId The hospital ID of the doctor to retrieve.
     * @return The {@link Doctor} object with the specified hospital ID, or {@code null} if not found.
     * @throws IOException If an error occurs while retrieving the doctor information.
     */
    public Doctor getDoctorByHospitalId(String hospitalId) throws IOException {
        for (Staff staff : staffList) {
            if (staff.getHospitalId().equals(hospitalId)) {
                return (Doctor) staff;
            }
        }

        return null;
    }

    /**
     * Returns a list of all doctor IDs from the staff repository.
     * 
     * @return A list of doctor IDs.
     */
    public List<String> getAllDoctors() {
        List<String> doctorList = new ArrayList<>();
        for (Staff staff : staffList) {
            if (staff instanceof Doctor) {
                doctorList.add(((Doctor) staff).getDoctorId());
            }
        }

        return doctorList;
    }
}
