package repository;

import constants.FilePath;
import model.Doctor;
import model.Patient;
import services.ExcelReaderWriter;
import utils.DateUtils;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * The {@code PatientRepository} class manages the operations related to storing, updating, retrieving, and saving patient records.
 * It loads and saves patient data from an Excel file and provides functionality for retrieving patient details, 
 * updating patient information, and retrieving patients under a specific doctor.
 * <p>
 * This class relies on {@link ExcelReaderWriter} for reading and writing patient data from/to an Excel file.
 * It also uses {@link MedicalRecordRepository} to manage medical records and {@link AppointmentRepository} to retrieve 
 * the list of patients under a specific doctor.
 * </p>
 * 
 * @author ARIEL KWOK DAI HUI
 * @version 1.0
 * @since 2024-11-15
 *
 * @see Patient
 * @see Doctor
 * @see MedicalRecordRepository
 * @see AppointmentRepository
 * @see ExcelReaderWriter
 */
public class PatientRepository {

    private final String patientInfoFilePath = FilePath.PATIENT_INFO_FILE_PATH;
    private List<String> patientInfoHeader = new ArrayList<>();

    private final MedicalRecordRepository medicalRecordRepository = new MedicalRecordRepository();
    private final AppointmentRepository appointmentRepository = new AppointmentRepository();

    private List<Patient> patientList = new ArrayList<>();

    /**
     * Constructs a new {@code PatientRepository} object, loading patient data from the Excel file into memory.
     *
     * @throws IOException If an error occurs while reading the patient data from the file.
     */
    public PatientRepository() throws IOException {
        patientList = loadPatientList();
    }

    /**
     * Loads patient data from the Excel file and returns a list of {@link Patient} objects.
     * This method parses patient information such as hospital ID, patient ID, name, gender, date of birth, blood type,
     * email, and contact number. It also loads the medical records for each patient using the {@link MedicalRecordRepository}.
     *
     * @return A list of {@link Patient} objects containing the patient data from the Excel file.
     * @throws IOException If an error occurs while reading the patient data from the file.
     */
    public List<Patient> loadPatientList() throws IOException {
        List<List<String>> patientInfoFile = ExcelReaderWriter.read(patientInfoFilePath);
        List<Patient> patientList = new ArrayList<>();

        for (int i = 0; i < patientInfoFile.size(); i++) {
            if (i == 0) {
                patientInfoHeader = patientInfoFile.get(i);
                continue;
            }
            Patient patientInfo = new Patient();
            patientInfo.setHospitalId(patientInfoFile.get(i).get(0));
            patientInfo.setPatientId(patientInfoFile.get(i).get(1));
            patientInfo.setPatientName(patientInfoFile.get(i).get(2));
            patientInfo.setGender(patientInfoFile.get(i).get(3));

            // Define a DateTimeFormatter for the "dd/MM/yyyy" format
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d-MMM-yyyy");

            // Parse the date string to LocalDate
            LocalDate dateOfBirth = LocalDate.parse(patientInfoFile.get(i).get(4), DateUtils.getDateFormatter());
            patientInfo.setDateOfBirth(dateOfBirth);
            patientInfo.setBloodType(patientInfoFile.get(i).get(5));
            patientInfo.setEmail(patientInfoFile.get(i).get(6));
            patientInfo.setContactNo(patientInfoFile.get(i).get(7));

            // Load medical records for the patient using the medicalRecordRepository
            patientInfo.setMedicalRecords(medicalRecordRepository.getMedicalRecordsByPatientId(patientInfo.getPatientId()));

            // Add the patient to the list
            patientList.add(patientInfo);
        }
        return patientList;
    }

    /**
     * Saves the current list of patients to the Excel file.
     * This method updates the patient records in the file, including hospital ID, patient ID, name, gender, date of birth, 
     * blood type, email, and contact number. The medical records are not saved here as they are managed separately.
     *
     * @throws IOException If an error occurs while writing the patient data to the file.
     */
    private void savePatients() throws IOException {
        List<List<String>> patientFile = new ArrayList<>();

        patientFile.add(patientInfoHeader);
        for (Patient patient : patientList) {
            List<String> patientRow = new ArrayList<>();
            patientRow.add(patient.getHospitalId());
            patientRow.add(patient.getPatientId());
            patientRow.add(patient.getPatientName());
            patientRow.add(patient.getGender());
            patientRow.add(patient.getDateOfBirth().format(DateUtils.getDateFormatter()));
            patientRow.add(patient.getBloodType());
            patientRow.add(patient.getEmail());
            patientRow.add(patient.getContactNo());
            patientFile.add(patientRow);
        }

        ExcelReaderWriter.write(patientFile, patientInfoFilePath);
    }

    /**
     * Loads patient information by their hospital ID from the patient list.
     *
     * @param hospitalId The hospital ID of the patient to retrieve.
     * @return The {@link Patient} object with the specified hospital ID, or {@code null} if the patient is not found.
     * @throws IOException If an error occurs while loading the patient data.
     */
    public Patient loadPatientInfo(String hospitalId) throws IOException {
        for (Patient patient : patientList) {
            if (patient.getHospitalId().equals(hospitalId)) {
                return patient;
            }
        }
        return null;
    }

    /**
     * Retrieves a list of patients under a specific doctor based on their appointments.
     * This method first loads the list of patient IDs assigned to the given doctor through the {@link AppointmentRepository},
     * and then returns the list of {@link Patient} objects whose patient IDs match those under the doctor.
     *
     * @param doctor The {@link Doctor} object whose patients are to be retrieved.
     * @return A list of {@link Patient} objects assigned to the given doctor.
     * @throws IOException If an error occurs while loading the patient or appointment data.
     */
    public List<Patient> retrievePatientsUnderDoctor(Doctor doctor) throws IOException {
        patientList = loadPatientList();

        List<Patient> patientUnderDoctor = new ArrayList<>();
        List<String> patientIdsUnderDoctor = appointmentRepository.getPatientListOfDoctor(doctor.getDoctorId());

        for (Patient patient : patientList) {
            if (patientIdsUnderDoctor.contains(patient.getPatientId())) {
                patientUnderDoctor.add(patient);
            }
        }
        return patientUnderDoctor;
    }

    /**
     * Updates the information of an existing patient in the patient list and saves the updated list to the Excel file.
     *
     * @param patient The {@link Patient} object containing the updated patient information.
     * @throws IOException If an error occurs while updating the patient data and saving it.
     */
    public void updatePatientInfo(Patient patient) throws IOException {
        for (Patient patientInfo : patientList) {
            if (patientInfo.getPatientId().equals(patient.getPatientId())) {
                patientInfo = patient;
                break;
            }
        }

        savePatients();
    }
}
