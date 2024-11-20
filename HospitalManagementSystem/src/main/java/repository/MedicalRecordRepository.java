/**
 * The MedicalRecordRepository class manages the storage and retrieval of medical records.
 * It interacts with an Excel file to load, update, and persist medical record data.
 * 
 * <p>Key functionalities:</p>
 * <ul>
 *   <li>Loading medical records from the file into memory.</li>
 *   <li>Saving changes to the medical records back to the file.</li>
 *   <li>Managing operations such as retrieving records by patient ID, inserting new records, and updating statuses.</li>
 * </ul>
 * 
 * <p>This repository also integrates with the {@link AppointmentRepository} to associate appointments with medical records.</p>
 * 
 * @author NICHOLAS CHANG CHIA KUAN
 * @version 1.0
 * @since 2024-11-15
 */
package repository;

import constants.FilePath;
import enums.Status;
import model.Appointment;
import model.MedicalRecord;
import services.ExcelReaderWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MedicalRecordRepository {

    /** Path to the Excel file containing medical records. */
    private final String medicalRecordFilePath = FilePath.MEDICAL_RECORDS_FILE_PATH;

    /** Header row from the Excel file. */
    private List<String> medicalRecordFileHeader = new ArrayList<>();

    /** List storing all medical records loaded from the file. */
    private List<MedicalRecord> medicalRecordList = new ArrayList<>();

    /** Repository for managing appointments. */
    private final AppointmentRepository appointmentRepository = new AppointmentRepository();

    /**
     * Constructor for MedicalRecordRepository. Loads medical records from the file.
     *
     * @throws IOException if an error occurs during file reading.
     */
    public MedicalRecordRepository() throws IOException {
        this.medicalRecordList = loadMedicalRecords();
    }

    /**
     * Loads medical records from the file into memory.
     *
     * @return a list of medical records.
     * @throws IOException if an error occurs during file reading.
     */
    private List<MedicalRecord> loadMedicalRecords() throws IOException {
        List<MedicalRecord> medicalRecordList = new ArrayList<>();
        List<List<String>> medicalRecordFile = ExcelReaderWriter.read(medicalRecordFilePath);

        for (int i = 0; i < medicalRecordFile.size(); i++) {
            if (i == 0) {
                medicalRecordFileHeader = medicalRecordFile.get(i);
                continue;
            }

            MedicalRecord medicalRecord = new MedicalRecord();
            medicalRecord.setPatientId(medicalRecordFile.get(i).get(0));
            medicalRecord.setDiagnoses(medicalRecordFile.get(i).get(1));
            medicalRecord.setTreatment(medicalRecordFile.get(i).get(2));
            medicalRecord.setPrescription(medicalRecordFile.get(i).get(3));
            medicalRecord.setPrescriptionAmount((int) Float.parseFloat(medicalRecordFile.get(i).get(4)));
            medicalRecord.setStatus(medicalRecordFile.get(i).get(5));
            medicalRecord.setId((int) Float.parseFloat(medicalRecordFile.get(i).get(6)));
            if (medicalRecordFile.get(i).size() > 7) {
                medicalRecord.setPastAppointment(appointmentRepository.getAppointmentById((int) Float.parseFloat(medicalRecordFile.get(i).get(7))));
            }
            medicalRecordList.add(medicalRecord);
        }

        return medicalRecordList;
    }

    /**
     * Saves the current state of the medical records to the file.
     *
     * @throws IOException if an error occurs during file writing.
     */
    private void saveMedicalRecords() throws IOException {
        List<List<String>> medicalRecordFile = new ArrayList<>();

        medicalRecordFile.add(medicalRecordFileHeader);
        for (MedicalRecord medicalRecord : medicalRecordList) {
            List<String> medicalRecordRow = new ArrayList<>();
            medicalRecordRow.add(medicalRecord.getPatientId());
            medicalRecordRow.add(medicalRecord.getDiagnoses());
            medicalRecordRow.add(medicalRecord.getTreatment());
            medicalRecordRow.add(medicalRecord.getPrescription());
            medicalRecordRow.add(String.valueOf(medicalRecord.getPrescriptionAmount()));
            medicalRecordRow.add(medicalRecord.getStatus());
            medicalRecordRow.add(String.valueOf(medicalRecord.getId()));
            if (medicalRecord.getPastAppointment() != null) {
                medicalRecordRow.add(String.valueOf(medicalRecord.getPastAppointment().getAppointmentId()));
            }
            medicalRecordFile.add(medicalRecordRow);
        }

        ExcelReaderWriter.write(medicalRecordFile, medicalRecordFilePath);
    }

    /**
     * Retrieves medical records associated with a specific patient ID.
     *
     * @param patientId the ID of the patient.
     * @return a list of medical records for the specified patient.
     * @throws IOException if an error occurs during data retrieval.
     */
    public List<MedicalRecord> getMedicalRecordsByPatientId(String patientId) throws IOException {
        medicalRecordList = loadMedicalRecords();
        List<MedicalRecord> patientMedicalRecords = new ArrayList<>();

        for (MedicalRecord medicalRecord : medicalRecordList) {
            if (medicalRecord.getPatientId().equals(patientId)) {
                patientMedicalRecords.add(medicalRecord);
            }
        }

        return patientMedicalRecords;
    }

    /**
     * Retrieves all medical records with a status of "PENDING".
     *
     * @return a list of medical records with a "PENDING" status.
     */
    public List<MedicalRecord> getAllAppointmentOutcomes() {
        List<MedicalRecord> appointmentOutcomes = new ArrayList<>();

        for (MedicalRecord medicalRecord : medicalRecordList) {
            if (medicalRecord.getStatus().equals(Status.PENDING.getDisplayValue())) {
                appointmentOutcomes.add(medicalRecord);
            }
        }

        return appointmentOutcomes;
    }

    /**
     * Updates the status of a medical record to "DISPENSED" and saves the changes.
     *
     * @param mr the medical record to update.
     * @throws IOException if an error occurs during file writing.
     */
    public void dispenseMedication(MedicalRecord mr) throws IOException {
        for (MedicalRecord medicalRecord : medicalRecordList) {
            if (medicalRecord.getId() == mr.getId()) {
                medicalRecord.setStatus(Status.DISPENSED.getDisplayValue());
                break;
            }
        }

        saveMedicalRecords();
    }

    /**
     * Inserts a new medical record into the repository and assigns it a unique ID.
     *
     * @param newMedicalRecord the new medical record to add.
     * @throws IOException if an error occurs during file writing.
     */
    public void insertNewMedicalRecord(MedicalRecord newMedicalRecord) throws IOException {
        newMedicalRecord.setId((medicalRecordList.get(medicalRecordList.size() - 1).getId() + 1)); // Auto ID
        medicalRecordList.add(newMedicalRecord);
        saveMedicalRecords();
    }

    /**
     * Retrieves a medical record associated with a specific appointment ID.
     *
     * @param appointmentId the ID of the appointment.
     * @return the medical record associated with the appointment, or null if not found.
     * @throws IOException if an error occurs during data retrieval.
     */
    public MedicalRecord getMedicalRecordByAppointmentId(int appointmentId) throws IOException {
        for (MedicalRecord medicalRecord : medicalRecordList) {
            if (medicalRecord.getPastAppointment() != null && appointmentId == medicalRecord.getPastAppointment().getAppointmentId()) {
                return medicalRecord;
            }
        }

        return null;
    }
}
