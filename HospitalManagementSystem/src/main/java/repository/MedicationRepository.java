/**
 * The MedicationRepository class manages the storage and retrieval of medication data.
 * It interacts with an Excel file to load, update, and persist the medication inventory.
 * 
 * <p>Key functionalities:</p>
 * <ul>
 *   <li>Loading medications from the inventory file.</li>
 *   <li>Saving changes to the inventory file after updates.</li>
 *   <li>Managing medication operations like adding, updating, and removing medications.</li>
 *   <li>Handling replenish requests and dispensing medications.</li>
 * </ul>
 * 
 * <p>This class ensures that medication data is efficiently managed and synchronized with the underlying file system.</p>
 * 
 * @author NICHOLAS CHANG CHIA KUAN
 * @version 1.0
 * @since 2024-11-15
 */
package repository;

import constants.FilePath;
import model.Medication;
import services.ExcelReaderWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MedicationRepository {

    /** Path to the Excel file containing the medication inventory. */
    private final String medicationFilePath = FilePath.MEDICAL_INVENTORY_FILE_PATH;

    /** Header row from the Excel file. */
    private List<String> medicationFileHeader = new ArrayList<>();

    /** List storing all medications loaded from the inventory file. */
    private List<Medication> medicationList = new ArrayList<>();

    /**
     * Constructor for MedicationRepository. Loads medications from the inventory file.
     *
     * @throws IOException if an error occurs during file reading.
     */
    public MedicationRepository() throws IOException {
        this.medicationList = loadMedications();
    }

    /**
     * Loads medications from the inventory file into memory.
     *
     * @return a list of medications.
     * @throws IOException if an error occurs during file reading.
     */
    private List<Medication> loadMedications() throws IOException {
        List<Medication> medicationList = new ArrayList<>();
        List<List<String>> medicationFile = ExcelReaderWriter.read(medicationFilePath);

        for (int i = 0; i < medicationFile.size(); i++) {
            if (i == 0) {
                medicationFileHeader = medicationFile.get(i);
                continue;
            }

            Medication medication = new Medication();
            medication.setMedicineName(medicationFile.get(i).get(0));
            medication.setCurrentStock((int) Float.parseFloat(medicationFile.get(i).get(1)));
            medication.setLowStockLevel((int) Float.parseFloat(medicationFile.get(i).get(2)));
            medication.setReplenishAmount((int) Float.parseFloat(medicationFile.get(i).get(3)));
            medicationList.add(medication);
        }

        return medicationList;
    }

    /**
     * Saves the current state of the medication list to the inventory file.
     *
     * @throws IOException if an error occurs during file writing.
     */
    private void saveMedications() throws IOException {
        List<List<String>> medicationFile = new ArrayList<>();

        medicationFile.add(medicationFileHeader);
        for (Medication medication : medicationList) {
            List<String> medicationRow = new ArrayList<>();
            medicationRow.add(medication.getMedicineName());
            medicationRow.add(String.valueOf(medication.getCurrentStock()));
            medicationRow.add(String.valueOf(medication.getLowStockLevel()));
            medicationRow.add(String.valueOf(medication.getReplenishAmount()));
            medicationFile.add(medicationRow);
        }

        ExcelReaderWriter.write(medicationFile, medicationFilePath);
    }

    /**
     * Retrieves the list of medications.
     *
     * @return the list of medications.
     */
    public List<Medication> getMedicationList() {
        return medicationList;
    }

    /**
     * Adds a new medication to the inventory and saves changes to the file.
     *
     * @param medication the medication to add.
     * @throws IOException if an error occurs during file writing.
     */
    public void addNewMedication(Medication medication) throws IOException {
        medicationList.add(medication);
        saveMedications();
    }

    /**
     * Updates an existing medication in the inventory and saves changes to the file.
     *
     * @param medicationToUpdate the updated medication details.
     * @throws IOException if an error occurs during file writing.
     */
    public void updateMedication(Medication medicationToUpdate) throws IOException {
        for (Medication medication : medicationList) {
            if (medication.getMedicineName().equals(medicationToUpdate.getMedicineName())) {
                medication = medicationToUpdate;
                break;
            }
        }
        saveMedications();
    }

    /**
     * Removes a medication from the inventory and saves changes to the file.
     *
     * @param medicationName the name of the medication to remove.
     * @throws IOException if an error occurs during file writing.
     */
    public void removeMedication(String medicationName) throws IOException {
        for (Medication medication : medicationList) {
            if (medication.getMedicineName().equals(medicationName)) {
                medicationList.remove(medication);
                break;
            }
        }
        saveMedications();
    }

    /**
     * Approves a replenishment request for a medication by updating its stock level.
     *
     * @param m the medication to replenish.
     * @throws IOException if an error occurs during file writing.
     */
    public void approveReplenishRequest(Medication m) throws IOException {
        for (Medication medication : medicationList) {
            if (medication.getMedicineName().equals(m.getMedicineName())) {
                medication.setCurrentStock(medication.getCurrentStock() + medication.getReplenishAmount());
                medication.setReplenishAmount(0);
                break;
            }
        }
        saveMedications();
    }

    /**
     * Submits a replenishment request for a medication.
     *
     * @param m                 the medication to request replenishment for.
     * @param amountToReplenish the amount to replenish.
     * @throws IOException if an error occurs during file writing.
     */
    public void submitReplenishRequest(Medication m, int amountToReplenish) throws IOException {
        for (Medication medication : medicationList) {
            if (medication.getMedicineName().equals(m.getMedicineName())) {
                medication.setReplenishAmount(amountToReplenish);
                break;
            }
        }
        saveMedications();
    }

    /**
     * Dispenses a specified amount of a medication, updating the stock level.
     *
     * @param medicationName   the name of the medication to dispense.
     * @param amountToDispense the amount to dispense.
     * @return {@code true} if the medication was successfully dispensed, {@code false} otherwise.
     * @throws IOException if an error occurs during file writing.
     */
    public boolean dispenseMedication(String medicationName, int amountToDispense) throws IOException {
        for (Medication medication : medicationList) {
            if (medication.getMedicineName().equals(medicationName)) {
                if (medication.getCurrentStock() < amountToDispense) {
                    return false;
                }

                medication.setCurrentStock(medication.getCurrentStock() - amountToDispense);
                break;
            }
        }
        saveMedications();
        return true;
    }
}
