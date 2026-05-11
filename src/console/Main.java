package console;

import models.Doctor;
import models.Patient;
import models.Medicine;

import java.util.ArrayList;
import java.util.List;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import db.OracleConnection;

public class Main {

    private static Connection dbConnection;

    public static void main(String[] args) {

        // Initialize database connection
        try {
            dbConnection = OracleConnection.getConnection();
            System.out.println("+ MEDCORE - Connected to Oracle Database");
        } catch (Exception e) {
            System.err.println("+ MEDCORE - Database connection failed: " + e.getMessage());
            System.exit(1);
        }

        Scanner input = new Scanner(System.in);

        int choice;

        do {

            System.out.println();
            System.out.println(
                    "   + MEDCORE   ");

            System.out.println("1. Doctor Menu");
            System.out.println("2. Patient Menu");
            System.out.println("3. Medicine Menu");
            System.out.println("4. Custom Query Runner");
            System.out.println("0. Exit");

            System.out.print("Enter Choice: ");

            choice = input.nextInt();

            switch (choice) {

                case 1:
                    doctorMenu(input);
                    break;

                case 2:
                    patientMenu(input);
                    break;

                case 3:
                    medicineMenu(input);
                    break;

                case 4:
                    customQueryRunner(input);
                    break;

                case 0:
                    System.out.println("+ MEDCORE - Exiting Program...");
                    break;

                default:
                    System.out.println("Invalid Choice");
            }

        } while (choice != 0);

        try {
            if (dbConnection != null && !dbConnection.isClosed()) {
                dbConnection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        input.close();
    }

    // Doctor Menu

    public static void doctorMenu(Scanner input) {

        int choice;

        do {

            System.out.println();
            System.out.println(
                    "   + MEDCORE - DOCTOR MENU   ");

            System.out.println("1. Insert Doctor");
            System.out.println("2. Show All Doctors");
            System.out.println("3. Update Doctor");
            System.out.println("4. Delete Doctor");
            System.out.println("5. Find Doctor By ID");

            System.out.println("6. Add Specialization");
            System.out.println("7. Show Specializations");
            System.out.println("8. Remove Specialization");

            System.out.println("9. Add Qualification");
            System.out.println("10. Show Qualifications");
            System.out.println("11. Remove Qualification");

            System.out.println("12. Assign Patient");
            System.out.println("13. Show Assigned Patients");

            System.out.println("0. Back");

            System.out.print("Enter Choice: ");

            choice = input.nextInt();
            input.nextLine();

            switch (choice) {

                case 1:

                    System.out.print("Doctor ID: ");
                    int doctorId = input.nextInt();
                    input.nextLine();

                    System.out.print("First Name: ");
                    String firstName = input.nextLine();

                    System.out.print("Middle Name: ");
                    String middleName = input.nextLine();

                    System.out.print("Last Name: ");
                    String lastName = input.nextLine();

                    Doctor doctor = new Doctor(
                            doctorId,
                            firstName,
                            middleName,
                            lastName);

                    doctor.insertDoctor();

                    break;

                case 2:

                    Doctor.showAllDoctors();

                    break;

                case 3:

                    System.out.print("Doctor ID: ");
                    int updateId = input.nextInt();
                    input.nextLine();

                    System.out.print("New First Name: ");
                    String updateFirst = input.nextLine();

                    System.out.print("New Middle Name: ");
                    String updateMiddle = input.nextLine();

                    System.out.print("New Last Name: ");
                    String updateLast = input.nextLine();

                    Doctor updatedDoctor = new Doctor(
                            updateId,
                            updateFirst,
                            updateMiddle,
                            updateLast);

                    updatedDoctor.updateDoctor();

                    break;

                case 4:

                    System.out.print("Doctor ID: ");
                    int deleteId = input.nextInt();

                    // Show warning about linked records
                    try {
                        PreparedStatement p1 = dbConnection
                                .prepareStatement("SELECT COUNT(*) FROM TREATS WHERE doctor_id = ?");
                        p1.setInt(1, deleteId);
                        ResultSet r1 = p1.executeQuery();
                        r1.next();
                        int patients = r1.getInt(1);
                        r1.close();
                        p1.close();

                        PreparedStatement p2 = dbConnection
                                .prepareStatement("SELECT COUNT(*) FROM DOCTOR_SPECIALIZATION WHERE doctor_id = ?");
                        p2.setInt(1, deleteId);
                        ResultSet r2 = p2.executeQuery();
                        r2.next();
                        int specs = r2.getInt(1);
                        r2.close();
                        p2.close();

                        PreparedStatement p3 = dbConnection
                                .prepareStatement("SELECT COUNT(*) FROM DOCTOR_QUALIFICATION WHERE doctor_id = ?");
                        p3.setInt(1, deleteId);
                        ResultSet r3 = p3.executeQuery();
                        r3.next();
                        int quals = r3.getInt(1);
                        r3.close();
                        p3.close();

                        if (patients > 0 || specs > 0 || quals > 0) {
                            System.out.println("\nWARNING: This doctor has linked records:");
                            if (patients > 0)
                                System.out.println("  - " + patients + " patient(s) assigned");
                            if (specs > 0)
                                System.out.println("  - " + specs + " specialization(s)");
                            if (quals > 0)
                                System.out.println("  - " + quals + " qualification(s)");
                            System.out.print("\nDelete anyway? (y/n): ");
                            String confirm = input.next();
                            if (!confirm.equalsIgnoreCase("y")) {
                                System.out.println("Delete cancelled.");
                                break;
                            }
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    Doctor deleteDoctor = new Doctor(
                            deleteId,
                            "",
                            "",
                            "");

                    deleteDoctor.deleteDoctor();

                    break;

                case 5:

                    System.out.print("Doctor ID: ");
                    int searchId = input.nextInt();

                    Doctor foundDoctor = Doctor.getDoctorById(searchId);

                    if (foundDoctor != null) {

                        foundDoctor.displayDoctor();
                    }

                    else {

                        System.out.println(
                                "Doctor Not Found");
                    }

                    break;

                case 6:

                    System.out.print("Doctor ID: ");
                    int specDoctorId = input.nextInt();
                    input.nextLine();

                    System.out.print("Specialization: ");
                    String specialization = input.nextLine();

                    Doctor specDoctor = Doctor.getDoctorById(
                            specDoctorId);

                    if (specDoctor != null) {

                        specDoctor.addSpecialization(
                                specialization);
                    }

                    else {

                        System.out.println(
                                "Doctor Not Found");
                    }

                    break;

                case 7:

                    System.out.print("Doctor ID: ");
                    int showSpecId = input.nextInt();

                    Doctor showSpecDoctor = Doctor.getDoctorById(
                            showSpecId);

                    if (showSpecDoctor != null) {

                        showSpecDoctor.showSpecializations();
                    }

                    else {

                        System.out.println(
                                "Doctor Not Found");
                    }

                    break;

                case 8:

                    System.out.print("Doctor ID: ");
                    int removeSpecId = input.nextInt();
                    input.nextLine();

                    System.out.print(
                            "Specialization To Remove: ");

                    String removeSpec = input.nextLine();

                    Doctor removeSpecDoctor = Doctor.getDoctorById(
                            removeSpecId);

                    if (removeSpecDoctor != null) {

                        removeSpecDoctor.removeSpecialization(
                                removeSpec);
                    }

                    else {

                        System.out.println(
                                "Doctor Not Found");
                    }

                    break;

                case 9:

                    System.out.print("Doctor ID: ");
                    int qualDoctorId = input.nextInt();
                    input.nextLine();

                    System.out.print("Qualification: ");
                    String qualification = input.nextLine();

                    Doctor qualDoctor = Doctor.getDoctorById(
                            qualDoctorId);

                    if (qualDoctor != null) {

                        qualDoctor.addQualification(
                                qualification);
                    }

                    else {

                        System.out.println(
                                "Doctor Not Found");
                    }

                    break;

                case 10:

                    System.out.print("Doctor ID: ");
                    int showQualId = input.nextInt();

                    Doctor showQualDoctor = Doctor.getDoctorById(
                            showQualId);

                    if (showQualDoctor != null) {

                        showQualDoctor.showQualifications();
                    }

                    else {

                        System.out.println(
                                "Doctor Not Found");
                    }

                    break;

                case 11:

                    System.out.print("Doctor ID: ");
                    int removeQualId = input.nextInt();
                    input.nextLine();

                    System.out.print(
                            "Qualification To Remove: ");

                    String removeQual = input.nextLine();

                    Doctor removeQualDoctor = Doctor.getDoctorById(
                            removeQualId);

                    if (removeQualDoctor != null) {

                        removeQualDoctor.removeQualification(
                                removeQual);
                    }

                    else {

                        System.out.println(
                                "Doctor Not Found");
                    }

                    break;

                case 12:

                    System.out.print("Doctor ID: ");
                    int assignDoctorId = input.nextInt();

                    System.out.print("Patient ID: ");
                    int assignPatientId = input.nextInt();

                    Doctor assignDoctor = Doctor.getDoctorById(
                            assignDoctorId);

                    if (assignDoctor != null) {

                        assignDoctor.assignPatient(
                                assignPatientId);
                    }

                    else {

                        System.out.println(
                                "Doctor Not Found");
                    }

                    break;

                case 13:

                    System.out.print("Doctor ID: ");
                    int showPatientDoctorId = input.nextInt();

                    Doctor patientDoctor = Doctor.getDoctorById(
                            showPatientDoctorId);

                    if (patientDoctor != null) {

                        patientDoctor.showPatients();
                    }

                    else {

                        System.out.println(
                                "Doctor Not Found");
                    }

                    break;

                case 0:

                    break;

                default:

                    System.out.println(
                            "Invalid Choice");
            }

        } while (choice != 0);
    }

    // Patient Menu

    public static void patientMenu(Scanner input) {

        int choice;

        do {

            System.out.println();
            System.out.println(
                    "   + MEDCORE - PATIENT MENU   ");

            System.out.println("1. Insert Patient");
            System.out.println("2. Show All Patients");
            System.out.println("3. Update Patient");
            System.out.println("4. Delete Patient");
            System.out.println("5. Find Patient By ID");

            System.out.println("6. Add Medicine");
            System.out.println("7. Remove Medicine");

            System.out.println("0. Back");

            System.out.print("Enter Choice: ");

            choice = input.nextInt();
            input.nextLine();

            switch (choice) {

                case 1:

                    System.out.print("Patient ID: ");
                    int patientId = input.nextInt();
                    input.nextLine();

                    // Age is derived - not required as input
                    System.out.print("DOB (YYYY-MM-DD): ");
                    String dob = input.nextLine();

                    System.out.print("Locality: ");
                    String locality = input.nextLine();

                    System.out.print("Town/City: ");
                    String townCity = input.nextLine();

                    Patient patient = new Patient(
                            patientId,
                            0, // Age will be computed automatically in insertPatient
                            dob,
                            locality,
                            townCity);

                    patient.insertPatient();

                    break;

                case 2:

                    Patient.showAllPatients();

                    break;

                case 3:

                    System.out.print("Patient ID: ");
                    int updateId = input.nextInt();
                    input.nextLine();

                    System.out.print("New DOB (YYYY-MM-DD): ");
                    String updateDob = input.nextLine();

                    System.out.print("New Locality: ");
                    String updateLocality = input.nextLine();

                    System.out.print("New Town/City: ");
                    String updateTownCity = input.nextLine();

                    Patient updatedPatient = new Patient(
                            updateId,
                            0, // Age will be computed automatically
                            updateDob,
                            updateLocality,
                            updateTownCity);

                    updatedPatient.updatePatient();

                    break;

                case 4:

                    System.out.print("Patient ID: ");
                    int deleteId = input.nextInt();

                    // Check if patient has linked records
                    try {
                        PreparedStatement p1 = dbConnection
                                .prepareStatement("SELECT COUNT(*) FROM TREATS WHERE patient_id = ?");
                        p1.setInt(1, deleteId);
                        ResultSet r1 = p1.executeQuery();
                        r1.next();
                        int doctors = r1.getInt(1);
                        r1.close();
                        p1.close();

                        PreparedStatement p2 = dbConnection
                                .prepareStatement("SELECT COUNT(*) FROM BILL WHERE patient_id = ?");
                        p2.setInt(1, deleteId);
                        ResultSet r2 = p2.executeQuery();
                        r2.next();
                        int medicines = r2.getInt(1);
                        r2.close();
                        p2.close();

                        if (doctors > 0 || medicines > 0) {
                            System.out.println("\nWARNING: This patient has linked records:");
                            if (doctors > 0)
                                System.out.println("  - " + doctors + " doctor(s) assigned");
                            if (medicines > 0)
                                System.out.println("  - " + medicines + " medicine(s) prescribed");
                            System.out.print("\nDelete anyway? (y/n): ");
                            String confirm = input.next();
                            if (!confirm.equalsIgnoreCase("y")) {
                                System.out.println("Delete cancelled.");
                                break;
                            }
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    Patient deletePatient = new Patient(
                            deleteId,
                            0,
                            "",
                            "",
                            "");

                    deletePatient.deletePatient();

                    break;

                case 5:

                    System.out.print("Patient ID: ");
                    int searchId = input.nextInt();

                    Patient foundPatient = Patient.getPatientById(
                            searchId);

                    if (foundPatient != null) {

                        foundPatient.displayPatient();
                    }

                    else {

                        System.out.println(
                                "Patient Not Found");
                    }

                    break;

                case 6:

                    System.out.print("Patient ID: ");
                    int billPatientId = input.nextInt();

                    System.out.print(
                            "Medicine Code: ");

                    int medicineCode = input.nextInt();

                    Patient billPatient = Patient.getPatientById(
                            billPatientId);

                    if (billPatient != null) {

                        billPatient.addMedicine(
                                medicineCode);
                    }

                    else {

                        System.out.println(
                                "Patient Not Found");
                    }

                    break;

                case 7:

                    System.out.print("Patient ID: ");
                    int removePatientId = input.nextInt();

                    System.out.print(
                            "Medicine Code: ");

                    int removeMedicineCode = input.nextInt();

                    Patient removeMedicinePatient = Patient.getPatientById(
                            removePatientId);

                    if (removeMedicinePatient != null) {

                        removeMedicinePatient.removeMedicine(
                                removeMedicineCode);
                    }

                    else {

                        System.out.println(
                                "Patient Not Found");
                    }

                    break;

                case 0:

                    break;

                default:

                    System.out.println(
                            "Invalid Choice");
            }

        } while (choice != 0);
    }

    // Medicine Menu

    public static void medicineMenu(Scanner input) {

        int choice;

        do {

            System.out.println();
            System.out.println(
                    "   + MEDCORE - MEDICINE MENU   ");

            System.out.println("1. Insert Medicine");
            System.out.println("2. Show All Medicines");
            System.out.println("3. Update Medicine");
            System.out.println("4. Delete Medicine");
            System.out.println("5. Find Medicine By Code");

            System.out.println("0. Back");

            System.out.print("Enter Choice: ");

            choice = input.nextInt();

            switch (choice) {

                case 1:

                    System.out.print(
                            "Medicine Code: ");

                    int code = input.nextInt();

                    System.out.print("Price: ");
                    double price = input.nextDouble();

                    System.out.print("Quantity: ");
                    int quantity = input.nextInt();

                    Medicine medicine = new Medicine(
                            code,
                            price,
                            quantity);

                    medicine.insertMedicine();

                    break;

                case 2:

                    Medicine.showAllMedicines();

                    break;

                case 3:

                    System.out.print(
                            "Medicine Code: ");

                    int updateCode = input.nextInt();

                    System.out.print(
                            "New Price: ");

                    double updatePrice = input.nextDouble();

                    System.out.print(
                            "New Quantity: ");

                    int updateQuantity = input.nextInt();

                    Medicine updatedMedicine = new Medicine(
                            updateCode,
                            updatePrice,
                            updateQuantity);

                    updatedMedicine.updateMedicine();

                    break;

                case 4:

                    System.out.print(
                            "Medicine Code: ");

                    int deleteCode = input.nextInt();

                    // Check if medicine is assigned to any patient
                    try {
                        PreparedStatement pst = dbConnection
                                .prepareStatement("SELECT COUNT(*) FROM BILL WHERE medicine_code = ?");
                        pst.setInt(1, deleteCode);
                        ResultSet rs = pst.executeQuery();
                        rs.next();
                        int patients = rs.getInt(1);
                        rs.close();
                        pst.close();

                        if (patients > 0) {
                            System.out
                                    .println("\nWARNING: This medicine is prescribed to " + patients + " patient(s).");
                            System.out.print("Delete anyway? (y/n): ");
                            String confirm = input.next();
                            if (!confirm.equalsIgnoreCase("y")) {
                                System.out.println("Delete cancelled.");
                                break;
                            }
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    Medicine deleteMedicine = new Medicine(
                            deleteCode,
                            0,
                            0);

                    deleteMedicine.deleteMedicine();

                    break;

                case 5:

                    System.out.print(
                            "Medicine Code: ");

                    int searchCode = input.nextInt();

                    Medicine foundMedicine = Medicine.getMedicineByCode(
                            searchCode);

                    if (foundMedicine != null) {

                        foundMedicine.displayMedicine();
                    }

                    else {

                        System.out.println(
                                "Medicine Not Found");
                    }

                    break;

                case 0:

                    break;

                default:

                    System.out.println(
                            "Invalid Choice");
            }

        } while (choice != 0);
    }

    // Custom Query Runner - Allows users to enter any SQL query

    // Custom Query Runner - Allows users to enter any SQL query

    public static void customQueryRunner(Scanner input) {

        System.out.println();
        System.out.println("   + MEDCORE - CUSTOM QUERY RUNNER   ");
        System.out.println("Enter any SQL query (SELECT, INSERT, UPDATE, DELETE)");
        System.out.println("End your query with a semicolon (;) on a new line");
        System.out.println("Type 'exit' to return to main menu");
        System.out.println("--------------------------------------------------");

        input.nextLine(); // Consume newline

        while (true) {
            System.out.print("\nSQL> ");

            StringBuilder queryBuilder = new StringBuilder();
            String line;

            // Read multi-line query until semicolon or exit
            while (true) {
                line = input.nextLine();

                if (line.equalsIgnoreCase("exit")) {
                    return;
                }

                queryBuilder.append(line).append(" ");

                // Check if line ends with semicolon
                if (line.trim().endsWith(";")) {
                    break;
                }
            }

            String sql = queryBuilder.toString().trim();

            // Remove trailing semicolon
            if (sql.endsWith(";")) {
                sql = sql.substring(0, sql.length() - 1);
            }

            if (sql.isEmpty()) {
                continue;
            }

            try {
                Statement stmt = dbConnection.createStatement();
                boolean hasResultSet = stmt.execute(sql);

                if (hasResultSet) {
                    ResultSet rs = stmt.getResultSet();
                    ResultSetMetaData meta = rs.getMetaData();
                    int columnCount = meta.getColumnCount();

                    // Calculate column widths dynamically
                    int[] columnWidths = new int[columnCount];
                    String[] columnNames = new String[columnCount];

                    for (int i = 1; i <= columnCount; i++) {
                        columnNames[i - 1] = meta.getColumnName(i);
                        columnWidths[i - 1] = Math.max(columnNames[i - 1].length(), 10);
                    }

                    // First pass to get max data lengths
                    List<String[]> rows = new ArrayList<>();
                    while (rs.next()) {
                        String[] row = new String[columnCount];
                        for (int i = 1; i <= columnCount; i++) {
                            String value = rs.getString(i);
                            if (value == null)
                                value = "NULL";
                            row[i - 1] = value;
                            columnWidths[i - 1] = Math.max(columnWidths[i - 1], Math.min(value.length(), 30));
                        }
                        rows.add(row);
                    }

                    // Print separator line
                    System.out.println();
                    StringBuilder separator = new StringBuilder("+");
                    for (int width : columnWidths) {
                        separator.append("-".repeat(width + 2)).append("+");
                    }
                    System.out.println(separator);

                    // Print headers
                    System.out.print("|");
                    for (int i = 0; i < columnCount; i++) {
                        System.out.printf(" %-" + columnWidths[i] + "s |", columnNames[i]);
                    }
                    System.out.println();
                    System.out.println(separator);

                    // Print rows
                    for (String[] row : rows) {
                        System.out.print("|");
                        for (int i = 0; i < columnCount; i++) {
                            String value = row[i];
                            if (value.length() > columnWidths[i]) {
                                value = value.substring(0, columnWidths[i] - 3) + "...";
                            }
                            System.out.printf(" %-" + columnWidths[i] + "s |", value);
                        }
                        System.out.println();
                    }
                    System.out.println(separator);
                    System.out.println(rows.size() + " row(s) returned.");
                    rs.close();
                } else {
                    int updateCount = stmt.getUpdateCount();
                    System.out.println("Query executed. " + updateCount + " row(s) affected.");
                }
                stmt.close();
            } catch (SQLException e) {
                System.out.println("ERROR: " + e.getMessage());
            }
        }
    }
}