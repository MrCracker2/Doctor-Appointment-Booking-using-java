import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

class Doctor {
    private String name;
    private String specialization;
    private ArrayList<String> availableSlots;

    public Doctor(String name, String specialization, ArrayList<String> availableSlots) {
        this.name = name;
        this.specialization = specialization;
        this.availableSlots = availableSlots;
    }

    public String getName() {
        return name;
    }

    public String getSpecialization() {
        return specialization;
    }

    public ArrayList<String> getAvailableSlots() {
        return availableSlots;
    }

    public boolean bookSlot(String slot) {
        if (availableSlots.contains(slot)) {
            availableSlots.remove(slot);
            return true;
        }
        return false;
    }
}

class Patient {
    private String name;
    private String contact;

    public Patient(String name, String contact) {
        this.name = name;
        this.contact = contact;
    }

    public String getName() {
        return name;
    }

    public String getContact() {
        return contact;
    }
}

class Appointment {
    private Doctor doctor;
    private Patient patient;
    private String slot;

    public Appointment(Doctor doctor, Patient patient, String slot) {
        this.doctor = doctor;
        this.patient = patient;
        this.slot = slot;
    }

    @Override
    public String toString() {
        return "Doctor: " + doctor.getName() + ", Patient: " + patient.getName() + ", Slot: " + slot;
    }
}

public class DoctorAppointmentBookingGUI extends JFrame {
    private ArrayList<Doctor> doctors = new ArrayList<>();
    private ArrayList<Appointment> appointments = new ArrayList<>();

    private JComboBox<String> specializationComboBox;
    private JComboBox<String> slotComboBox;
    private JTextField patientNameField;
    private JTextField contactField;
    private JTextArea appointmentArea;

    public DoctorAppointmentBookingGUI() {
        setTitle("Doctor Appointment Booking System");
        setSize(700, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        initDoctors();
        initComponents();
    }

    private void initDoctors() {
        // Initialize multiple specializations and doctors
        String[] specializations = {"Cardiology", "Dermatology", "Orthopedics", "Pediatrics", "Neurology", 
                                    "Oncology", "Gynecology", "Urology", "Psychiatry", "Ophthalmology"};
        
        // Create doctors with their slots for each specialization
        for (String spec : specializations) {
            ArrayList<String> slots = new ArrayList<>();
            slots.add("9:00 AM");
            slots.add("11:00 AM");
            slots.add("1:00 PM");
            slots.add("3:00 PM");
            slots.add("5:00 PM");
            doctors.add(new Doctor("Dr. " + spec + " Specialist", spec, slots));
        }
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(new Color(240, 248, 255));
        
        JLabel titleLabel = new JLabel("Doctor Appointment Booking System", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 24));
        titleLabel.setForeground(new Color(25, 25, 112));

        JPanel inputPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(25, 25, 112), 2), 
            "Appointment Details", TitledBorder.DEFAULT_JUSTIFICATION, 
            TitledBorder.DEFAULT_POSITION, new Font("Arial", Font.BOLD, 14), 
            new Color(25, 25, 112))
        );
        inputPanel.setBackground(new Color(230, 230, 250));

        inputPanel.add(new JLabel("Specialization:"));
        specializationComboBox = new JComboBox<>(new String[]{
            "Select Specialization", "Cardiology", "Dermatology", "Orthopedics", "Pediatrics", "Neurology", 
            "Oncology", "Gynecology", "Urology", "Psychiatry", "Ophthalmology"
        });
        inputPanel.add(specializationComboBox);

        inputPanel.add(new JLabel("Available Slot:"));
        slotComboBox = new JComboBox<>(new String[]{"Select Slot"});
        inputPanel.add(slotComboBox);

        inputPanel.add(new JLabel("Patient Name:"));
        patientNameField = new JTextField();
        inputPanel.add(patientNameField);

        inputPanel.add(new JLabel("Contact Number:"));
        contactField = new JTextField();
        inputPanel.add(contactField);

        JButton bookButton = new JButton("Book Appointment");
        bookButton.setBackground(new Color(100, 149, 237));
        bookButton.setForeground(Color.WHITE);
        bookButton.setFont(new Font("Arial", Font.BOLD, 14));
        inputPanel.add(bookButton);

        appointmentArea = new JTextArea(10, 30);
        appointmentArea.setEditable(false);
        appointmentArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        appointmentArea.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(25, 25, 112), 1), 
            "Booked Appointments", TitledBorder.LEFT, 
            TitledBorder.TOP, new Font("Arial", Font.BOLD, 14), 
            new Color(25, 25, 112))
        );
        
        JScrollPane scrollPane = new JScrollPane(appointmentArea);

        specializationComboBox.addActionListener(this::updateSlots);
        bookButton.addActionListener(this::bookAppointment);

        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(inputPanel, BorderLayout.CENTER);
        mainPanel.add(scrollPane, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void updateSlots(ActionEvent e) {
        String specialization = (String) specializationComboBox.getSelectedItem();
        slotComboBox.removeAllItems();
        slotComboBox.addItem("Select Slot");

        for (Doctor doctor : doctors) {
            if (doctor.getSpecialization().equalsIgnoreCase(specialization)) {
                for (String slot : doctor.getAvailableSlots()) {
                    slotComboBox.addItem(slot);
                }
                break;
            }
        }
    }

    private void bookAppointment(ActionEvent e) {
        String specialization = (String) specializationComboBox.getSelectedItem();
        String slot = (String) slotComboBox.getSelectedItem();
        String patientName = patientNameField.getText();
        String contact = contactField.getText();

        if (specialization.equals("Select Specialization") || slot.equals("Select Slot") ||
            patientName.isEmpty() || contact.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Doctor selectedDoctor = null;
        for (Doctor doctor : doctors) {
            if (doctor.getSpecialization().equalsIgnoreCase(specialization)) {
                selectedDoctor = doctor;
                break;
            }
        }

        if (selectedDoctor != null && selectedDoctor.bookSlot(slot)) {
            Patient patient = new Patient(patientName, contact);
            Appointment appointment = new Appointment(selectedDoctor, patient, slot);
            appointments.add(appointment);
            appointmentArea.append(appointment + "\n");

            JOptionPane.showMessageDialog(this, "Appointment booked successfully!");
        } else {
            JOptionPane.showMessageDialog(this, "Slot is unavailable.", "Booking Failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new DoctorAppointmentBookingGUI().setVisible(true);
        });
    }
}

