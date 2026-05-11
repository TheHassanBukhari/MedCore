package models;

import db.OracleConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Medicine {

    private int code;
    private double price;
    private int quantity;


    // Constructor

    public Medicine(
            int code,
            double price,
            int quantity
    ) {

        this.code = code;
        this.price = price;
        this.quantity = quantity;
    }


    // Getters

    public int getCode() {
        return code;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }


    // Setters

    public void setCode(int code) {
        this.code = code;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }


    // Display Method

    public void displayMedicine() {

        System.out.println(
                "Medicine Code: " + code
        );

        System.out.println(
                "Price: " + price
        );

        System.out.println(
                "Quantity: " + quantity
        );
    }


    // Insert Medicine

    public void insertMedicine() {

        String query =
                "INSERT INTO MEDICINE " +
                "VALUES (?, ?, ?)";

        try {

            Connection con =
                    OracleConnection.getConnection();

            PreparedStatement pst =
                    con.prepareStatement(query);

            pst.setInt(1, code);
            pst.setDouble(2, price);
            pst.setInt(3, quantity);

            pst.executeUpdate();

            System.out.println(
                    "Medicine Inserted Successfully"
            );

            pst.close();
            con.close();
        }

        catch (SQLException e) {

            System.out.println(
                    "Insertion Failed"
            );

            e.printStackTrace();
        }
    }


    // Show All Medicines

    public static void showAllMedicines() {

        String query =
                "SELECT * FROM MEDICINE";

        try {

            Connection con =
                    OracleConnection.getConnection();

            PreparedStatement pst =
                    con.prepareStatement(query);

            ResultSet rs =
                    pst.executeQuery();

            while (rs.next()) {

                Medicine medicine =
                        new Medicine(

                                rs.getInt("code"),

                                rs.getDouble("price"),

                                rs.getInt("quantity")
                        );

                medicine.displayMedicine();

                System.out.println();
            }

            rs.close();
            pst.close();
            con.close();
        }

        catch (SQLException e) {

            System.out.println(
                    "Failed To Fetch Medicines"
            );

            e.printStackTrace();
        }
    }


    // Update Medicine

    public void updateMedicine() {

        String query =
                "UPDATE MEDICINE " +
                "SET price = ?, " +
                "quantity = ? " +
                "WHERE code = ?";

        try {

            Connection con =
                    OracleConnection.getConnection();

            PreparedStatement pst =
                    con.prepareStatement(query);

            pst.setDouble(1, price);
            pst.setInt(2, quantity);
            pst.setInt(3, code);

            int rowsUpdated =
                    pst.executeUpdate();

            if (rowsUpdated > 0) {

                System.out.println(
                        "Medicine Updated Successfully"
                );
            }

            else {

                System.out.println(
                        "Medicine Not Found"
                );
            }

            pst.close();
            con.close();
        }

        catch (SQLException e) {

            System.out.println(
                    "Update Failed"
            );

            e.printStackTrace();
        }
    }


    // Delete Medicine

    public void deleteMedicine() {

        String query =
                "DELETE FROM MEDICINE " +
                "WHERE code = ?";

        try {

            Connection con =
                    OracleConnection.getConnection();

            PreparedStatement pst =
                    con.prepareStatement(query);

            pst.setInt(1, code);

            int rowsDeleted =
                    pst.executeUpdate();

            if (rowsDeleted > 0) {

                System.out.println(
                        "Medicine Deleted Successfully"
                );
            }

            else {

                System.out.println(
                        "Medicine Not Found"
                );
            }

            pst.close();
            con.close();
        }

        catch (SQLException e) {

            System.out.println(
                    "Deletion Failed"
            );

            e.printStackTrace();
        }
    }


    // Get Medicine By Code

    public static Medicine getMedicineByCode(
            int code
    ) {

        String query =
                "SELECT * FROM MEDICINE " +
                "WHERE code = ?";

        try {

            Connection con =
                    OracleConnection.getConnection();

            PreparedStatement pst =
                    con.prepareStatement(query);

            pst.setInt(1, code);

            ResultSet rs =
                    pst.executeQuery();

            if (rs.next()) {

                Medicine medicine =
                        new Medicine(

                                rs.getInt("code"),

                                rs.getDouble("price"),

                                rs.getInt("quantity")
                        );

                rs.close();
                pst.close();
                con.close();

                return medicine;
            }

            rs.close();
            pst.close();
            con.close();
        }

        catch (SQLException e) {

            System.out.println(
                    "Failed To Fetch Medicine"
            );

            e.printStackTrace();
        }

        return null;
    }
}