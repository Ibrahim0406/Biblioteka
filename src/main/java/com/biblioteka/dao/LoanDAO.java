package com.biblioteka.dao;

import com.biblioteka.database.DatabaseConnection;
import com.biblioteka.model.Loan;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class LoanDAO implements DAO<Loan> {

    @Override
    public void create(Loan loan) throws Exception {
        String sql = "INSERT INTO loans (book_id, borrower, loan_date, return_date) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, loan.getBookId());
            pstmt.setString(2, loan.getBorrower());
            pstmt.setDate(3, Date.valueOf(loan.getLoanDate()));
            if (loan.getReturnDate() != null) {
                pstmt.setDate(4, Date.valueOf(loan.getReturnDate()));
            } else {
                pstmt.setDate(4, null);
            }
            pstmt.executeUpdate();
        }
    }

    @Override
    public Loan read(int id) throws Exception {
        String sql = "SELECT l.*, b.title as book_title FROM loans l " +
                     "JOIN books b ON l.book_id = b.id WHERE l.id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                LocalDate returnDate = rs.getDate("return_date") != null ?
                        rs.getDate("return_date").toLocalDate() : null;
                return new Loan(
                        rs.getInt("id"),
                        rs.getInt("book_id"),
                        rs.getString("book_title"),
                        rs.getString("borrower"),
                        rs.getDate("loan_date").toLocalDate(),
                        returnDate
                );
            }
        }
        return null;
    }

    @Override
    public List<Loan> readAll() throws Exception {
        List<Loan> loans = new ArrayList<>();
        String sql = "SELECT l.*, b.title as book_title FROM loans l " +
                     "JOIN books b ON l.book_id = b.id ORDER BY l.loan_date DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                LocalDate returnDate = rs.getDate("return_date") != null ?
                        rs.getDate("return_date").toLocalDate() : null;
                loans.add(new Loan(
                        rs.getInt("id"),
                        rs.getInt("book_id"),
                        rs.getString("book_title"),
                        rs.getString("borrower"),
                        rs.getDate("loan_date").toLocalDate(),
                        returnDate
                ));
            }
        }
        return loans;
    }

    @Override
    public void update(Loan loan) throws Exception {
        String sql = "UPDATE loans SET book_id = ?, borrower = ?, loan_date = ?, return_date = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, loan.getBookId());
            pstmt.setString(2, loan.getBorrower());
            pstmt.setDate(3, Date.valueOf(loan.getLoanDate()));
            if (loan.getReturnDate() != null) {
                pstmt.setDate(4, Date.valueOf(loan.getReturnDate()));
            } else {
                pstmt.setDate(4, null);
            }
            pstmt.setInt(5, loan.getId());
            pstmt.executeUpdate();
        }
    }

    @Override
    public void delete(int id) throws Exception {
        String sql = "DELETE FROM loans WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }

    // Metoda za dohvaćanje pozajmica određenog korisnika
    public List<Loan> getLoansByBorrower(String borrower) throws Exception {
        List<Loan> loans = new ArrayList<>();
        String sql = "SELECT l.*, b.title as book_title FROM loans l " +
                     "JOIN books b ON l.book_id = b.id " +
                     "WHERE l.borrower = ? AND l.return_date IS NULL " +
                     "ORDER BY l.loan_date DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, borrower);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                loans.add(new Loan(
                        rs.getInt("id"),
                        rs.getInt("book_id"),
                        rs.getString("book_title"),
                        rs.getString("borrower"),
                        rs.getDate("loan_date").toLocalDate(),
                        null
                ));
            }
        }
        return loans;
    }

    // Metoda za vraćanje knjige
    public void returnBook(int loanId) throws Exception {
        String sql = "UPDATE loans SET return_date = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setDate(1, Date.valueOf(LocalDate.now()));
            pstmt.setInt(2, loanId);
            pstmt.executeUpdate();
        }
    }
}

