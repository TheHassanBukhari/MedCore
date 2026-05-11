package gui;

import javax.swing.*;
import javax.swing.table.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.sql.*;
import models.*;
import db.OracleConnection;

public class HospitalGUI extends JFrame {

    // ==================== THEME CONSTANTS ====================
    private static final Color BG_DARK = new Color(10, 14, 20);
    private static final Color BG_PANEL = new Color(16, 22, 32);
    private static final Color BG_CARD = new Color(22, 30, 44);
    private static final Color BG_INPUT = new Color(28, 38, 56);
    private static final Color ACCENT_TEAL = new Color(0, 188, 188);
    private static final Color ACCENT_BLUE = new Color(30, 120, 220);
    private static final Color ACCENT_RED = new Color(220, 60, 60);
    private static final Color ACCENT_GREEN = new Color(40, 180, 100);
    private static final Color TEXT_PRIMARY = new Color(220, 228, 240);
    private static final Color TEXT_MUTED = new Color(100, 120, 150);
    private static final Color BORDER_COLOR = new Color(35, 50, 72);
    private static final Color ROW_ALT = new Color(18, 26, 38);
    private static final Color ROW_HOVER = new Color(0, 188, 188, 30);
    private static final Color ROW_SELECT = new Color(0, 188, 188, 55);

    private static final Font FONT_TITLE = new Font("Courier New", Font.BOLD, 15);
    private static final Font FONT_LABEL = new Font("Courier New", Font.PLAIN, 13);
    private static final Font FONT_MONO = new Font("Courier New", Font.PLAIN, 12);
    private static final Font FONT_HEADER = new Font("Courier New", Font.BOLD, 22);
    private static final Font FONT_BTN = new Font("Courier New", Font.BOLD, 12);

    // ==================== FIELDS ====================
    private Connection dbConnection;
    private CardLayout cardLayout;
    private JPanel contentArea;

    private DefaultTableModel doctorTableModel;
    private DefaultTableModel patientTableModel;
    private DefaultTableModel medicineTableModel;

    private JLabel statusLabel;
    private JPanel sidebarPanel;
    private String currentSection = "";

    // ==================== CONSTRUCTOR ====================
    public HospitalGUI() {
        connectDatabase();
        setTitle("MedCore");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 720);
        setMinimumSize(new Dimension(900, 600));
        setLocationRelativeTo(null);
        setBackground(BG_DARK);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(BG_DARK);

        cardLayout = new CardLayout();
        JPanel outer = new JPanel(cardLayout);
        outer.setBackground(BG_DARK);

        outer.add(buildLoginScreen(), "LOGIN");
        outer.add(buildMainApp(), "APP");

        root.add(outer);
        setContentPane(root);
        cardLayout.show(outer, "LOGIN");
    }

    private void connectDatabase() {
        try {
            dbConnection = OracleConnection.getConnection();
        } catch (Exception e) {
            System.err.println("DB connection failed: " + e.getMessage());
        }
    }

    // ==================== LOGIN SCREEN ====================
    private JPanel buildLoginScreen() {
        JPanel panel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // Background
                g2.setColor(BG_DARK);
                g2.fillRect(0, 0, getWidth(), getHeight());
                RadialGradientPaint glow = new RadialGradientPaint(
                        new Point2D.Float(0, 0),
                        600f,
                        new float[] { 0f, 1f },
                        new Color[] { new Color(0, 188, 188, 40), new Color(0, 0, 0, 0) });
                g2.setPaint(glow);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        panel.setBackground(BG_DARK);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 0, 8, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;

        // Logo / branding block
        JPanel brandBlock = new JPanel();
        brandBlock.setOpaque(false);
        brandBlock.setLayout(new BoxLayout(brandBlock, BoxLayout.Y_AXIS));

        JLabel cross = new JLabel("+", SwingConstants.CENTER);
        cross.setFont(new Font("Courier New", Font.BOLD, 100));
        cross.setForeground(ACCENT_TEAL);
        cross.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel title = new JLabel("MEDCORE", SwingConstants.CENTER);
        title.setFont(new Font("Courier New", Font.BOLD, 48));
        title.setForeground(TEXT_PRIMARY);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        brandBlock.add(cross);
        brandBlock.add(Box.createVerticalStrut(4));
        brandBlock.add(title);
        brandBlock.add(Box.createVerticalStrut(6));

        gbc.gridy = 0;
        panel.add(brandBlock, gbc);

        // Password label
        gbc.insets = new Insets(6, 0, 4, 0);
        JLabel passLbl = new JLabel("ADMIN PASSWORD");
        passLbl.setFont(new Font("Courier New", Font.BOLD, 10));
        passLbl.setForeground(TEXT_MUTED);
        gbc.gridy = 2;
        panel.add(passLbl, gbc);

        // Password field
        JPasswordField passField = new JPasswordField(20);
        passField.setFont(FONT_MONO);
        passField.setBackground(BG_INPUT);
        passField.setForeground(TEXT_PRIMARY);
        passField.setCaretColor(ACCENT_TEAL);
        passField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                BorderFactory.createEmptyBorder(10, 14, 10, 14)));
        passField.setPreferredSize(new Dimension(340, 46));
        passField.setEchoChar('\u25CF');
        gbc.gridy = 3;
        gbc.insets = new Insets(0, 0, 16, 0);
        panel.add(passField, gbc);

        // Login button
        JButton loginBtn = makeButton("ENTER SYSTEM", ACCENT_TEAL, BG_DARK);
        loginBtn.setPreferredSize(new Dimension(340, 46));
        gbc.gridy = 4;
        gbc.insets = new Insets(0, 0, 10, 0);
        panel.add(loginBtn, gbc);

        // Exit button
        JButton exitBtn = makeGhostButton("EXIT");
        exitBtn.setPreferredSize(new Dimension(340, 38));
        gbc.gridy = 5;
        gbc.insets = new Insets(0, 0, 0, 0);
        panel.add(exitBtn, gbc);

        // footerLabel note
        JLabel footerLabel = new JLabel("A Fun Project by Hassan Bukhari", SwingConstants.CENTER);
        footerLabel.setFont(new Font("Courier New", Font.PLAIN, 11));
        footerLabel.setForeground(new Color(60, 80, 110));
        gbc.gridy = 6;
        gbc.insets = new Insets(30, 0, 0, 0);
        panel.add(footerLabel, gbc);

        loginBtn.addActionListener(e -> {
            String pw = new String(passField.getPassword());
            if (pw.equals("admin123")) {
                cardLayout.show((JPanel) panel.getParent(), "APP");
                setStatus("Logged in as Administrator", ACCENT_GREEN);
            } else {
                passField.setText("");
                passField.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(ACCENT_RED, 1),
                        BorderFactory.createEmptyBorder(10, 14, 10, 14)));
                JLabel errLbl = new JLabel("Incorrect password");
                errLbl.setForeground(ACCENT_RED);
                errLbl.setFont(FONT_MONO);
                // shake effect
                Timer shaker = new Timer(30, null);
                final int[] count = { 0 };
                final int[] dx = { -6, 6, -5, 5, -3, 3, -1, 1, 0 };
                Point orig = passField.getLocation();
                shaker.addActionListener(se -> {
                    if (count[0] < dx.length) {
                        passField.setLocation(orig.x + dx[count[0]], orig.y);
                        count[0]++;
                    } else {
                        passField.setLocation(orig);
                        shaker.stop();
                    }
                });
                shaker.start();
            }
        });

        passField.addActionListener(e -> loginBtn.doClick());
        exitBtn.addActionListener(e -> System.exit(0));

        return panel;
    }

    // ==================== MAIN APP LAYOUT ====================
    private JPanel buildMainApp() {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(BG_DARK);

        // Top bar
        wrapper.add(buildTopBar(), BorderLayout.NORTH);

        // Sidebar
        sidebarPanel = buildSidebar();
        wrapper.add(sidebarPanel, BorderLayout.WEST);

        // Content area with card layout
        CardLayout innerCard = new CardLayout();
        contentArea = new JPanel(innerCard);
        contentArea.setBackground(BG_PANEL);

        contentArea.add(buildWelcomeCard(), "WELCOME");
        contentArea.add(buildDoctorSection(), "DOCTOR");
        contentArea.add(buildPatientSection(), "PATIENT");
        contentArea.add(buildMedicineSection(), "MEDICINE");
        contentArea.add(buildQueriesSection(), "QUERIES");

        innerCard.show(contentArea, "WELCOME");
        wrapper.add(contentArea, BorderLayout.CENTER);

        // Status bar
        wrapper.add(buildStatusBar(), BorderLayout.SOUTH);

        return wrapper;
    }

    // ==================== TOP BAR ====================
    private JPanel buildTopBar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(BG_CARD);
        bar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER_COLOR));
        bar.setPreferredSize(new Dimension(0, 52));

        JLabel logo = new JLabel("  + MEDCORE");
        logo.setFont(new Font("Courier New", Font.BOLD, 16));
        logo.setForeground(ACCENT_TEAL);
        bar.add(logo, BorderLayout.WEST);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 16, 10));
        right.setOpaque(false);

        JLabel clockLabel = new JLabel();
        clockLabel.setFont(FONT_MONO);
        clockLabel.setForeground(TEXT_MUTED);
        right.add(clockLabel);

        Timer clock = new Timer(1000, e -> {
            java.time.LocalTime now = java.time.LocalTime.now();
            clockLabel.setText(String.format("%02d:%02d:%02d", now.getHour(), now.getMinute(), now.getSecond()));
        });
        clock.start();

        JButton logoutBtn = makeGhostButton("LOGOUT");
        logoutBtn.setFont(new Font("Courier New", Font.BOLD, 10));
        logoutBtn.setPreferredSize(new Dimension(90, 30));
        logoutBtn.addActionListener(e -> {
            cardLayout.show((JPanel) getContentPane().getComponent(0), "LOGIN");
            setStatus("Logged out", TEXT_MUTED);
        });
        right.add(logoutBtn);

        bar.add(right, BorderLayout.EAST);
        return bar;
    }

    // ==================== SIDEBAR ====================
    private JPanel buildSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setBackground(BG_CARD);
        sidebar.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, BORDER_COLOR));
        sidebar.setPreferredSize(new Dimension(200, 0));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));

        sidebar.add(Box.createVerticalStrut(24));

        String[][] items = {
                { "OVERVIEW", "WELCOME" },
                { "DOCTORS", "DOCTOR" },
                { "PATIENTS", "PATIENT" },
                { "MEDICINES", "MEDICINE" },
                { "CUSTOM QUERY", "QUERIES" }
        };

        for (String[] item : items) {
            JButton btn = buildNavButton(item[0], item[1]);
            sidebar.add(btn);
            sidebar.add(Box.createVerticalStrut(4));
        }

        sidebar.add(Box.createVerticalGlue());
        return sidebar;
    }

    private JButton buildNavButton(String label, String target) {
        JButton btn = new JButton(label) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (currentSection.equals(target)) {
                    g2.setColor(new Color(0, 188, 188, 25));
                    g2.fillRect(0, 0, getWidth(), getHeight());
                    g2.setColor(ACCENT_TEAL);
                    g2.fillRect(0, 0, 3, getHeight());
                } else if (getModel().isRollover()) {
                    g2.setColor(new Color(255, 255, 255, 8));
                    g2.fillRect(0, 0, getWidth(), getHeight());
                }
                super.paintComponent(g);
            }
        };
        btn.setFont(new Font("Courier New", Font.BOLD, 11));
        btn.setForeground(currentSection.equals(target) ? ACCENT_TEAL : TEXT_MUTED);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setMaximumSize(new Dimension(200, 42));
        btn.setPreferredSize(new Dimension(200, 42));
        btn.setBorder(BorderFactory.createEmptyBorder(0, 22, 0, 0));

        btn.addActionListener(e -> {
            currentSection = target;
            switchSection(target);
            // refresh all nav buttons
            for (Component c : sidebarPanel.getComponents()) {
                if (c instanceof JButton) {
                    JButton nb = (JButton) c;
                    String nbTarget = (String) nb.getClientProperty("target");
                    if (nbTarget != null) {
                        nb.setForeground(nbTarget.equals(target) ? ACCENT_TEAL : TEXT_MUTED);
                        nb.repaint();
                    }
                }
            }
        });
        btn.putClientProperty("target", target);
        return btn;
    }

    private void switchSection(String section) {
        CardLayout cl = (CardLayout) contentArea.getLayout();
        switch (section) {
            case "DOCTOR":
                refreshDoctorTable();
                break;
            case "PATIENT":
                refreshPatientTable();
                break;
            case "MEDICINE":
                refreshMedicineTable();
                break;
        }
        cl.show(contentArea, section);
    }

    // ==================== STATUS BAR ====================
    private JPanel buildStatusBar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(new Color(8, 12, 18));
        bar.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, BORDER_COLOR));
        bar.setPreferredSize(new Dimension(0, 28));

        statusLabel = new JLabel("  Ready");
        statusLabel.setFont(new Font("Courier New", Font.PLAIN, 11));
        statusLabel.setForeground(TEXT_MUTED);
        bar.add(statusLabel, BorderLayout.WEST);

        JLabel dbLabel = new JLabel("Oracle XE  ");
        dbLabel.setFont(new Font("Courier New", Font.PLAIN, 11));
        dbLabel.setForeground(dbConnection != null ? ACCENT_GREEN : ACCENT_RED);
        bar.add(dbLabel, BorderLayout.EAST);

        return bar;
    }

    private void setStatus(String msg, Color color) {
        if (statusLabel != null) {
            statusLabel.setText("  " + msg);
            statusLabel.setForeground(color);
        }
    }

    // ==================== WELCOME CARD ====================
    private JPanel buildWelcomeCard() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(BG_PANEL);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 0, 8, 0);
        gbc.anchor = GridBagConstraints.CENTER;

        JLabel icon = new JLabel("+");
        icon.setFont(new Font("Courier New", Font.BOLD, 72));
        icon.setForeground(new Color(0, 188, 188, 60));
        gbc.gridy = 0;
        panel.add(icon, gbc);

        JLabel title = new JLabel("WELCOME TO MEDCORE");
        title.setFont(new Font("Courier New", Font.BOLD, 26));
        title.setForeground(TEXT_PRIMARY);
        gbc.gridy = 1;
        panel.add(title, gbc);

        JLabel sub = new JLabel("Select a section from the sidebar to get started.");
        sub.setFont(FONT_MONO);
        sub.setForeground(TEXT_MUTED);
        gbc.gridy = 2;
        gbc.insets = new Insets(4, 0, 0, 0);
        panel.add(sub, gbc);

        return panel;
    }

    // ==================== DOCTOR SECTION ====================
    private JPanel buildDoctorSection() {
        String[] cols = { "ID", "First Name", "Middle Name", "Last Name" };
        doctorTableModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        JTable table = buildStyledTable(doctorTableModel);

        JButton[] buttons = {
                makeButton("ADD DOCTOR", ACCENT_GREEN, BG_DARK),
                makeButton("UPDATE", ACCENT_BLUE, BG_DARK),
                makeButton("DELETE", ACCENT_RED, BG_DARK),
                makeButton("REFRESH", ACCENT_TEAL, BG_DARK),
                makeGhostButton("SPECIALIZATIONS"),
                makeGhostButton("QUALIFICATIONS"),
                makeGhostButton("ASSIGN PATIENT"),
                makeGhostButton("VIEW PATIENTS")
        };

        buttons[0].addActionListener(e -> addDoctor());
        buttons[1].addActionListener(e -> updateDoctor(table));
        buttons[2].addActionListener(e -> deleteDoctor(table));
        buttons[3].addActionListener(e -> refreshDoctorTable());
        buttons[4].addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                showError("Select a doctor first.");
                return;
            }
            new SubListDialog(this, "Specializations", (int) doctorTableModel.getValueAt(row, 0), "SPEC")
                    .setVisible(true);
        });
        buttons[5].addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                showError("Select a doctor first.");
                return;
            }
            new SubListDialog(this, "Qualifications", (int) doctorTableModel.getValueAt(row, 0), "QUAL")
                    .setVisible(true);
        });
        buttons[6].addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                showError("Select a doctor first.");
                return;
            }
            int docId = (int) doctorTableModel.getValueAt(row, 0);
            String input = showInputDialog("Assign Patient", "Patient ID:");
            if (input == null)
                return;
            try {
                Doctor doc = Doctor.getDoctorById(docId);
                doc.assignPatient(Integer.parseInt(input.trim()));
                setStatus("Patient assigned to Dr. ID " + docId, ACCENT_GREEN);
            } catch (NumberFormatException ex) {
                showError("Invalid Patient ID.");
            }
        });
        buttons[7].addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                showError("Select a doctor first.");
                return;
            }
            int docId = (int) doctorTableModel.getValueAt(row, 0);
            new RecordViewDialog(this, "Patients of Doctor " + docId,
                    new String[] { "Patient ID", "Age", "DOB", "Locality", "Town/City" },
                    "SELECT p.* FROM PATIENT p JOIN TREATS t ON p.patient_id = t.patient_id WHERE t.doctor_id = "
                            + docId)
                    .setVisible(true);
        });

        return buildSection("DOCTOR MANAGEMENT", table, buttons);
    }

    private void addDoctor() {
        FormDialog form = new FormDialog(this, "Add New Doctor",
                new String[] { "Doctor ID", "First Name", "Middle Name", "Last Name" });
        form.setVisible(true);
        String[] vals = form.getValues();
        if (vals == null)
            return;
        try {
            Doctor d = new Doctor(Integer.parseInt(vals[0].trim()), vals[1].trim(), vals[2].trim(), vals[3].trim());
            d.insertDoctor();
            refreshDoctorTable();
            setStatus("Doctor added: ID " + vals[0], ACCENT_GREEN);
        } catch (NumberFormatException ex) {
            showError("Doctor ID must be a number.");
        }
    }

    private void updateDoctor(JTable table) {
        int row = table.getSelectedRow();
        if (row < 0) {
            showError("Select a doctor to update.");
            return;
        }
        int id = (int) doctorTableModel.getValueAt(row, 0);
        Doctor d = Doctor.getDoctorById(id);
        FormDialog form = new FormDialog(this, "Update Doctor ID " + id,
                new String[] { "First Name", "Middle Name", "Last Name" },
                new String[] { d.getFirstName(), d.getMiddleName(), d.getLastName() });
        form.setVisible(true);
        String[] vals = form.getValues();
        if (vals == null)
            return;
        new Doctor(id, vals[0], vals[1], vals[2]).updateDoctor();
        refreshDoctorTable();
        setStatus("Doctor updated: ID " + id, ACCENT_GREEN);
    }

    private void deleteDoctor(JTable table) {
        int row = table.getSelectedRow();
        if (row < 0) {
            showError("Select a doctor to delete.");
            return;
        }
        int id = (int) doctorTableModel.getValueAt(row, 0);

        try {
            PreparedStatement p1 = dbConnection.prepareStatement("SELECT COUNT(*) FROM TREATS WHERE doctor_id = ?");
            p1.setInt(1, id);
            ResultSet r1 = p1.executeQuery();
            r1.next();
            int patients = r1.getInt(1);
            r1.close();
            p1.close();

            PreparedStatement p2 = dbConnection
                    .prepareStatement("SELECT COUNT(*) FROM DOCTOR_SPECIALIZATION WHERE doctor_id = ?");
            p2.setInt(1, id);
            ResultSet r2 = p2.executeQuery();
            r2.next();
            int specs = r2.getInt(1);
            r2.close();
            p2.close();

            PreparedStatement p3 = dbConnection
                    .prepareStatement("SELECT COUNT(*) FROM DOCTOR_QUALIFICATION WHERE doctor_id = ?");
            p3.setInt(1, id);
            ResultSet r3 = p3.executeQuery();
            r3.next();
            int quals = r3.getInt(1);
            r3.close();
            p3.close();

            StringBuilder msg = new StringBuilder("Delete Doctor ID " + id + "?\n\n");
            if (patients > 0)
                msg.append("  - ").append(patients).append(" patient(s) will be unassigned\n");
            if (specs > 0)
                msg.append("  - ").append(specs).append(" specialization(s) will be removed\n");
            if (quals > 0)
                msg.append("  - ").append(quals).append(" qualification(s) will be removed\n");
            if (patients == 0 && specs == 0 && quals == 0)
                msg.append("  No linked records found.\n");
            msg.append("\nThis cannot be undone.");

            int confirm = JOptionPane.showConfirmDialog(this, msg.toString(),
                    "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (confirm != JOptionPane.YES_OPTION)
                return;

            PreparedStatement d1 = dbConnection.prepareStatement("DELETE FROM TREATS WHERE doctor_id = ?");
            d1.setInt(1, id);
            d1.executeUpdate();
            d1.close();
            PreparedStatement d2 = dbConnection
                    .prepareStatement("DELETE FROM DOCTOR_SPECIALIZATION WHERE doctor_id = ?");
            d2.setInt(1, id);
            d2.executeUpdate();
            d2.close();
            PreparedStatement d3 = dbConnection
                    .prepareStatement("DELETE FROM DOCTOR_QUALIFICATION WHERE doctor_id = ?");
            d3.setInt(1, id);
            d3.executeUpdate();
            d3.close();
            PreparedStatement d4 = dbConnection.prepareStatement("DELETE FROM DOCTOR WHERE doctor_id = ?");
            d4.setInt(1, id);
            d4.executeUpdate();
            d4.close();

            refreshDoctorTable();
            setStatus("Doctor deleted: ID " + id, ACCENT_RED);

        } catch (SQLException ex) {
            showError("Delete failed: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void refreshDoctorTable() {
        doctorTableModel.setRowCount(0);
        try {
            Statement st = dbConnection.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM DOCTOR ORDER BY doctor_id");
            while (rs.next()) {
                doctorTableModel.addRow(new Object[] {
                        rs.getInt("doctor_id"),
                        rs.getString("first_name"),
                        rs.getString("middle_name"),
                        rs.getString("last_name")
                });
            }
            rs.close();
            st.close();
            setStatus("Doctor records loaded: " + doctorTableModel.getRowCount(), TEXT_MUTED);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ==================== PATIENT SECTION ====================
    private JPanel buildPatientSection() {
        String[] cols = { "ID", "Age", "DOB", "Locality", "Town/City" };
        patientTableModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        JTable table = buildStyledTable(patientTableModel);

        JButton[] buttons = {
                makeButton("ADD PATIENT", ACCENT_GREEN, BG_DARK),
                makeButton("UPDATE", ACCENT_BLUE, BG_DARK),
                makeButton("DELETE", ACCENT_RED, BG_DARK),
                makeButton("REFRESH", ACCENT_TEAL, BG_DARK),
                makeGhostButton("MEDICINES"),
                makeGhostButton("DOCTORS"),
                makeGhostButton("CALC BILL")
        };

        buttons[0].addActionListener(e -> addPatient());
        buttons[1].addActionListener(e -> updatePatient(table));
        buttons[2].addActionListener(e -> deletePatient(table));
        buttons[3].addActionListener(e -> refreshPatientTable());
        buttons[4].addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                showError("Select a patient first.");
                return;
            }
            new ManageMedicinesDialog(this, (int) patientTableModel.getValueAt(row, 0)).setVisible(true);
        });
        buttons[5].addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row < 0) {
                showError("Select a patient first.");
                return;
            }
            int pid = (int) patientTableModel.getValueAt(row, 0);
            new RecordViewDialog(this, "Doctors of Patient " + pid,
                    new String[] { "Doctor ID", "First Name", "Middle Name", "Last Name" },
                    "SELECT d.* FROM DOCTOR d JOIN TREATS t ON d.doctor_id = t.doctor_id WHERE t.patient_id = " + pid)
                    .setVisible(true);
        });
        buttons[6].addActionListener(e -> calculateBill(table));

        return buildSection("PATIENT MANAGEMENT", table, buttons);
    }

    private void addPatient() {
        FormDialog form = new FormDialog(this, "Add New Patient",
                new String[] { "Patient ID", "DOB (YYYY-MM-DD)", "Locality", "Town/City" });

        form.setVisible(true);
        String[] vals = form.getValues();
        if (vals == null)
            return;

        try {
            Patient p = new Patient(
                    Integer.parseInt(vals[0].trim()),
                    0, // age ignored, will be computed in insert
                    vals[1].trim(),
                    vals[2].trim(),
                    vals[3].trim());

            p.insertPatient();
            refreshPatientTable();
            setStatus("Patient added: ID " + vals[0], ACCENT_GREEN);

        } catch (NumberFormatException ex) {
            showError("ID must be a number.");
        }
    }

    private void updatePatient(JTable table) {
        int row = table.getSelectedRow();
        if (row < 0) {
            showError("Select a patient to update.");
            return;
        }
        int id = (int) patientTableModel.getValueAt(row, 0);
        Patient p = Patient.getPatientById(id);
        // Trim the dob to just YYYY-MM-DD in case Oracle returns a full timestamp
        String dob = p.getDob() != null ? p.getDob().toString().substring(0, 10) : "";
        FormDialog form = new FormDialog(this, "Update Patient ID " + id,
                new String[] { "Age", "DOB (YYYY-MM-DD)", "Locality", "Town/City" },
                new String[] { String.valueOf(p.getAge()), dob, p.getLocality(), p.getTownCity() });
        form.setVisible(true);
        String[] vals = form.getValues();
        if (vals == null)
            return;
        try {
            new Patient(id, Integer.parseInt(vals[0]), vals[1], vals[2], vals[3]).updatePatient();
            refreshPatientTable();
            setStatus("Patient updated: ID " + id, ACCENT_GREEN);
        } catch (NumberFormatException ex) {
            showError("Age must be a number.");
        }
    }

    private void deletePatient(JTable table) {
        int row = table.getSelectedRow();
        if (row < 0) {
            showError("Select a patient to delete.");
            return;
        }
        int id = (int) patientTableModel.getValueAt(row, 0);
        if (!confirmDelete("patient ID " + id))
            return;
        new Patient(id, 0, "", "", "").deletePatient();
        refreshPatientTable();
        setStatus("Patient deleted: ID " + id, ACCENT_RED);
    }

    private void calculateBill(JTable table) {
        int row = table.getSelectedRow();
        if (row < 0) {
            showError("Select a patient first.");
            return;
        }
        int pid = (int) patientTableModel.getValueAt(row, 0);
        try {
            PreparedStatement pst = dbConnection.prepareStatement(
                    "SELECT SUM(m.price * m.quantity) AS total " +
                            "FROM BILL b JOIN MEDICINE m ON b.medicine_code = m.code WHERE b.patient_id = ?");
            pst.setInt(1, pid);
            ResultSet rs = pst.executeQuery();
            String total = rs.next() ? String.format("PKR %.2f", rs.getDouble("total")) : "No medicines assigned.";
            rs.close();
            pst.close();

            JDialog dlg = buildStyledDialog(this, "Total Bill  Patient " + pid, 340, 200);
            JPanel inner = new JPanel(new GridBagLayout());
            inner.setBackground(BG_CARD);
            inner.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
            JLabel amt = new JLabel(total, SwingConstants.CENTER);
            amt.setFont(new Font("Courier New", Font.BOLD, 28));
            amt.setForeground(ACCENT_TEAL);
            JLabel lbl = new JLabel("Total Bill for Patient " + pid, SwingConstants.CENTER);
            lbl.setFont(FONT_MONO);
            lbl.setForeground(TEXT_MUTED);
            JButton close = makeButton("CLOSE", ACCENT_TEAL, BG_DARK);
            close.addActionListener(ev -> dlg.dispose());
            GridBagConstraints g = new GridBagConstraints();
            g.gridy = 0;
            g.insets = new Insets(0, 0, 6, 0);
            inner.add(lbl, g);
            g.gridy = 1;
            g.insets = new Insets(0, 0, 20, 0);
            inner.add(amt, g);
            g.gridy = 2;
            inner.add(close, g);
            dlg.add(inner);
            dlg.setVisible(true);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void refreshPatientTable() {
        patientTableModel.setRowCount(0);
        try {
            Statement st = dbConnection.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM PATIENT ORDER BY patient_id");
            while (rs.next()) {
                patientTableModel.addRow(new Object[] {
                        rs.getInt("patient_id"), rs.getInt("age"),
                        rs.getString("dob"), rs.getString("locality"), rs.getString("town_city")
                });
            }
            rs.close();
            st.close();
            setStatus("Patient records loaded: " + patientTableModel.getRowCount(), TEXT_MUTED);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ==================== MEDICINE SECTION ====================
    private JPanel buildMedicineSection() {
        String[] cols = { "Code", "Price (PKR)", "Quantity" };
        medicineTableModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        JTable table = buildStyledTable(medicineTableModel);

        JButton[] buttons = {
                makeButton("ADD MEDICINE", ACCENT_GREEN, BG_DARK),
                makeButton("UPDATE", ACCENT_BLUE, BG_DARK),
                makeButton("DELETE", ACCENT_RED, BG_DARK),
                makeButton("REFRESH", ACCENT_TEAL, BG_DARK)
        };

        buttons[0].addActionListener(e -> addMedicine());
        buttons[1].addActionListener(e -> updateMedicine(table));
        buttons[2].addActionListener(e -> deleteMedicine(table));
        buttons[3].addActionListener(e -> refreshMedicineTable());

        return buildSection("MEDICINE MANAGEMENT", table, buttons);
    }

    private void addMedicine() {
        FormDialog form = new FormDialog(this, "Add New Medicine",
                new String[] { "Medicine Code", "Price", "Quantity" });
        form.setVisible(true);
        String[] vals = form.getValues();
        if (vals == null)
            return;
        try {
            new Medicine(
                    Integer.parseInt(vals[0].trim()),
                    Double.parseDouble(vals[1].trim()),
                    Integer.parseInt(vals[2].trim())).insertMedicine();
            refreshMedicineTable();
            setStatus("Medicine added: Code " + vals[0], ACCENT_GREEN);
        } catch (NumberFormatException ex) {
            showError("Invalid numeric input.");
        }
    }

    private void updateMedicine(JTable table) {
        int row = table.getSelectedRow();
        if (row < 0) {
            showError("Select a medicine to update.");
            return;
        }
        int code = (int) medicineTableModel.getValueAt(row, 0);
        Medicine m = Medicine.getMedicineByCode(code);
        FormDialog form = new FormDialog(this, "Update Medicine Code " + code,
                new String[] { "Price", "Quantity" },
                new String[] { String.valueOf(m.getPrice()), String.valueOf(m.getQuantity()) });
        form.setVisible(true);
        String[] vals = form.getValues();
        if (vals == null)
            return;
        try {
            new Medicine(code, Double.parseDouble(vals[0]), Integer.parseInt(vals[1])).updateMedicine();
            refreshMedicineTable();
            setStatus("Medicine updated: Code " + code, ACCENT_GREEN);
        } catch (NumberFormatException ex) {
            showError("Invalid numeric input.");
        }
    }

    private void deleteMedicine(JTable table) {
        int row = table.getSelectedRow();
        if (row < 0) {
            showError("Select a medicine to delete.");
            return;
        }
        int code = (int) medicineTableModel.getValueAt(row, 0);
        if (!confirmDelete("medicine code " + code))
            return;
        new Medicine(code, 0, 0).deleteMedicine();
        refreshMedicineTable();
        setStatus("Medicine deleted: Code " + code, ACCENT_RED);
    }

    private void refreshMedicineTable() {
        medicineTableModel.setRowCount(0);
        try {
            Statement st = dbConnection.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM MEDICINE ORDER BY code");
            while (rs.next()) {
                medicineTableModel.addRow(new Object[] {
                        rs.getInt("code"), rs.getDouble("price"), rs.getInt("quantity")
                });
            }
            rs.close();
            st.close();
            setStatus("Medicine records loaded: " + medicineTableModel.getRowCount(), TEXT_MUTED);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ==================== QUERIES SECTION ====================
    private JPanel buildQueriesSection() {
        JPanel panel = new JPanel(new BorderLayout(0, 0));
        panel.setBackground(BG_PANEL);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 14, 20));

        JLabel header = new JLabel("CUSTOM QUERY RUNNER");
        header.setFont(FONT_HEADER);
        header.setForeground(TEXT_PRIMARY);
        header.setBorder(BorderFactory.createEmptyBorder(0, 0, 14, 0));
        panel.add(header, BorderLayout.NORTH);

        // Result table
        DefaultTableModel resultModel = new DefaultTableModel() {
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        JTable resultTable = buildStyledTable(resultModel);
        JScrollPane resultScroll = new JScrollPane(resultTable);
        resultScroll.getViewport().setBackground(BG_CARD);
        resultScroll.setBackground(BG_CARD);
        resultScroll.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));

        // Query input area
        JTextArea queryArea = new JTextArea(8, 0);
        queryArea.setFont(FONT_MONO);
        queryArea.setBackground(BG_INPUT);
        queryArea.setForeground(TEXT_PRIMARY);
        queryArea.setCaretColor(ACCENT_TEAL);
        queryArea.setLineWrap(true);
        queryArea.setWrapStyleWord(true);
        queryArea.setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));
        queryArea.setText("SELECT * FROM DOCTOR");

        JScrollPane queryScroll = new JScrollPane(queryArea);
        queryScroll.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));
        queryScroll.setBackground(BG_INPUT);

        // Status label for row count or error
        JLabel queryStatus = new JLabel("Ready.");
        queryStatus.setFont(new Font("Courier New", Font.PLAIN, 11));
        queryStatus.setForeground(TEXT_MUTED);

        JButton runBtn = makeButton("RUN QUERY", ACCENT_TEAL, BG_DARK);
        runBtn.setPreferredSize(new Dimension(130, 36));

        JButton clearBtn = makeGhostButton("CLEAR");
        clearBtn.setPreferredSize(new Dimension(90, 36));
        clearBtn.addActionListener(e -> {
            queryArea.setText("");
            resultModel.setRowCount(0);
            resultModel.setColumnCount(0);
            queryStatus.setText("Cleared.");
            queryStatus.setForeground(TEXT_MUTED);
        });

        runBtn.addActionListener(e -> {
            String sql = queryArea.getText().trim().replaceAll(";$", "");
            if (sql.isEmpty())
                return;
            resultModel.setRowCount(0);
            resultModel.setColumnCount(0);
            try {
                Statement st = dbConnection.createStatement();
                boolean hasResult = st.execute(sql);
                if (hasResult) {
                    ResultSet rs = st.getResultSet();
                    ResultSetMetaData meta = rs.getMetaData();
                    int cols = meta.getColumnCount();
                    for (int i = 1; i <= cols; i++)
                        resultModel.addColumn(meta.getColumnName(i));
                    int rowCount = 0;
                    while (rs.next()) {
                        Object[] row = new Object[cols];
                        for (int i = 0; i < cols; i++)
                            row[i] = rs.getObject(i + 1);
                        resultModel.addRow(row);
                        rowCount++;
                    }
                    rs.close();
                    queryStatus.setText("Returned " + rowCount + " row(s).");
                    queryStatus.setForeground(ACCENT_GREEN);
                } else {
                    int affected = st.getUpdateCount();
                    queryStatus.setText("Query executed. " + affected + " row(s) affected.");
                    queryStatus.setForeground(ACCENT_GREEN);
                }
                st.close();
            } catch (SQLException ex) {
                queryStatus.setText("Error: " + ex.getMessage());
                queryStatus.setForeground(ACCENT_RED);
            }
        });

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btnRow.setBackground(BG_PANEL);
        btnRow.add(queryStatus);
        btnRow.add(clearBtn);
        btnRow.add(runBtn);

        JPanel inputBlock = new JPanel(new BorderLayout(0, 8));
        inputBlock.setBackground(BG_PANEL);
        inputBlock.setBorder(BorderFactory.createEmptyBorder(0, 0, 12, 0));
        inputBlock.add(queryScroll, BorderLayout.CENTER);
        inputBlock.add(btnRow, BorderLayout.SOUTH);

        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, inputBlock, resultScroll);
        split.setDividerSize(6);
        split.setDividerLocation(240);
        split.setBackground(BG_PANEL);
        split.setBorder(null);
        split.setResizeWeight(0.35);

        panel.add(split, BorderLayout.CENTER);
        return panel;
    }

    // ==================== SECTION BUILDER UTILITY ====================
    private JPanel buildSection(String title, JTable table, JButton[] buttons) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BG_PANEL);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 14, 20));

        // Title bar
        JLabel header = new JLabel(title);
        header.setFont(FONT_HEADER);
        header.setForeground(TEXT_PRIMARY);
        header.setBorder(BorderFactory.createEmptyBorder(0, 0, 14, 0));
        panel.add(header, BorderLayout.NORTH);

        // Table
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBackground(BG_CARD);
        scroll.getViewport().setBackground(BG_CARD);
        scroll.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));
        panel.add(scroll, BorderLayout.CENTER);

        // Button bar
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 12));
        bar.setBackground(BG_PANEL);
        for (JButton b : buttons)
            bar.add(b);
        panel.add(bar, BorderLayout.SOUTH);

        return panel;
    }

    // ==================== STYLED TABLE ====================
    private JTable buildStyledTable(DefaultTableModel model) {
        JTable table = new JTable(model) {
            @Override
            public Component prepareRenderer(TableCellRenderer r, int row, int col) {
                Component c = super.prepareRenderer(r, row, col);
                if (isRowSelected(row)) {
                    c.setBackground(ROW_SELECT);
                    c.setForeground(ACCENT_TEAL);
                } else if (row % 2 == 0) {
                    c.setBackground(BG_CARD);
                    c.setForeground(TEXT_PRIMARY);
                } else {
                    c.setBackground(ROW_ALT);
                    c.setForeground(TEXT_PRIMARY);
                }
                return c;
            }
        };
        table.setFont(FONT_MONO);
        table.setBackground(BG_CARD);
        table.setForeground(TEXT_PRIMARY);
        table.setGridColor(new Color(55, 75, 105));
        table.setRowHeight(36);
        table.setShowHorizontalLines(true);
        table.setShowVerticalLines(true);
        table.setSelectionBackground(ROW_SELECT);
        table.setSelectionForeground(ACCENT_TEAL);
        table.setFocusable(false);

        JTableHeader header = table.getTableHeader();
        header.setBackground(new Color(12, 18, 28));
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Courier New", Font.BOLD, 12));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, Color.WHITE));
        header.setPreferredSize(new Dimension(0, 40));
        ((DefaultTableCellRenderer) header.getDefaultRenderer()).setHorizontalAlignment(SwingConstants.CENTER);

        return table;
    }

    // ==================== BUTTON FACTORIES ====================
    private JButton makeButton(String text, Color accent, Color bg) {
        JButton btn = new JButton(text) {
            private boolean hovering = false;
            {
                addMouseListener(new MouseAdapter() {
                    public void mouseEntered(MouseEvent e) {
                        hovering = true;
                        repaint();
                    }

                    public void mouseExited(MouseEvent e) {
                        hovering = false;
                        repaint();
                    }
                });
            }

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color fill = hovering ? accent.brighter() : accent;
                g2.setColor(fill);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 4, 4);
                g2.setColor(bg);
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2.drawString(getText(), x, y);
            }
        };
        btn.setFont(FONT_BTN);
        btn.setForeground(bg);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(130, 36));
        return btn;
    }

    private JButton makeGhostButton(String text) {
        JButton btn = new JButton(text) {
            private boolean hovering = false;
            {
                addMouseListener(new MouseAdapter() {
                    public void mouseEntered(MouseEvent e) {
                        hovering = true;
                        repaint();
                    }

                    public void mouseExited(MouseEvent e) {
                        hovering = false;
                        repaint();
                    }
                });
            }

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (hovering) {
                    g2.setColor(new Color(0, 188, 188, 20));
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 4, 4);
                }
                g2.setColor(hovering ? ACCENT_TEAL : new Color(80, 110, 150));
                g2.setStroke(new BasicStroke(1f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 4, 4);
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2.setColor(hovering ? ACCENT_TEAL : TEXT_MUTED);
                g2.drawString(getText(), x, y);
            }
        };
        btn.setFont(FONT_BTN);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(140, 36));
        return btn;
    }

    // ==================== HELPER DIALOGS ====================
    private void showError(String msg) {
        JDialog dlg = buildStyledDialog(this, "Error", 320, 160);
        JPanel inner = new JPanel(new GridBagLayout());
        inner.setBackground(BG_CARD);
        inner.setBorder(BorderFactory.createEmptyBorder(20, 24, 20, 24));
        GridBagConstraints g = new GridBagConstraints();
        g.gridy = 0;
        g.insets = new Insets(0, 0, 14, 0);
        JLabel lbl = new JLabel(msg);
        lbl.setFont(FONT_MONO);
        lbl.setForeground(ACCENT_RED);
        inner.add(lbl, g);
        g.gridy = 1;
        JButton ok = makeButton("OK", ACCENT_RED, BG_DARK);
        ok.setPreferredSize(new Dimension(100, 34));
        ok.addActionListener(e -> dlg.dispose());
        inner.add(ok, g);
        dlg.add(inner);
        dlg.setVisible(true);
    }

    private boolean confirmDelete(String item) {
        int res = JOptionPane.showConfirmDialog(this,
                "<html><font face='Courier New' color='#E0E4F0'>Delete " + item
                        + "?<br><font color='#6478A0'>This cannot be undone.</font></font></html>",
                "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        return res == JOptionPane.YES_OPTION;
    }

    private String showInputDialog(String title, String label) {
        JTextField field = buildTextField();
        JPanel panel = new JPanel(new GridLayout(2, 1, 6, 6));
        panel.setBackground(BG_CARD);
        JLabel lbl = new JLabel(label);
        lbl.setFont(FONT_LABEL);
        lbl.setForeground(TEXT_MUTED);
        panel.add(lbl);
        panel.add(field);
        int res = JOptionPane.showConfirmDialog(this, panel, title, JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);
        return res == JOptionPane.OK_OPTION ? field.getText() : null;
    }

    private JDialog buildStyledDialog(JFrame parent, String title, int w, int h) {
        JDialog dlg = new JDialog(parent, title, true);
        dlg.setSize(w, h);
        dlg.setLocationRelativeTo(parent);
        dlg.setBackground(BG_CARD);
        dlg.getContentPane().setBackground(BG_CARD);
        dlg.setResizable(false);
        return dlg;
    }

    private JTextField buildTextField() {
        JTextField field = new JTextField();
        field.setFont(FONT_MONO);
        field.setBackground(BG_INPUT);
        field.setForeground(TEXT_PRIMARY);
        field.setCaretColor(ACCENT_TEAL);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)));
        return field;
    }

    // ==================== FORM DIALOG ====================
    class FormDialog extends JDialog {
        private JTextField[] fields;
        private boolean submitted = false;

        FormDialog(JFrame parent, String title, String[] labels) {
            this(parent, title, labels, new String[labels.length]);
        }

        FormDialog(JFrame parent, String title, String[] labels, String[] defaults) {
            super(parent, title, true);
            fields = new JTextField[labels.length];
            setBackground(BG_CARD);
            getContentPane().setBackground(BG_CARD);
            setLayout(new BorderLayout());

            JLabel header = new JLabel("  " + title);
            header.setFont(FONT_TITLE);
            header.setForeground(ACCENT_TEAL);
            header.setOpaque(true);
            header.setBackground(new Color(12, 18, 28));
            header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER_COLOR));
            header.setPreferredSize(new Dimension(0, 44));
            add(header, BorderLayout.NORTH);

            JPanel form = new JPanel(new GridBagLayout());
            form.setBackground(BG_CARD);
            form.setBorder(BorderFactory.createEmptyBorder(20, 24, 16, 24));
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.insets = new Insets(5, 0, 5, 0);

            for (int i = 0; i < labels.length; i++) {
                JLabel lbl = new JLabel(labels[i]);
                lbl.setFont(new Font("Courier New", Font.BOLD, 10));
                lbl.setForeground(TEXT_MUTED);
                gbc.gridx = 0;
                gbc.gridy = i * 2;
                gbc.weightx = 1;
                form.add(lbl, gbc);

                JTextField tf = buildTextField();
                tf.setText(defaults[i] != null ? defaults[i] : "");
                tf.setPreferredSize(new Dimension(320, 38));
                fields[i] = tf;
                gbc.gridy = i * 2 + 1;
                form.add(tf, gbc);
            }

            add(form, BorderLayout.CENTER);

            JPanel btns = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 12));
            btns.setBackground(new Color(12, 18, 28));
            btns.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, BORDER_COLOR));
            JButton cancel = makeGhostButton("CANCEL");
            cancel.setPreferredSize(new Dimension(100, 34));
            cancel.addActionListener(e -> dispose());
            JButton submit = makeButton("SAVE", ACCENT_TEAL, BG_DARK);
            submit.setPreferredSize(new Dimension(100, 34));
            submit.addActionListener(e -> {
                submitted = true;
                dispose();
            });
            btns.add(cancel);
            btns.add(submit);
            add(btns, BorderLayout.SOUTH);

            pack();
            setMinimumSize(new Dimension(380, 0));
            setResizable(false);
            setLocationRelativeTo(parent);
        }

        String[] getValues() {
            if (!submitted)
                return null;
            String[] vals = new String[fields.length];
            for (int i = 0; i < fields.length; i++)
                vals[i] = fields[i].getText();
            return vals;
        }
    }

    // ==================== RECORD VIEW DIALOG ====================
    class RecordViewDialog extends JDialog {
        RecordViewDialog(JFrame parent, String title, String[] colNames, String sql) {
            super(parent, title, true);
            setBackground(BG_CARD);
            getContentPane().setBackground(BG_CARD);
            setLayout(new BorderLayout());

            JLabel header = new JLabel("  " + title);
            header.setFont(FONT_TITLE);
            header.setForeground(ACCENT_TEAL);
            header.setOpaque(true);
            header.setBackground(new Color(12, 18, 28));
            header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER_COLOR));
            header.setPreferredSize(new Dimension(0, 44));
            add(header, BorderLayout.NORTH);

            DefaultTableModel model = new DefaultTableModel();
            JTable table = buildStyledTable(model);

            try {
                Statement st = dbConnection.createStatement();
                ResultSet rs = st.executeQuery(sql);
                ResultSetMetaData meta = rs.getMetaData();
                int cols = meta.getColumnCount();
                for (int i = 1; i <= cols; i++)
                    model.addColumn(meta.getColumnName(i));
                while (rs.next()) {
                    Object[] row = new Object[cols];
                    for (int i = 0; i < cols; i++)
                        row[i] = rs.getObject(i + 1);
                    model.addRow(row);
                }
                rs.close();
                st.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            JScrollPane scroll = new JScrollPane(table);
            scroll.getViewport().setBackground(BG_CARD);
            scroll.setBackground(BG_CARD);
            scroll.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
            add(scroll, BorderLayout.CENTER);

            JPanel btns = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 10));
            btns.setBackground(new Color(12, 18, 28));
            btns.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, BORDER_COLOR));
            JButton close = makeButton("CLOSE", ACCENT_TEAL, BG_DARK);
            close.setPreferredSize(new Dimension(100, 34));
            close.addActionListener(e -> dispose());
            btns.add(close);
            add(btns, BorderLayout.SOUTH);

            setSize(720, 480);
            setLocationRelativeTo(parent);
        }
    }

    // ==================== SUB LIST DIALOG (SPEC / QUAL) ====================
    class SubListDialog extends JDialog {
        private DefaultTableModel model;
        private final int doctorId;
        private final String mode; // "SPEC" or "QUAL"

        SubListDialog(JFrame parent, String title, int doctorId, String mode) {
            super(parent, title + "  Dr. ID " + doctorId, true);
            this.doctorId = doctorId;
            this.mode = mode;
            setBackground(BG_CARD);
            getContentPane().setBackground(BG_CARD);
            setLayout(new BorderLayout());

            JLabel header = new JLabel("  " + title + " for Doctor " + doctorId);
            header.setFont(FONT_TITLE);
            header.setForeground(ACCENT_TEAL);
            header.setOpaque(true);
            header.setBackground(new Color(12, 18, 28));
            header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER_COLOR));
            header.setPreferredSize(new Dimension(0, 44));
            add(header, BorderLayout.NORTH);

            String col = mode.equals("SPEC") ? "Specialization" : "Qualification";
            model = new DefaultTableModel(new String[] { col }, 0) {
                public boolean isCellEditable(int r, int c) {
                    return false;
                }
            };
            JTable table = buildStyledTable(model);
            loadRows();

            JScrollPane scroll = new JScrollPane(table);
            scroll.getViewport().setBackground(BG_CARD);
            scroll.setBackground(BG_CARD);
            scroll.setBorder(BorderFactory.createEmptyBorder(8, 8, 4, 8));
            add(scroll, BorderLayout.CENTER);

            // Input bar
            JPanel bottom = new JPanel(new BorderLayout());
            bottom.setBackground(new Color(12, 18, 28));
            bottom.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(1, 0, 0, 0, BORDER_COLOR),
                    BorderFactory.createEmptyBorder(10, 12, 10, 12)));

            JTextField input = buildTextField();
            input.setPreferredSize(new Dimension(0, 36));
            input.setToolTipText("Enter " + col);

            JButton addBtn = makeButton("ADD", ACCENT_GREEN, BG_DARK);
            addBtn.setPreferredSize(new Dimension(90, 36));
            JButton removeBtn = makeButton("REMOVE", ACCENT_RED, BG_DARK);
            removeBtn.setPreferredSize(new Dimension(100, 36));
            JButton closeBtn = makeGhostButton("CLOSE");
            closeBtn.setPreferredSize(new Dimension(90, 36));

            JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
            btnRow.setOpaque(false);
            btnRow.add(addBtn);
            btnRow.add(removeBtn);
            btnRow.add(closeBtn);

            bottom.add(input, BorderLayout.CENTER);
            bottom.add(btnRow, BorderLayout.EAST);
            add(bottom, BorderLayout.SOUTH);

            addBtn.addActionListener(e -> {
                String val = input.getText().trim();
                if (val.isEmpty())
                    return;
                String tbl = mode.equals("SPEC") ? "DOCTOR_SPECIALIZATION" : "DOCTOR_QUALIFICATION";
                String fieldName = mode.equals("SPEC") ? "specialization" : "qualification";
                try {
                    PreparedStatement pst = dbConnection.prepareStatement(
                            "INSERT INTO " + tbl + " VALUES (?, ?)");
                    pst.setInt(1, doctorId);
                    pst.setString(2, val);
                    pst.executeUpdate();
                    pst.close();
                    input.setText("");
                    loadRows();
                } catch (SQLException ex) {
                    showError("Could not add: already exists?");
                }
            });

            removeBtn.addActionListener(e -> {
                int row = table.getSelectedRow();
                if (row < 0) {
                    showError("Select a row to remove.");
                    return;
                }
                String val = (String) model.getValueAt(row, 0);
                String tbl = mode.equals("SPEC") ? "DOCTOR_SPECIALIZATION" : "DOCTOR_QUALIFICATION";
                String fieldName = mode.equals("SPEC") ? "specialization" : "qualification";
                try {
                    PreparedStatement pst = dbConnection.prepareStatement(
                            "DELETE FROM " + tbl + " WHERE doctor_id = ? AND " + fieldName + " = ?");
                    pst.setInt(1, doctorId);
                    pst.setString(2, val);
                    pst.executeUpdate();
                    pst.close();
                    loadRows();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            });

            closeBtn.addActionListener(e -> dispose());

            setSize(440, 380);
            setLocationRelativeTo(parent);
        }

        private void loadRows() {
            model.setRowCount(0);
            String tbl = mode.equals("SPEC") ? "DOCTOR_SPECIALIZATION" : "DOCTOR_QUALIFICATION";
            String field = mode.equals("SPEC") ? "specialization" : "qualification";
            try {
                PreparedStatement pst = dbConnection.prepareStatement(
                        "SELECT " + field + " FROM " + tbl + " WHERE doctor_id = ?");
                pst.setInt(1, doctorId);
                ResultSet rs = pst.executeQuery();
                while (rs.next())
                    model.addRow(new Object[] { rs.getString(field) });
                rs.close();
                pst.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // ==================== MANAGE MEDICINES DIALOG ====================
    class ManageMedicinesDialog extends JDialog {
        private DefaultTableModel model;
        private final int patientId;

        ManageMedicinesDialog(JFrame parent, int patientId) {
            super(parent, "Medicines for Patient " + patientId, true);
            this.patientId = patientId;
            setBackground(BG_CARD);
            getContentPane().setBackground(BG_CARD);
            setLayout(new BorderLayout());

            JLabel header = new JLabel("  Medicines Assigned to Patient " + patientId);
            header.setFont(FONT_TITLE);
            header.setForeground(ACCENT_TEAL);
            header.setOpaque(true);
            header.setBackground(new Color(12, 18, 28));
            header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER_COLOR));
            header.setPreferredSize(new Dimension(0, 44));
            add(header, BorderLayout.NORTH);

            model = new DefaultTableModel(new String[] { "Code", "Price (PKR)", "Qty" }, 0) {
                public boolean isCellEditable(int r, int c) {
                    return false;
                }
            };
            JTable table = buildStyledTable(model);
            loadMedicines();

            JScrollPane scroll = new JScrollPane(table);
            scroll.getViewport().setBackground(BG_CARD);
            scroll.setBackground(BG_CARD);
            scroll.setBorder(BorderFactory.createEmptyBorder(8, 8, 4, 8));
            add(scroll, BorderLayout.CENTER);

            JPanel bottom = new JPanel(new BorderLayout());
            bottom.setBackground(new Color(12, 18, 28));
            bottom.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(1, 0, 0, 0, BORDER_COLOR),
                    BorderFactory.createEmptyBorder(10, 12, 10, 12)));

            JTextField codeInput = buildTextField();
            codeInput.setPreferredSize(new Dimension(0, 36));
            codeInput.setToolTipText("Medicine Code");

            JButton addBtn = makeButton("ADD", ACCENT_GREEN, BG_DARK);
            addBtn.setPreferredSize(new Dimension(90, 36));
            JButton removeBtn = makeButton("REMOVE", ACCENT_RED, BG_DARK);
            removeBtn.setPreferredSize(new Dimension(100, 36));
            JButton closeBtn = makeGhostButton("CLOSE");
            closeBtn.setPreferredSize(new Dimension(90, 36));

            JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
            btnRow.setOpaque(false);
            btnRow.add(addBtn);
            btnRow.add(removeBtn);
            btnRow.add(closeBtn);
            bottom.add(codeInput, BorderLayout.CENTER);
            bottom.add(btnRow, BorderLayout.EAST);
            add(bottom, BorderLayout.SOUTH);

            addBtn.addActionListener(e -> {
                try {
                    int code = Integer.parseInt(codeInput.getText().trim());
                    PreparedStatement pst = dbConnection.prepareStatement("INSERT INTO BILL VALUES (?, ?)");
                    pst.setInt(1, patientId);
                    pst.setInt(2, code);
                    pst.executeUpdate();
                    pst.close();
                    codeInput.setText("");
                    loadMedicines();
                    setStatus("Medicine " + code + " added to patient " + patientId, ACCENT_GREEN);
                } catch (NumberFormatException ex) {
                    showError("Invalid medicine code.");
                } catch (SQLException ex) {
                    showError("Error: code may not exist or already assigned.");
                }
            });

            removeBtn.addActionListener(e -> {
                int row = table.getSelectedRow();
                if (row < 0) {
                    showError("Select a row to remove.");
                    return;
                }
                int code = (int) model.getValueAt(row, 0);
                try {
                    PreparedStatement pst = dbConnection.prepareStatement(
                            "DELETE FROM BILL WHERE patient_id = ? AND medicine_code = ?");
                    pst.setInt(1, patientId);
                    pst.setInt(2, code);
                    pst.executeUpdate();
                    pst.close();
                    loadMedicines();
                    setStatus("Medicine " + code + " removed from patient " + patientId, TEXT_MUTED);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            });

            closeBtn.addActionListener(e -> dispose());

            setSize(520, 420);
            setLocationRelativeTo(parent);
        }

        private void loadMedicines() {
            model.setRowCount(0);
            try {
                PreparedStatement pst = dbConnection.prepareStatement(
                        "SELECT m.code, m.price, m.quantity FROM BILL b " +
                                "JOIN MEDICINE m ON b.medicine_code = m.code WHERE b.patient_id = ?");
                pst.setInt(1, patientId);
                ResultSet rs = pst.executeQuery();
                while (rs.next())
                    model.addRow(new Object[] { rs.getInt("code"), rs.getDouble("price"), rs.getInt("quantity") });
                rs.close();
                pst.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // ==================== MAIN ====================
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception ignored) {
        }

        UIManager.put("OptionPane.background", new Color(16, 22, 32));
        UIManager.put("Panel.background", new Color(16, 22, 32));
        UIManager.put("OptionPane.messageForeground", new Color(220, 228, 240));

        SwingUtilities.invokeLater(() -> new HospitalGUI().setVisible(true));
    }
}
