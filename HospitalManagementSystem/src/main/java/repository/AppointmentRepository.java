package repository;

import constants.FilePath;
import enums.Status;
import model.Appointment;
import model.Doctor;
import model.Patient;
import services.ExcelReaderWriter;
import utils.DateUtils;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Repository class for managing appointments in the hospital system.
 * This class provides functionalities to load, save, and manipulate appointment data
 * stored in an Excel file. It supports scheduling, updating, and retrieving
 * appointment details for doctors and patients.
 *
 * @since 2024-11-15
 * @version 1.0
 * @author LI LIYI
 */

public class AppointmentRepository {

    private final String appointmentsFilePath = FilePath.APPOINTMENTS_FILE_PATH;
    private List<String> appointmentsFileHeader = new ArrayList<>();
    private List<Appointment> appointmentList = new ArrayList<>();

    /**
     * Initializes the repository by loading all appointments from the Excel file.
     *
     * @throws IOException if an error occurs while reading the Excel file.
     */
    public AppointmentRepository() throws IOException {
        this.appointmentList = loadAppointments();
    }

    /**
     * Loads all appointments from the Excel file.
     *
     * @return a list of appointments.
     * @throws IOException if an error occurs while reading the Excel file.
     */
    private List<Appointment> loadAppointments() throws IOException {
        List<Appointment> appointmentList = new ArrayList<>();
        List<List<String>> appointmentFile = ExcelReaderWriter.read(appointmentsFilePath);

        for (int i = 0; i < appointmentFile.size(); i++) {
            if (i == 0) {
                appointmentsFileHeader = appointmentFile.get(i);
                continue;
            }

            Appointment appointment = new Appointment();
            appointment.setAppointmentId((int) Float.parseFloat(appointmentFile.get(i).get(0)));
            appointment.setDoctorId(appointmentFile.get(i).get(1));
            appointment.setPatientId(appointmentFile.get(i).get(2));
            appointment.setAppointmentTime(appointmentFile.get(i).get(3));
            appointment.setAppointmentStatus(appointmentFile.get(i).get(4));
            appointmentList.add(appointment);
        }

        return appointmentList;
    }

    /**
     * Retrieves all appointments from the repository.
     *
     * @return a list of all appointments.
     */
    public List<Appointment> getAppointmentList() {
        return appointmentList;
    }

    /**
     * Saves all appointments back to the Excel file.
     *
     * @throws IOException if an error occurs while writing to the Excel file.
     */
    private void saveAppointments() throws IOException {
        List<List<String>> appointmentFile = new ArrayList<>();
        appointmentFile.add(appointmentsFileHeader);

        for (Appointment appointment : appointmentList) {
            List<String> appointmentRow = new ArrayList<>();
            appointmentRow.add(String.valueOf(appointment.getAppointmentId()));
            appointmentRow.add(appointment.getDoctorId());
            appointmentRow.add(appointment.getPatientId());
            appointmentRow.add(appointment.getAppointmentTime());
            appointmentRow.add(appointment.getAppointmentStatus());
            appointmentFile.add(appointmentRow);
        }

        ExcelReaderWriter.write(appointmentFile, appointmentsFilePath);
    }

    /**
     * Retrieves a list of patients associated with a specific doctor.
     *
     * @param doctorId the ID of the doctor.
     * @return a list of patient IDs.
     */
    public List<String> getPatientListOfDoctor(String doctorId) {
        List<String> patients = new ArrayList<>();
        for (Appointment appointment : appointmentList) {
            if (appointment.getDoctorId().equals(doctorId)) {
                patients.add(appointment.getPatientId());
            }
        }

        return patients;
    }

    /**
     * Retrieves the schedule for a specific doctor on a given date.
     *
     * @param doctor the doctor whose schedule is being retrieved.
     * @param dateChoice the date for which the schedule is being retrieved.
     * @return a list of appointments for the specified doctor and date.
     */
    public List<Appointment> getDoctorSchedule(Doctor doctor, LocalDate dateChoice) {
        List<Appointment> doctorSchedule = new ArrayList<>();

        for (Appointment appointment : appointmentList) {
            if (appointment.getDoctorId().equals(doctor.getDoctorId())) {
                if (appointment.getAppointmentStatus().equals(Status.FREE.getDisplayValue()) ||
                    appointment.getAppointmentStatus().equals(Status.PENDING.getDisplayValue()) ||
                    appointment.getAppointmentStatus().equals(Status.CONFIRMED.getDisplayValue())) {

                    LocalDate appointmentDate = DateUtils.extractDate(appointment.getAppointmentTime());
                    LocalTime appointmentTime = DateUtils.extractTime(appointment.getAppointmentTime());

                    assert appointmentDate != null;
                    assert appointmentTime != null;

                    if ((appointmentDate.equals(dateChoice) && appointmentTime.isAfter(LocalTime.now())) || 
                        appointmentDate.isAfter(LocalDate.now())) {
                        doctorSchedule.add(appointment);
                    }
                }
            }
        }

        return doctorSchedule;
    }

    /**
     * Retrieves a list of upcoming appointments for a specific patient.
     *
     * @param patient the patient whose upcoming appointments are being retrieved.
     * @return a list of upcoming appointments for the patient.
     */
    public List<Appointment> getUpcomingAppointmentsForPatient(Patient patient) {
        List<Appointment> upcomingAppointments = new ArrayList<>();

        for (Appointment appointment : appointmentList) {
            if (appointment.getPatientId().equals(patient.getPatientId())) {
                if (DateUtils.isUpcoming(appointment.getAppointmentTime())) {
                    upcomingAppointments.add(appointment);
                }
            }
        }

        return upcomingAppointments;
    }

    /**
     * Retrieves available time slots for a specific doctor on a given date.
     *
     * @param doctorId the ID of the doctor.
     * @param date the date for which time slots are to be retrieved.
     * @return a list of available time slots as LocalTime objects.
     */
    public List<LocalTime> getAvailableTimeSlots(String doctorId, LocalDate date) {
        List<LocalTime> availableTimeSlots = new ArrayList<>();

        for (Appointment appointment : appointmentList) {
            if (appointment.getDoctorId().equals(doctorId)) {
                if (Objects.equals(DateUtils.extractDate(appointment.getAppointmentTime()), date)) {
                    if (appointment.getAppointmentStatus().equals(Status.FREE.getDisplayValue()) ||
                            appointment.getAppointmentStatus().equals(Status.CANCELLED.getDisplayValue())) {
                        availableTimeSlots.add(DateUtils.extractTime(appointment.getAppointmentTime()));
                    }
                }
            }
        }

        return availableTimeSlots;
    }

    /**
     * Books an appointment for a patient with a specific doctor on a given date and time.
     *
     * @param patientId the ID of the patient.
     * @param doctorId the ID of the doctor.
     * @param date the date of the appointment.
     * @param time the time of the appointment.
     * @throws IOException if an error occurs while saving the updated appointment data.
     */
    public void bookAppointment(String patientId, String doctorId, LocalDate date, LocalTime time) throws IOException {
        for (Appointment appointment : appointmentList) {
            if (appointment.getDoctorId().equals(doctorId)) {
                if (Objects.equals(DateUtils.extractDate(appointment.getAppointmentTime()), date) &&
                        Objects.equals(DateUtils.extractTime(appointment.getAppointmentTime()), time)) {
                    appointment.setAppointmentStatus(Status.PENDING.getDisplayValue());
                    appointment.setPatientId(patientId);
                    break;
                }
            }
        }

        saveAppointments();
    }

    /**
     * Frees an appointment slot, marking it as available for scheduling.
     *
     * @param appointmentToFree the appointment to be freed.
     * @throws IOException if an error occurs while saving the updated appointment data.
     */
    public void freeAppointmentSlot(Appointment appointmentToFree) throws IOException {
        for (Appointment appointment : appointmentList) {
            if (appointment.getAppointmentId() == appointmentToFree.getAppointmentId()) {
                appointment.setAppointmentStatus(Status.FREE.getDisplayValue());
                appointment.setPatientId(Status.FREE.getDisplayValue());
                break;
            }
        }
        saveAppointments();
    }

    /**
     * Cancels an appointment, marking it as canceled.
     *
     * @param appointmentToCancel the appointment to be canceled.
     * @throws IOException if an error occurs while saving the updated appointment data.
     */
    public void cancelAppointmentSlot(Appointment appointmentToCancel) throws IOException {
        for (Appointment appointment : appointmentList) {
            if (appointment.getAppointmentId() == appointmentToCancel.getAppointmentId()) {
                appointment.setAppointmentStatus(Status.CANCELLED.getDisplayValue());
                appointment.setPatientId(Status.FREE.getDisplayValue());
                break;
            }
        }
        saveAppointments();
    }

    /**
     * Adds a new time slot to the schedule for a doctor.
     *
     * @param appointment the new appointment slot to be added.
     * @throws IOException if an error occurs while saving the updated appointment data.
     */
    public void addTimeSlotToSchedule(Appointment appointment) throws IOException {
        appointment.setAppointmentId((appointmentList.get(appointmentList.size() - 1).getAppointmentId() + 1)); // Auto-generated ID
        appointmentList.add(appointment);
        saveAppointments();
    }

    /**
     * Removes a time slot from the schedule for a doctor.
     *
     * @param appointment the appointment slot to be removed.
     * @throws IOException if an error occurs while saving the updated appointment data.
     */
    public void removeTimeSlotFromSchedule(Appointment appointment) throws IOException {
        for (Appointment appointmentToRemove : appointmentList) {
            if (appointmentToRemove.getAppointmentId() == appointment.getAppointmentId()) {
                appointmentList.remove(appointmentToRemove);
                break;
            }
        }

        saveAppointments();
    }

    /**
     * Approves a pending appointment, marking it as confirmed.
     *
     * @param appointmentToApprove the appointment to be approved.
     * @throws IOException if an error occurs while saving the updated appointment data.
     */
    public void approveAppointment(Appointment appointmentToApprove) throws IOException {
        for (Appointment appointment : appointmentList) {
            if (appointment.getAppointmentId() == appointmentToApprove.getAppointmentId()) {
                appointment.setAppointmentStatus(Status.CONFIRMED.getDisplayValue());
                break;
            }
        }

        saveAppointments();
    }

    /**
     * Declines a pending appointment, freeing up the time slot.
     *
     * @param appointmentToDecline the appointment to be declined.
     * @throws IOException if an error occurs while saving the updated appointment data.
     */
    public void declineAppointment(Appointment appointmentToDecline) throws IOException {
        for (Appointment appointment : appointmentList) {
            if (appointment.getAppointmentId() == appointmentToDecline.getAppointmentId()) {
                appointment.setPatientId(Status.FREE.getDisplayValue());
                appointment.setAppointmentStatus(Status.FREE.getDisplayValue());
                break;
            }
        }

        saveAppointments();
    }

    /**
     * Retrieves a list of upcoming appointments for a specific doctor.
     *
     * @param doctorId the ID of the doctor.
     * @return a list of upcoming appointments.
     */
    public List<Appointment> getUpcomingAppointments(String doctorId) {
        List<Appointment> upcomingAppointments = new ArrayList<>();

        for (Appointment appointment : appointmentList) {
            if (appointment.getDoctorId().equals(doctorId)) {
                if (Objects.equals(DateUtils.extractDate(appointment.getAppointmentTime()), LocalDate.now())) {
                    if (appointment.getAppointmentStatus().equals(Status.CONFIRMED.getDisplayValue())) {
                        upcomingAppointments.add(appointment);
                    }
                }
            }
        }

        return upcomingAppointments;
    }

    /**
     * Marks an appointment as completed.
     *
     * @param appointmentToComplete the appointment to be marked as completed.
     * @throws IOException if an error occurs while saving the updated appointment data.
     */
    public void completeAppointment(Appointment appointmentToComplete) throws IOException {
        for (Appointment appointment : appointmentList) {
            if (appointment.getAppointmentId() == appointmentToComplete.getAppointmentId()) {
                appointment.setAppointmentStatus(Status.COMPLETED.getDisplayValue());
                break;
            }
        }

        saveAppointments();
    }

    /**
     * Retrieves an appointment by its unique ID.
     *
     * @param appointmentId the ID of the appointment.
     * @return the appointment with the specified ID, or null if not found.
     * @throws IOException if an error occurs while accessing the appointment data.
     */
    public Appointment getAppointmentById(int appointmentId) throws IOException {
        for (Appointment appointment : appointmentList) {
            if (appointment.getAppointmentId() == appointmentId) {
                return appointment;
            }
        }

        return null;
    }

}
