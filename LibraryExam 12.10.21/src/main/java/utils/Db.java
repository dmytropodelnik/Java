package utils;

import models.Book;
import org.json.JSONObject;

import java.sql.*;
import java.util.ArrayList;

public class Db {
    private static JSONObject config ;
    private static Connection connection ;
    private static final String PREFIX = "KH181_14_";

    private static BookOrm bookOrm;
    public static BookOrm getBookOrm() {
        if (bookOrm == null) {
            bookOrm = new BookOrm(connection, PREFIX, config);
        }
        return bookOrm;
    }
    public static Connection getConnection() {
        return connection;
    }

    public static boolean setConnection(JSONObject json) {
        try {
            String connectionString;
            String dbms = json.getString("dbms");
            if (dbms.equalsIgnoreCase("Oracle")) {
                connectionString = String.format(
                        "jdbc:oracle:thin:%s/%s@%s:%s:XE",
                        json.getString("user"),
                        json.getString("pass"),
                        json.getString("host"),
                        json.getString("port")
                );
                DriverManager.registerDriver(
                        new oracle.jdbc.driver.OracleDriver()
                );
                connection =
                        DriverManager.getConnection(connectionString);

                config = json;
                return true;
            }
            else if (dbms.equalsIgnoreCase("Oracle")) {

            } else {
                System.err.println("Db: Unsupported DBMS");
            }
        } catch (Exception ex) {
            System.err.println("Db: " + ex.getMessage());
        }
        connection = null;
        config = null;
        return false;
    }

    public static void closeConnection() {
        if (connection != null)
            try {
                connection.close();
            } catch (Exception ignored) {

            }
    }

    /**
     * Loads books(s) list (library)
     */
    public static ArrayList<Book> getPictures() {
        ArrayList<Book> res = null;
        try (Statement statement = connection.createStatement()) {
            String query = "SELECT * FROM Books" + PREFIX;
            ResultSet answer = statement.executeQuery(query);
            res = new ArrayList<>();
            while (answer.next()) {
                res.add(new Book(
                        answer.getString("ID"),
                        answer.getString("AUTHOR"),
                        answer.getString("TITLE"),
                        answer.getString("MOMENT")
                ));
            }
        } catch (Exception ex) {
            System.err.println("getPictures: " + ex.getMessage());
        }
        return res;
    }
}