package models;

import db.OracleConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Doctor {

    private int doctorId;
    private String firstName;
    private String middleName;
    private String lastName;

    // Constructor

    public Doctor(
            int doctorId,
            String firstName,
            String middleName,
            String lastName) {

        this.doctorId = doctorId;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
    }

    // Getters

    public int getDoctorId() {
        return doctorId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public String getLastName() {
        return lastName;
    }

    // Setters

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    // Display Method

    public void displayDoctor() {

        System.out.println(
                "Doctor ID: " + doctorId);

        System.out.println(
                "First Name: " + firstName);

        System.out.println(
                "Middle Name: " + middleName);

        System.out.println(
                "Last Name: " + lastName);
    }

    // Insert Doctor

    public void insertDoctor() {

        String query = "INSERT INTO DOCTOR VALUES (?, ?, ?, ?)";

        try {

            Connection con = OracleConnection.getConnection();

            PreparedStatement pst = con.prepareStatement(query);

            pst.setInt(1, doctorId);
            pst.setString(2, firstName);
            pst.setString(3, middleName);
            pst.setString(4, lastName);

            pst.executeUpdate();

            System.out.println(
                    "Doctor Inserted Successfully");

            pst.close();
            con.close();
        }

        catch (SQLException e) {

            System.out.println(
                    "Insertion Failed");

            e.printStackTrace();
        }
    }

    // Show All Doctors

    public static void showAllDoctors() {

        String query = "SELECT * FROM DOCTOR";

        try {

            Connection con = OracleConnection.getConnection();

            PreparedStatement pst = con.prepareStatement(query);

            ResultSet rs = pst.executeQuery();

            while (rs.next()) {

                Doctor doctor = new Doctor(

                        rs.getInt("doctor_id"),

                        rs.getString("first_name"),

                        rs.getString("middle_name"),

                        rs.getString("last_name"));

                doctor.displayDoctor();

                System.out.println();
            }

            rs.close();
            pst.close();
            con.close();
        }

        catch (SQLException e) {

            System.out.println(
                    "Failed To Fetch Doctors");

            e.printStackTrace();
        }
    }

    // Update Doctor

    public void updateDoctor() {

        String query = "UPDATE DOCTOR " +
                "SET first_name = ?, " +
                "middle_name = ?, " +
                "last_name = ? " +
                "WHERE doctor_id = ?";

        try {

            Connection con = OracleConnection.getConnection();

            PreparedStatement pst = con.prepareStatement(query);

            pst.setString(1, firstName);
            pst.setString(2, middleName);
            pst.setString(3, lastName);
            pst.setInt(4, doctorId);

            int rowsUpdated = pst.executeUpdate();

            if (rowsUpdated > 0) {

                System.out.println(
                        "Doctor Updated Successfully");
            }

            else {

                System.out.println(
                        "Doctor Not Found");
            }

            pst.close();
            con.close();
        }

        catch (SQLException e) {

            System.out.println(
                    "Update Failed");

            e.printStackTrace();
        }
    }

    // Delete Doctor

    public void deleteDoctor() {

        String query = "DELETE FROM DOCTOR " +
                "WHERE doctor_id = ?";

        try {

            Connection con = OracleConnection.getConnection();

            PreparedStatement pst = con.prepareStatement(query);

            pst.setInt(1, doctorId);

            int rowsDeleted = pst.executeUpdate();

            if (rowsDeleted > 0) {

                System.out.println(
                        "Doctor Deleted Successfully");
            }

            else {

                System.out.println(
                        "Doctor Not Found");
            }

            pst.close();
            con.close();
        }

        catch (SQLException e) {

            System.out.println(
                    "Deletion Failed");

            e.printStackTrace();
        }
    }

    // Get Doctor By ID

    public static Doctor getDoctorById(
            int doctorId) {

        String query = "SELECT * FROM DOCTOR " +
                "WHERE doctor_id = ?";

        try {

            Connection con = OracleConnection.getConnection();

            PreparedStatement pst = con.prepareStatement(query);

            pst.setInt(1, doctorId);

            ResultSet rs = pst.executeQuery();

            if (rs.next()) {

                Doctor doctor = new Doctor(

                        rs.getInt("doctor_id"),

                        rs.getString("first_name"),

                        rs.getString("middle_name"),

                        rs.getString("last_name"));

                rs.close();
                pst.close();
                con.close();

                return doctor;
            }

            rs.close();
            pst.close();
            con.close();
        }

        catch (SQLException e) {

            System.out.println(
                    "Failed To Fetch Doctor");

            e.printStackTrace();
        }

        return null;
    }

    // Add Specialization

    public void addSpecialization(
            String specialization) {

        String query = "INSERT INTO DOCTOR_SPECIALIZATION " +
                "VALUES (?, ?)";

        try {

            Connection con = OracleConnection.getConnection();

            PreparedStatement pst = con.prepareStatement(query);

            pst.setInt(1, doctorId);
            pst.setString(2, specialization);

            pst.executeUpdate();

            System.out.println(
                    "Specialization Added");

            pst.close();
            con.close();
        }

        catch (SQLException e) {

            System.out.println(
                    "Failed To Add Specialization");

            e.printStackTrace();
        }
    }

    // Show Specializations

    public void showSpecializations() {

        String query = "SELECT specialization " +
                "FROM DOCTOR_SPECIALIZATION " +
                "WHERE doctor_id = ?";

        try {

            Connection con = OracleConnection.getConnection();

            PreparedStatement pst = con.prepareStatement(query);

            pst.setInt(1, doctorId);

            ResultSet rs = pst.executeQuery();

            System.out.println(
                    "Specializations:");

            while (rs.next()) {

                System.out.println(
                        rs.getString("specialization"));
            }

            rs.close();
            pst.close();
            con.close();
        }

        catch (SQLException e) {

            System.out.println(
                    "Failed To Fetch Specializations");

            e.printStackTrace();
        }
    }

    // Remove Specialization

    public void removeSpecialization(
            String specialization) {

        String query = "DELETE FROM DOCTOR_SPECIALIZATION " +
                "WHERE doctor_id = ? " +
                "AND specialization = ?";

        try {

            Connection con = OracleConnection.getConnection();

            PreparedStatement pst = con.prepareStatement(query);

            pst.setInt(1, doctorId);
            pst.setString(2, specialization);

            int rowsDeleted = pst.executeUpdate();

            if (rowsDeleted > 0) {

                System.out.println(
                        "Specialization Removed");
            }

            else {

                System.out.println(
                        "Specialization Not Found");
            }

            pst.close();
            con.close();
        }

        catch (SQLException e) {

            System.out.println(
                    "Failed To Remove Specialization");

            e.printStackTrace();
        }
    }

    // Add Qualification

    public void addQualification(
            String qualification) {

        String query = "INSERT INTO DOCTOR_QUALIFICATION " +
                "VALUES (?, ?)";

        try {

            Connection con = OracleConnection.getConnection();

            PreparedStatement pst = con.prepareStatement(query);

            pst.setInt(1, doctorId);
            pst.setString(2, qualification);

            pst.executeUpdate();

            System.out.println(
                    "Qualification Added");

            pst.close();
            con.close();
        }

        catch (SQLException e) {

            System.out.println(
                    "Failed To Add Qualification");

            e.printStackTrace();
        }
    }

    // Show Qualifications

    public void showQualifications() {

        String query = "SELECT qualification " +
                "FROM DOCTOR_QUALIFICATION " +
                "WHERE doctor_id = ?";

        try {

            Connection con = OracleConnection.getConnection();

            PreparedStatement pst = con.prepareStatement(query);

            pst.setInt(1, doctorId);

            ResultSet rs = pst.executeQuery();

            System.out.println(
                    "Qualifications:");

            while (rs.next()) {

                System.out.println(
                        rs.getString("qualification"));
            }

            rs.close();
            pst.close();
            con.close();
        }

        catch (SQLException e) {

            System.out.println(
                    "Failed To Fetch Qualifications");

            e.printStackTrace();
        }
    }

    // Remove Qualification

    public void removeQualification(
            String qualification) {

        String query = "DELETE FROM DOCTOR_QUALIFICATION " +
                "WHERE doctor_id = ? " +
                "AND qualification = ?";

        try {

            Connection con = OracleConnection.getConnection();

            PreparedStatement pst = con.prepareStatement(query);

            pst.setInt(1, doctorId);
            pst.setString(2, qualification);

            int rowsDeleted = pst.executeUpdate();

            if (rowsDeleted > 0) {

                System.out.println(
                        "Qualification Removed");
            }

            else {

                System.out.println(
                        "Qualification Not Found");
            }

            pst.close();
            con.close();
        }

        catch (SQLException e) {

            System.out.println(
                    "Failed To Remove Qualification");

            e.printStackTrace();
        }
    }

    // Assign Patient

    public void assignPatient(
            int patientId) {

        String query = "INSERT INTO TREATS VALUES (?, ?)";

        try {

            Connection con = OracleConnection.getConnection();

            PreparedStatement pst = con.prepareStatement(query);

            pst.setInt(1, doctorId);
            pst.setInt(2, patientId);

            pst.executeUpdate();

            System.out.println(
                    "Patient Assigned Successfully");

            pst.close();
            con.close();
        }

        catch (SQLException e) {

            System.out.println(
                    "Failed To Assign Patient");

            e.printStackTrace();
        }
    }

    // Show Patients

    public void showPatients() {

        String query = "SELECT p.* " +
                "FROM PATIENT p " +
                "JOIN TREATS t " +
                "ON p.patient_id = t.patient_id " +
                "WHERE t.doctor_id = ?";

        try {

            Connection con = OracleConnection.getConnection();

            PreparedStatement pst = con.prepareStatement(query);

            pst.setInt(1, doctorId);

            ResultSet rs = pst.executeQuery();

            while (rs.next()) {

                System.out.println(
                        "Patient ID: " +
                                rs.getInt("patient_id"));

                System.out.println(
                        "Age: " +
                                rs.getInt("age"));

                System.out.println(
                        "DOB: " +
                                rs.getString("dob"));

                System.out.println(
                        "Locality: " +
                                rs.getString("locality"));

                System.out.println(
                        "Town/City: " +
                                rs.getString("town_city"));

                System.out.println();
            }

            rs.close();
            pst.close();
            con.close();
        }

        catch (SQLException e) {

            System.out.println(
                    "Failed To Fetch Patients");

            e.printStackTrace();
        }
    }
}