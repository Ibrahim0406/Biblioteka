package com.biblioteka.dao;

import com.biblioteka.database.DatabaseConnection;
import com.biblioteka.model.Reservation;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReservationDAO implements DAO<Reservation> {

    @Override
    public void create(Reservation reservation) throws SQLException {
        String query = "INSERT INTO reservations (book_id, book_title, username, reservation_date, status) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, reservation.getBookId());
            stmt.setString(2, reservation.getBookTitle());
            stmt.setString(3, reservation.getUsername());
            stmt.setDate(4, Date.valueOf(reservation.getReservationDate()));
            stmt.setString(5, reservation.getStatus());
            stmt.executeUpdate();
        }
    }

    @Override
    public Reservation read(int id) throws SQLException {
        String query = "SELECT * FROM reservations WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToReservation(rs);
            }
        }
        return null;
    }

    @Override
    public List<Reservation> readAll() throws SQLException {
        List<Reservation> reservations = new ArrayList<>();
        String query = "SELECT * FROM reservations ORDER BY reservation_date DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                reservations.add(mapResultSetToReservation(rs));
            }
        }
        return reservations;
    }

    @Override
    public void update(Reservation reservation) throws SQLException {
        String query = "UPDATE reservations SET book_id = ?, book_title = ?, username = ?, reservation_date = ?, status = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, reservation.getBookId());
            stmt.setString(2, reservation.getBookTitle());
            stmt.setString(3, reservation.getUsername());
            stmt.setDate(4, Date.valueOf(reservation.getReservationDate()));
            stmt.setString(5, reservation.getStatus());
            stmt.setInt(6, reservation.getId());
            stmt.executeUpdate();
        }
    }

    @Override
    public void delete(int id) throws SQLException {
        String query = "DELETE FROM reservations WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public List<Reservation> getReservationsByUser(String username) throws SQLException {
        List<Reservation> reservations = new ArrayList<>();
        String query = "SELECT * FROM reservations WHERE username = ? AND status = 'AKTIVNA' ORDER BY reservation_date DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                reservations.add(mapResultSetToReservation(rs));
            }
        }
        return reservations;
    }

    public void cancelReservation(int id) throws SQLException {
        String query = "UPDATE reservations SET status = 'OTKAZANA' WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    private Reservation mapResultSetToReservation(ResultSet rs) throws SQLException {
        return new Reservation(
                rs.getInt("id"),
                rs.getInt("book_id"),
                rs.getString("book_title"),
                rs.getString("username"),
                rs.getDate("reservation_date").toLocalDate(),
                rs.getString("status")
        );
    }
}
