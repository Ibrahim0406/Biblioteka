package com.biblioteka.dao;

import com.biblioteka.database.DatabaseConnection;
import com.biblioteka.model.Book;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookDAO implements DAO<Book> {

    @Override
    public void create(Book book) throws Exception {
        String sql = "INSERT INTO books (title, author, isbn, year, price, status, available_quantity) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, book.getTitle());
            pstmt.setString(2, book.getAuthor());
            pstmt.setString(3, book.getIsbn());
            pstmt.setInt(4, book.getYear());
            pstmt.setDouble(5, book.getPrice());
            pstmt.setString(6, book.getStatus() != null ? book.getStatus() : "DOSTUPNO");
            pstmt.setInt(7, book.getAvailableQuantity() > 0 ? book.getAvailableQuantity() : 1);
            pstmt.executeUpdate();
        }
    }

    @Override
    public Book read(int id) throws Exception {
        String sql = "SELECT * FROM books WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String status = rs.getString("status");
                int availableQuantity = rs.getInt("available_quantity");
                if (status == null) status = "DOSTUPNO";
                if (availableQuantity == 0) availableQuantity = 1;
                return new Book(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getString("isbn"),
                        rs.getInt("year"),
                        rs.getDouble("price"),
                        status,
                        availableQuantity
                );
            }
        }
        return null;
    }

    @Override
    public List<Book> readAll() throws Exception {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM books ORDER BY title";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String status = rs.getString("status");
                int availableQuantity = rs.getInt("available_quantity");
                if (status == null) status = "DOSTUPNO";
                if (availableQuantity == 0) availableQuantity = 1;
                books.add(new Book(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getString("isbn"),
                        rs.getInt("year"),
                        rs.getDouble("price"),
                        status,
                        availableQuantity
                ));
            }
        }
        return books;
    }

    @Override
    public void update(Book book) throws Exception {
        String sql = "UPDATE books SET title = ?, author = ?, isbn = ?, year = ?, price = ?, status = ?, available_quantity = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, book.getTitle());
            pstmt.setString(2, book.getAuthor());
            pstmt.setString(3, book.getIsbn());
            pstmt.setInt(4, book.getYear());
            pstmt.setDouble(5, book.getPrice());
            pstmt.setString(6, book.getStatus() != null ? book.getStatus() : "DOSTUPNO");
            pstmt.setInt(7, book.getAvailableQuantity());
            pstmt.setInt(8, book.getId());
            pstmt.executeUpdate();
        }
    }

    // Metoda za smanjenje količine dostupnih knjiga
    public void decreaseQuantity(int bookId) throws Exception {
        String sql = "UPDATE books SET available_quantity = available_quantity - 1, status = CASE WHEN available_quantity - 1 <= 0 THEN 'NEDOSTUPNO' ELSE status END WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, bookId);
            pstmt.executeUpdate();
        }
    }

    // Metoda za povećanje količine dostupnih knjiga
    public void increaseQuantity(int bookId) throws Exception {
        String sql = "UPDATE books SET available_quantity = available_quantity + 1, status = 'DOSTUPNO' WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, bookId);
            pstmt.executeUpdate();
        }
    }

    @Override
    public void delete(int id) throws Exception {
        String sql = "DELETE FROM books WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }
}