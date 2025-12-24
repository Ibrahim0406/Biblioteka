package com.biblioteka.dao;

import com.biblioteka.database.DatabaseConnection;
import com.biblioteka.model.Review;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReviewDAO implements DAO<Review> {

    @Override
    public void create(Review review) throws Exception {
        String sql = "INSERT INTO reviews (book_id, username, rating, comment, review_date) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, review.getBookId());
            pstmt.setString(2, review.getUsername());
            pstmt.setInt(3, review.getRating());
            pstmt.setString(4, review.getComment());
            pstmt.setDate(5, Date.valueOf(review.getReviewDate()));
            pstmt.executeUpdate();
        }
    }

    @Override
    public Review read(int id) throws Exception {
        String sql = "SELECT r.*, b.title as book_title FROM reviews r " +
                "JOIN books b ON r.book_id = b.id WHERE r.id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new Review(
                        rs.getInt("id"),
                        rs.getInt("book_id"),
                        rs.getString("book_title"),
                        rs.getString("username"),
                        rs.getInt("rating"),
                        rs.getString("comment"),
                        rs.getDate("review_date").toLocalDate()
                );
            }
        }
        return null;
    }

    @Override
    public List<Review> readAll() throws Exception {
        List<Review> reviews = new ArrayList<>();
        String sql = "SELECT r.*, b.title as book_title FROM reviews r " +
                "JOIN books b ON r.book_id = b.id ORDER BY r.review_date DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                reviews.add(new Review(
                        rs.getInt("id"),
                        rs.getInt("book_id"),
                        rs.getString("book_title"),
                        rs.getString("username"),
                        rs.getInt("rating"),
                        rs.getString("comment"),
                        rs.getDate("review_date").toLocalDate()
                ));
            }
        }
        return reviews;
    }

    @Override
    public void update(Review review) throws Exception {
        String sql = "UPDATE reviews SET rating = ?, comment = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, review.getRating());
            pstmt.setString(2, review.getComment());
            pstmt.setInt(3, review.getId());
            pstmt.executeUpdate();
        }
    }

    @Override
    public void delete(int id) throws Exception {
        String sql = "DELETE FROM reviews WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }

    // Dohvati sve recenzije za određenu knjigu
    public List<Review> getReviewsByBook(int bookId) throws Exception {
        List<Review> reviews = new ArrayList<>();
        String sql = "SELECT r.*, b.title as book_title FROM reviews r " +
                "JOIN books b ON r.book_id = b.id " +
                "WHERE r.book_id = ? ORDER BY r.review_date DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, bookId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                reviews.add(new Review(
                        rs.getInt("id"),
                        rs.getInt("book_id"),
                        rs.getString("book_title"),
                        rs.getString("username"),
                        rs.getInt("rating"),
                        rs.getString("comment"),
                        rs.getDate("review_date").toLocalDate()
                ));
            }
        }
        return reviews;
    }

    // Dohvati prosječnu ocjenu za knjigu
    public double getAverageRating(int bookId) throws Exception {
        String sql = "SELECT AVG(rating) as avg_rating FROM reviews WHERE book_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, bookId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getDouble("avg_rating");
            }
        }
        return 0.0;
    }

    // Provjeri da li je korisnik već recenzirao knjigu
    public boolean hasUserReviewedBook(String username, int bookId) throws Exception {
        String sql = "SELECT COUNT(*) FROM reviews WHERE username = ? AND book_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            pstmt.setInt(2, bookId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
        return false;
    }

    // Dohvati recenzije određenog korisnika
    public List<Review> getReviewsByUser(String username) throws Exception {
        List<Review> reviews = new ArrayList<>();
        String sql = "SELECT r.*, b.title as book_title FROM reviews r " +
                "JOIN books b ON r.book_id = b.id " +
                "WHERE r.username = ? ORDER BY r.review_date DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                reviews.add(new Review(
                        rs.getInt("id"),
                        rs.getInt("book_id"),
                        rs.getString("book_title"),
                        rs.getString("username"),
                        rs.getInt("rating"),
                        rs.getString("comment"),
                        rs.getDate("review_date").toLocalDate()
                ));
            }
        }
        return reviews;
    }
}
