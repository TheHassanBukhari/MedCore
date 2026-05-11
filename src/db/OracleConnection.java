package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class OracleConnection {

    private static final String URL =
            "jdbc:oracle:thin:@localhost:1521/XEPDB1";

    private static final String USERNAME =
            "hospital";

    private static final String PASSWORD =
            "hosp123";


    public static Connection getConnection() {

        Connection con = null;

        try {

            con = DriverManager.getConnection(
                    URL,
                    USERNAME,
                    PASSWORD
            );

            System.out.println("Database Connected");

        }

        catch (SQLException e) {

            System.out.println("Connection Failed");
            e.printStackTrace();
        }

        return con;
    }
}