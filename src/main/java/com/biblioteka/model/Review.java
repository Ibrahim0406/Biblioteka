package com.biblioteka.model;

import javafx.beans.property.*;
import java.time.LocalDate;

public class Review {
    private final IntegerProperty id;
    private final IntegerProperty bookId;
    private final StringProperty bookTitle;
    private final StringProperty username;
    private final IntegerProperty rating;
    private final StringProperty comment;
    private final ObjectProperty<LocalDate> reviewDate;

    public Review() {
        this(0, 0, "", "", 0, "", LocalDate.now());
    }

    public Review(int id, int bookId, String bookTitle, String username, int rating, String comment, LocalDate reviewDate) {
        this.id = new SimpleIntegerProperty(id);
        this.bookId = new SimpleIntegerProperty(bookId);
        this.bookTitle = new SimpleStringProperty(bookTitle);
        this.username = new SimpleStringProperty(username);
        this.rating = new SimpleIntegerProperty(rating);
        this.comment = new SimpleStringProperty(comment);
        this.reviewDate = new SimpleObjectProperty<>(reviewDate);
    }

    // ID
    public int getId() {
        return id.get();
    }

    public void setId(int value) {
        id.set(value);
    }

    public IntegerProperty idProperty() {
        return id;
    }

    // Book ID
    public int getBookId() {
        return bookId.get();
    }

    public void setBookId(int value) {
        bookId.set(value);
    }

    public IntegerProperty bookIdProperty() {
        return bookId;
    }

    // Book Title
    public String getBookTitle() {
        return bookTitle.get();
    }

    public void setBookTitle(String value) {
        bookTitle.set(value);
    }

    public StringProperty bookTitleProperty() {
        return bookTitle;
    }

    // Username
    public String getUsername() {
        return username.get();
    }

    public void setUsername(String value) {
        username.set(value);
    }

    public StringProperty usernameProperty() {
        return username;
    }

    // Rating
    public int getRating() {
        return rating.get();
    }

    public void setRating(int value) {
        rating.set(value);
    }

    public IntegerProperty ratingProperty() {
        return rating;
    }

    // Comment
    public String getComment() {
        return comment.get();
    }

    public void setComment(String value) {
        comment.set(value);
    }

    public StringProperty commentProperty() {
        return comment;
    }

    // Review Date
    public LocalDate getReviewDate() {
        return reviewDate.get();
    }

    public void setReviewDate(LocalDate value) {
        reviewDate.set(value);
    }

    public ObjectProperty<LocalDate> reviewDateProperty() {
        return reviewDate;
    }
}
