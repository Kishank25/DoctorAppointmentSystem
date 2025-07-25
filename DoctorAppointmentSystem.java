// Enhanced GUI Version - Doctor Appointment Booking System using Java Swing

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

class Doctor {
    String id;
    String name;
    List<String> availableSlots;

    Doctor(String id, String name, List<String> slots) {
        this.id = id;
        this.name = name;
        this.availableSlots = new ArrayList<>(slots);
    }

    public String toString() {
        return name + " (" + id + ")";
    }
}

class Appointment {
    String patientName;
    String patientId;
    Doctor doctor;
    String timeSlot;

    Appointment(String patientId, String patientName, Doctor doctor, String timeSlot) {
        this.patientId = patientId;
        this.patientName = patientName;
        this.doctor = doctor;
        this.timeSlot = timeSlot;
    }

    public String toString() {
        return patientName + " booked with " + doctor.name + " at " + timeSlot;
    }
}

public class DoctorAppointmentSystem extends JFrame {
    private JComboBox<Doctor> doctorDropdown;
    private JComboBox<String> slotDropdown;
    private JTextField patientIdField, patientNameField;
    private JTextArea appointmentDisplay;

    private List<Doctor> doctors = new ArrayList<>();
    private List<Appointment> appointments = new ArrayList<>();

    public DoctorAppointmentSystem() {
        setTitle("Doctor Appointment Booking System");
        setSize(700, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        seedDoctors();
        initUI();
    }

    private void initUI() {
        JPanel inputPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY), "Book Appointment", 0, 0, new Font("Arial", Font.BOLD, 14)));

        JLabel idLabel = new JLabel("Patient ID:");
        patientIdField = new JTextField();
        JLabel nameLabel = new JLabel("Patient Name:");
        patientNameField = new JTextField();

        JLabel doctorLabel = new JLabel("Select Doctor:");
        doctorDropdown = new JComboBox<>(doctors.toArray(new Doctor[0]));
        JLabel slotLabel = new JLabel("Available Slots:");
        slotDropdown = new JComboBox<>();

        updateSlotDropdown();

        JButton bookButton = new JButton("Book Appointment");
        bookButton.setBackground(new Color(34, 139, 34));
        bookButton.setForeground(Color.WHITE);
        bookButton.setFocusPainted(false);
        bookButton.addActionListener(e -> bookAppointment());

        JButton cancelButton = new JButton("Cancel Appointment");
        cancelButton.setBackground(new Color(178, 34, 34));
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setFocusPainted(false);
        cancelButton.addActionListener(e -> cancelAppointment());

        doctorDropdown.addActionListener(e -> updateSlotDropdown());

        inputPanel.add(idLabel); inputPanel.add(patientIdField);
        inputPanel.add(nameLabel); inputPanel.add(patientNameField);
        inputPanel.add(doctorLabel); inputPanel.add(doctorDropdown);
        inputPanel.add(slotLabel); inputPanel.add(slotDropdown);
        inputPanel.add(bookButton); inputPanel.add(cancelButton);

        JPanel displayPanel = new JPanel(new BorderLayout());
        displayPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY), "Appointments", 0, 0, new Font("Arial", Font.BOLD, 14)));
        appointmentDisplay = new JTextArea();
        appointmentDisplay.setEditable(false);
        appointmentDisplay.setFont(new Font("Monospaced", Font.PLAIN, 13));
        appointmentDisplay.setBackground(new Color(245, 245, 245));
        displayPanel.add(new JScrollPane(appointmentDisplay), BorderLayout.CENTER);

        add(inputPanel, BorderLayout.NORTH);
        add(displayPanel, BorderLayout.CENTER);
        refreshAppointments();
    }

    private void seedDoctors() {
        doctors.add(new Doctor("D101", "Dr. Mehta", Arrays.asList("10AM", "11AM", "12PM")));
        doctors.add(new Doctor("D102", "Dr. Sharma", Arrays.asList("2PM", "3PM", "4PM")));
        doctors.add(new Doctor("D103", "Dr. Verma", Arrays.asList("9AM", "1PM", "5PM")));
    }

    private void updateSlotDropdown() {
        Doctor selected = (Doctor) doctorDropdown.getSelectedItem();
        slotDropdown.removeAllItems();
        if (selected != null) {
            for (String slot : selected.availableSlots) {
                slotDropdown.addItem(slot);
            }
        }
    }

    private void bookAppointment() {
        String pid = patientIdField.getText().trim();
        String pname = patientNameField.getText().trim();
        Doctor doctor = (Doctor) doctorDropdown.getSelectedItem();
        String slot = (String) slotDropdown.getSelectedItem();

        if (pid.isEmpty() || pname.isEmpty() || doctor == null || slot == null) {
            JOptionPane.showMessageDialog(this, "Please fill all fields and select a valid slot.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        doctor.availableSlots.remove(slot);
        appointments.add(new Appointment(pid, pname, doctor, slot));
        JOptionPane.showMessageDialog(this, "Appointment Booked Successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);

        patientIdField.setText("");
        patientNameField.setText("");
        updateSlotDropdown();
        refreshAppointments();
    }

    private void cancelAppointment() {
        String pid = JOptionPane.showInputDialog(this, "Enter Patient ID to cancel appointment:");
        if (pid == null || pid.trim().isEmpty()) return;

        boolean found = false;
        Iterator<Appointment> it = appointments.iterator();
        while (it.hasNext()) {
            Appointment a = it.next();
            if (a.patientId.equalsIgnoreCase(pid.trim())) {
                a.doctor.availableSlots.add(a.timeSlot);
                it.remove();
                found = true;
                JOptionPane.showMessageDialog(this, "Appointment Cancelled Successfully.", "Cancelled", JOptionPane.INFORMATION_MESSAGE);
                break;
            }
        }

        if (!found) {
            JOptionPane.showMessageDialog(this, "No appointment found for the given Patient ID.", "Not Found", JOptionPane.WARNING_MESSAGE);
        }
        updateSlotDropdown();
        refreshAppointments();
    }

    private void refreshAppointments() {
        StringBuilder sb = new StringBuilder();
        for (Appointment a : appointments) {
            sb.append(a).append("\n");
        }
        appointmentDisplay.setText(sb.toString());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new DoctorAppointmentSystem().setVisible(true));
    }
}
