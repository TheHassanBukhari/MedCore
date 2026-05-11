package models;

import db.OracleConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Patient {

    private int patientId;
    private int age;
    private String dob;
    private String locality;
    private String townCity;

    // Constructor

    public Patient(
            int patientId,
            int age,
            String dob,
            String locality,
            String townCity) {

        this.patientId = patientId;
        this.age = age;
        this.dob = dob;
        this.locality = locality;
        this.townCity = townCity;
    }

    // Getters

    public int getPatientId() {
        return patientId;
    }

    public int getAge() {
        return age;
    }

    public String getDob() {
        return dob;
    }

    public String getLocality() {
        return locality;
    }

    public String getTownCity() {
        return townCity;
    }

    // Setters

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public void setTownCity(String townCity) {
        this.townCity = townCity;
    }

    // Display Method

    public void displayPatient() {

        System.out.println(
                "Patient ID: " + patientId);

        System.out.println(
                "Age: " + age);

        System.out.println(
                "DOB: " + dob);

        System.out.println(
                "Locality: " + locality);

        System.out.println(
                "Town/City: " + townCity);
    }

    // Insert Patient

public void insertPatient() {

    String query = "INSERT INTO PATIENT " +
            "VALUES (?, ?, TO_DATE(?, 'YYYY-MM-DD'), ?, ?)";

    try {

        // compute age from dob (YYYY-MM-DD)
        java.time.LocalDate birthDate = java.time.LocalDate.parse(dob);
        int computedAge = java.time.Period.between(birthDate, java.time.LocalDate.now()).getYears();

        Connection con = OracleConnection.getConnection();
        PreparedStatement pst = con.prepareStatement(query);

        pst.setInt(1, patientId);
        pst.setInt(2, computedAge);
        pst.setString(3, dob);
        pst.setString(4, locality);
        pst.setString(5, townCity);

        pst.executeUpdate();

        System.out.println("Patient Inserted Successfully");

        pst.close();
        con.close();
    }

    catch (SQLException e) {

        System.out.println("Insertion Failed");
        e.printStackTrace();
    }
}

    // Show All Patients

    public static void showAllPatients() {

        String query = "SELECT * FROM PATIENT";

        try {

            Connection con = OracleConnection.getConnection();

            PreparedStatement pst = con.prepareStatement(query);

            ResultSet rs = pst.executeQuery();

            while (rs.next()) {

                Patient patient = new Patient(

                        rs.getInt("patient_id"),

                        rs.getInt("age"),

                        rs.getString("dob"),

                        rs.getString("locality"),

                        rs.getString("town_city"));

                patient.displayPatient();

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

    // Update Patient

    public void updatePatient() {

        String query = "UPDATE PATIENT " +
                "SET age = ?, " +
                "dob = TO_DATE(?, 'YYYY-MM-DD'), " +
                "locality = ?, " +
                "town_city = ? " +
                "WHERE patient_id = ?";

        try {

            Connection con = OracleConnection.getConnection();

            PreparedStatement pst = con.prepareStatement(query);

            pst.setInt(1, age);
            pst.setString(2, dob);
            pst.setString(3, locality);
            pst.setString(4, townCity);
            pst.setInt(5, patientId);

            int rowsUpdated = pst.executeUpdate();

            if (rowsUpdated > 0) {

                System.out.println(
                        "Patient Updated Successfully");
            }

            else {

                System.out.println(
                        "Patient Not Found");
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

    // Delete Patient

    public void deletePatient() {

        String query = "DELETE FROM PATIENT " +
                "WHERE patient_id = ?";

        try {

            Connection con = OracleConnection.getConnection();

            PreparedStatement pst = con.prepareStatement(query);

            pst.setInt(1, patientId);

            int rowsDeleted = pst.executeUpdate();

            if (rowsDeleted > 0) {

                System.out.println(
                        "Patient Deleted Successfully");
            }

            else {

                System.out.println(
                        "Patient Not Found");
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

    // Get Patient By ID

    public static Patient getPatientById(
            int patientId) {

        String query = "SELECT * FROM PATIENT " +
                "WHERE patient_id = ?";

        try {

            Connection con = OracleConnection.getConnection();

            PreparedStatement pst = con.prepareStatement(query);

            pst.setInt(1, patientId);

            ResultSet rs = pst.executeQuery();

            if (rs.next()) {

                Patient patient = new Patient(

                        rs.getInt("patient_id"),

                        rs.getInt("age"),

                        rs.getString("dob"),

                        rs.getString("locality"),

                        rs.getString("town_city"));

                rs.close();
                pst.close();
                con.close();

                return patient;
            }

            rs.close();
            pst.close();
            con.close();
        }

        catch (SQLException e) {

            System.out.println(
                    "Failed To Fetch Patient");

            e.printStackTrace();
        }

        return null;
    }

    // add Medicine

    public void addMedicine(int medicineCode) {

        String query = "INSERT INTO BILL VALUES (?, ?)";

        try {

            Connection con = OracleConnection.getConnection();

            PreparedStatement pst = con.prepareStatement(query);

            pst.setInt(1, patientId);
            pst.setInt(2, medicineCode);

            pst.executeUpdate();

            System.out.println(
                    "Medicine Added To Patient");

            pst.close();
            con.close();
        }

        catch (SQLException e) {

            System.out.println(
                    "Failed To Add Medicine");

            e.printStackTrace();
        }
    }

    // remove Medicine

    public void removeMedicine(int medicineCode) {

        String query = "DELETE FROM BILL " +
                "WHERE patient_id = ? " +
                "AND medicine_code = ?";

        try {

            Connection con = OracleConnection.getConnection();

            PreparedStatement pst = con.prepareStatement(query);

            pst.setInt(1, patientId);
            pst.setInt(2, medicineCode);

            int rowsDeleted = pst.executeUpdate();

            if (rowsDeleted > 0) {

                System.out.println(
                        "Medicine Removed Successfully");
            }

            else {

                System.out.println(
                        "Medicine Relation Not Found");
            }

            pst.close();
            con.close();
        }

        catch (SQLException e) {

            System.out.println(
                    "Failed To Remove Medicine");

            e.printStackTrace();
        }
    }
}