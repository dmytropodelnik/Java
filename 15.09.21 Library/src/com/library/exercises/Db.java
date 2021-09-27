package com.library.exercises;

import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;

public class Db {

    private final String PREFIX = "KH181_14_";

    /**
     * Oracle DB Express Edition
     */
    public void demo_xe() {
        Connection connection;  // ~SqlConnection

        // Loading config: ../config/db.json
        String connectionString;

        File file = new File("./src/step/java/config/db.json");
        if (!file.exists()) {
            System.out.println("Config location error");
            return;
        }
        JSONObject conf;
        try {
            conf = new JSONObject(
                    new String(
                            new FileInputStream(file)
                                    .readAllBytes()
                    )
            );

            connectionString = String.format(
                    "jdbc:oracle:thin:%s/%s@%s:%d:XE",
                    conf.getString("user"),
                    conf.getString("pass"),
                    conf.getString("host"),
                    conf.getInt("port")
            );

        } catch (IOException ex) {
            System.err.println(ex.getMessage());
            return;
        }

        try {
            // Registering driver
            DriverManager.registerDriver(
                    // Don't forget add OJDBC.JAR library
                    new oracle.jdbc.driver.OracleDriver()
            );

            // Connecting...
            connection = DriverManager.getConnection(
                    connectionString
            );
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
            return;
        }

        // Queries
        // ! Single DB - user prefixes
        // Update-Queries: DDL + DML (except SELECT)
        String query;
        /*
                query = "CREATE TABLE " + PREFIX + "exercise( "
                + "id RAW(16) DEFAULT SYS_GUID() PRIMARY KEY, "
                + "name NVARCHAR2(64) NOT NULL,"
                + "id_parent RAW(16) )";
         */
        /*
        query = "INSERT INTO " + PREFIX + "exercise(name) " +
                "VALUES('Petrovich')";
         */
        /*
        query = "INSERT INTO " + PREFIX + "exercise(name, id_parent) " +
                "VALUES('Lukich', (SELECT id FROM "
                + PREFIX + "exercise WHERE name = 'Petrovich'))";
         */
        /*
        query = String.format(
                "INSERT INTO %sexercise(name, id_parent)" +
                "VALUES('%s', (SELECT id FROM %sexercise WHERE name = '%s'))",
                PREFIX, "Pavlovna", PREFIX, "Petrovich"
        );

        try (Statement statement = connection.createStatement()) {
            // statement ~ SqlCommand
            statement.executeUpdate(query);
        }
        catch (SQLException ex) {
            System.err.println(ex.getMessage() + " " + query);
            return;
        }
        */

        // HOMEWORK

        // create cinemas table
        /*
        query = "CREATE TABLE " + "Cinemas" + "exercise( "
                + "id RAW(16) DEFAULT SYS_GUID() PRIMARY KEY, "
                + "city NVARCHAR2(50) NOT NULL,"
                + "street NVARCHAR2(50) NOT NULL,"
                + "house NVARCHAR2(10) NOT NULL)";
         */

        // create cinemas' phones table
        /*
        query = "CREATE TABLE " + "Cinemas Phones" + "exercise( "
                + "id RAW(16) DEFAULT SYS_GUID() PRIMARY KEY, "
                + "phone NVARCHAR2(30) NOT NULL,"
                + "id_cinema RAW(16))";
         */

        // create countries table
        /*
        query = "CREATE TABLE " + "Countries" + "exercise( "
                + "id RAW(16) DEFAULT SYS_GUID() PRIMARY KEY, "
                + "country NVARCHAR2(30) NOT NULL)";
         */

        // create genres table
        /*
        query = "CREATE TABLE " + "Genres" + "exercise( "
                + "id RAW(16) DEFAULT SYS_GUID() PRIMARY KEY, "
                + "name NVARCHAR2(25) NOT NULL)";
         */

        // add address
        /*
        query = String.format(
                "INSERT INTO %sexercise(city, street, house)" +
                        "VALUES('%s', '%s','%s')",
                "Cinemas", "Odesa", "Main road", "24"
        );
         */

        // add phone number
        /*
        query = String.format(
                "INSERT INTO %sexercise(phone, id_cinema)" +
                        "VALUES('%s', (SELECT id FROM %sexercise WHERE name = '%s'))",
                "Cinemas Phones", "380508763182", "Cinemas", "PlanetaKino"
        );
         */

        // add country
        /*
        query = String.format(
                "INSERT INTO %sexercise(country)" +
                        "VALUES('%s')",
                "Countries", "Ukraine"
        );
         */

        // add genre
        /*
        query = String.format(
                "INSERT INTO %sexercise(country)" +
                        "VALUES('%s')",
                "Genres", "Action"
        );

        try (Statement statement = connection.createStatement()) {
            // statement ~ SqlCommand
            statement.executeUpdate(query);
        }
        catch (SQLException ex) {
            System.err.println(ex.getMessage() + " " + query);
            return;
        }
        */

        /*
        query = String.format(
                "INSERT INTO %sexercise(country)" +
                        "VALUES('%s')",
                "Genres", "Action"
        );
         */

        /*
        query = "INSERT INTO " + "Genres" + "exercise(name) VALUES(?)";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, "Comedy");
            statement.executeUpdate();
        }
        catch (SQLException ex) {
            System.err.println(ex.getMessage() + " " + query);
            return;
        }
         */
        /*
        query = "INSERT INTO " + "Countries" + "exercise(country) VALUES(?)";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, "Ukraine");
            statement.executeUpdate();
        }
        catch (SQLException ex) {
            System.err.println(ex.getMessage() + " " + query);
            return;
        }
        */
        /*
        query = "INSERT INTO " + PREFIX + "exercise(name) VALUES(?)";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, "Kuzmich");
            statement.executeUpdate();
        }
        catch (SQLException ex) {
            System.err.println(ex.getMessage() + " " + query);
            return;
        }
         */

        System.out.println("OK");
    }

    /**
     * Oracle MariaDB (ex MySQL)
     */
    public void demo_maria() {
        /*
            Preface: 1. Install MySQL/MariaDB
            2. Create DataBase (CREATE DATABASE J181;)
            3. Create ALL PRIVILEGES ON J181
                (GRANT ALL PRIVILEGES ON J181.* TO 'user181'@'localhost' IDENTIFIED BY 'pass181';)
            4.

         */

        String connectionString;

        File file = new File("./src/step/java/config/db3.json");
        if (!file.exists()) {
            System.out.println("Config location error");
            return;
        }
        JSONObject conf;
        try {
            conf = new JSONObject(
                    new String(
                            new FileInputStream(file)
                                    .readAllBytes()
                    )
            );

            connectionString = String.format(
                    "jdbc:%s://%s:%d/%s"
                    + "?useUnicode=true&characterEncoding=UTF-8"
                    + "&useJDBCCompliantTimezoneShift=true&use"
                    conf.getString("dbms"),
                    conf.getString("host"),
                    conf.getInt("port"),
                    conf.getString("schema")
            );

        } catch (IOException ex) {
            System.err.println(ex.getMessage());
            return;
        }
    }
}
/*
    Работа с базами данных.
    База данных - способ организации данных, при котором, кроме данных, создаются связи между ними.
    По видам связей БД классифицируются отдельно.
        Табличные БД (обычныеб, SQL-БД)
        Сетевые БД (Grid)
        Графовые (GraphQL)
        ООП (Oracle)
    Стандарт SQL довольно старый (74-86). Все СУБД его реализуют, а также добавляют свои особенности.
        Это формирует диалекты SQL.
        Кроме диалектов разные СУБД вводят свои типы данных
        VARCHAR2(size) - 4k ANSI
        NVARCHAR2(size)- 4k Localized
        NUMBER(p, s) - p - цифр всего, s (scale) - точность (после .)
        RAW(size) - бинарный (байтовый) тип
        CLOB - Char LOB (~TEXT)
        NCLOB
    СУБД (DBMS) играют роль сервера. Соответственно, требуют подключения.
        При подключении из "языка программирования" требуются специальные драйверы (коннекторы).
    После установки подключения языки программирования предлагают универсальный интерфейс для
        работы с БД (ADO, PDO, JDBC, ...)

 */
