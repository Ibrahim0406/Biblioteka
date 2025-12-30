package com.biblioteka.database;

import java.sql.*;

public class DatabaseConnection {
    // MySQL konfiguracija
    private static final String URL = "jdbc:mysql://localhost:3306/library_db";
    private static final String USER = "root";
    private static final String PASSWORD = "root"; // Unesite vašu MySQL lozinku

    private static Connection connection = null;

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            initializeDatabase();
        }
        return connection;
    }

    private static void initializeDatabase() {
        try (Statement stmt = connection.createStatement()) {
            // Kreiranje tabele users
            stmt.execute(
                    "CREATE TABLE IF NOT EXISTS users (" +
                            "id INT AUTO_INCREMENT PRIMARY KEY, " +
                            "username VARCHAR(50) UNIQUE NOT NULL, " +
                            "password VARCHAR(255) NOT NULL, " +
                            "full_name VARCHAR(100) NOT NULL, " +
                            "role VARCHAR(20) NOT NULL)"
            );

            // Kreiranje tabele books
            stmt.execute(
                    "CREATE TABLE IF NOT EXISTS books (" +
                            "id INT AUTO_INCREMENT PRIMARY KEY, " +
                            "title VARCHAR(255) NOT NULL, " +
                            "author VARCHAR(100) NOT NULL, " +
                            "isbn VARCHAR(20) UNIQUE NOT NULL, " +
                            "year INT NOT NULL, " +
                            "price DECIMAL(10,2) NOT NULL, " +
                            "status VARCHAR(20) DEFAULT 'DOSTUPNO', " +
                            "available_quantity INT DEFAULT 0 CHECK (avilable_quantity >= 0))"
            );

            // Dodavanje novih kolona ako već postoje knjige bez njih
            try {
                ResultSet rs = stmt.executeQuery("SHOW COLUMNS FROM books LIKE 'status'");
                if (!rs.next()) {
                    stmt.execute("ALTER TABLE books ADD COLUMN status VARCHAR(20) DEFAULT 'DOSTUPNO'");
                }
            } catch (SQLException e) {
                // Ignoriši grešku
            }
            try {
                ResultSet rs = stmt.executeQuery("SHOW COLUMNS FROM books LIKE 'available_quantity'");
                if (!rs.next()) {
                    stmt.execute("ALTER TABLE books ADD COLUMN available_quantity INT DEFAULT 1");
                }
            } catch (SQLException e) {
                // Ignoriši grešku
            }

            // Ažuriranje postojećih knjiga da imaju default vrijednosti
            try {
                stmt.execute("UPDATE books SET status = 'DOSTUPNO' WHERE status IS NULL");
                stmt.execute("UPDATE books SET available_quantity = 1 WHERE available_quantity IS NULL");
            } catch (SQLException e) {
                // Ignoriši grešku
            }

            // Kreiranje tabele loans
            stmt.execute(
                    "CREATE TABLE IF NOT EXISTS loans (" +
                            "id INT AUTO_INCREMENT PRIMARY KEY, " +
                            "book_id INT NOT NULL, " +
                            "borrower VARCHAR(100) NOT NULL, " +
                            "loan_date DATE NOT NULL, " +
                            "return_date DATE, " +
                            "FOREIGN KEY (book_id) REFERENCES books(id) ON DELETE CASCADE)"
            );

            // Kreiranje tabele za recenzije i ocjene
            stmt.execute(
                    "CREATE TABLE IF NOT EXISTS reviews (" +
                            "id INT AUTO_INCREMENT PRIMARY KEY, " +
                            "book_id INT NOT NULL, " +
                            "username VARCHAR(50) NOT NULL, " +
                            "rating INT NOT NULL CHECK (rating BETWEEN 1 AND 5), " +
                            "comment TEXT, " +
                            "review_date DATE NOT NULL, " +
                            "FOREIGN KEY (book_id) REFERENCES books(id) ON DELETE CASCADE, " +
                            "UNIQUE KEY unique_user_book (username, book_id))"
            );

            stmt.execute(
                    "CREATE TABLE IF NOT EXISTS reservations (" +
                            "id INT AUTO_INCREMENT PRIMARY KEY, " +
                            "book_id INT NOT NULL, " +
                            "book_title VARCHAR(255) NOT NULL, " +
                            "username VARCHAR(50) NOT NULL, " +
                            "reservation_date DATE NOT NULL, " +
                            "status VARCHAR(20) DEFAULT 'AKTIVNA', " +
                            "FOREIGN KEY (book_id) REFERENCES books(id) ON DELETE CASCADE)"
            );

            // Dodavanje default korisnika ako ne postoji
            String checkAdmin = "SELECT COUNT(*) FROM users WHERE username = 'admin'";
            ResultSet rs = stmt.executeQuery(checkAdmin);
            if (rs.next() && rs.getInt(1) == 0) {
                stmt.execute(
                        "INSERT INTO users (username, password, full_name, role) " +
                                "VALUES ('admin', 'admin123', 'Administrator', 'ADMIN')"
                );
            }

            // Dodavanje default korisnika za testiranje
            String checkUser = "SELECT COUNT(*) FROM users WHERE username = 'korisnik'";
            rs = stmt.executeQuery(checkUser);
            if (rs.next() && rs.getInt(1) == 0) {
                stmt.execute(
                        "INSERT INTO users (username, password, full_name, role) " +
                                "VALUES ('korisnik', 'korisnik123', 'Test Korisnik', 'USER')"
                );
            }

            System.out.println("MySQL baza podataka uspješno inicijalizovana!");

        } catch (SQLException e) {
            System.err.println("Greška pri inicijalizaciji baze: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Konekcija zatvorena.");
            }
        } catch (SQLException e) {
            System.err.println("Greška pri zatvaranju konekcije: " + e.getMessage());
        }
    }
}
