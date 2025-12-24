package com.biblioteka.model;

import javafx.beans.property.*;
import java.time.LocalDate;

public class Reservation {
    private final IntegerProperty id;
    private final IntegerProperty bookId;
    private final StringProperty bookTitle;
    private final StringProperty username;
    private final ObjectProperty<LocalDate> reservationDate;
    private final StringProperty status;

    public Reservation(int id, int bookId, String bookTitle, String username, LocalDate reservationDate, String status) {
        this.id = new SimpleIntegerProperty(id);
        this.bookId = new SimpleIntegerProperty(bookId);
        this.bookTitle = new SimpleStringProperty(bookTitle);
        this.username = new SimpleStringProperty(username);
        this.reservationDate = new SimpleObjectProperty<>(reservationDate);
        this.status = new SimpleStringProperty(status);
    }

    // Property getters
    public IntegerProperty idProperty() { return id; }
    public IntegerProperty bookIdProperty() { return bookId; }
    public StringProperty bookTitleProperty() { return bookTitle; }
    public StringProperty usernameProperty() { return username; }
    public ObjectProperty<LocalDate> reservationDateProperty() { return reservationDate; }
    public StringProperty statusProperty() { return status; }

    // Regular getters
    public int getId() { return id.get(); }
    public int getBookId() { return bookId.get(); }
    public String getBookTitle() { return bookTitle.get(); }
    public String getUsername() { return username.get(); }
    public LocalDate getReservationDate() { return reservationDate.get(); }
    public String getStatus() { return status.get(); }

    // Setters
    public void setId(int id) { this.id.set(id); }
    public void setBookId(int bookId) { this.bookId.set(bookId); }
    public void setBookTitle(String bookTitle) { this.bookTitle.set(bookTitle); }
    public void setUsername(String username) { this.username.set(username); }
    public void setReservationDate(LocalDate date) { this.reservationDate.set(date); }
    public void setStatus(String status) { this.status.set(status); }
}
