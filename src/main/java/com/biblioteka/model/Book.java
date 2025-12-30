package com.biblioteka.model;


import javafx.beans.property.*;

public class Book {
    private final IntegerProperty id;
    private final StringProperty title;
    private final StringProperty author;
    private final StringProperty isbn;
    private final IntegerProperty year;
    private final DoubleProperty price;
    private final StringProperty status;
    private final IntegerProperty availableQuantity;

    public Book() {
        this(0, "", "", "", 0, 0.0, "DOSTUPNO", 1);
    }

    public Book(int id, String title, String author, String isbn, int year, double price) {
        this(id, title, author, isbn, year, price, "DOSTUPNO", 0);
    }

    public Book(int id, String title, String author, String isbn, int year, double price, String status, int availableQuantity) {
        this.id = new SimpleIntegerProperty(id);
        this.title = new SimpleStringProperty(title);
        this.author = new SimpleStringProperty(author);
        this.isbn = new SimpleStringProperty(isbn);
        this.year = new SimpleIntegerProperty(year);
        this.price = new SimpleDoubleProperty(price);
        this.status = new SimpleStringProperty(status);
        this.availableQuantity = new SimpleIntegerProperty(availableQuantity);
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

    // Title
    public String getTitle() {
        return title.get();
    }

    public void setTitle(String value) {
        title.set(value);
    }

    public StringProperty titleProperty() {
        return title;
    }

    // Author
    public String getAuthor() {
        return author.get();
    }

    public void setAuthor(String value) {
        author.set(value);
    }

    public StringProperty authorProperty() {
        return author;
    }

    // ISBN
    public String getIsbn() {
        return isbn.get();
    }

    public void setIsbn(String value) {
        isbn.set(value);
    }

    public StringProperty isbnProperty() {
        return isbn;
    }

    // Year
    public int getYear() {
        return year.get();
    }

    public void setYear(int value) {
        year.set(value);
    }

    public IntegerProperty yearProperty() {
        return year;
    }

    // Price
    public double getPrice() {
        return price.get();
    }

    public void setPrice(double value) {
        price.set(value);
    }

    public DoubleProperty priceProperty() {
        return price;
    }

    // Status
    public String getStatus() {
        return status.get();
    }

    public void setStatus(String value) {
        status.set(value);
    }

    public StringProperty statusProperty() {
        return status;
    }

    // Available Quantity
    public int getAvailableQuantity() {
        return availableQuantity.get();
    }

    public void setAvailableQuantity(int value) {
        availableQuantity.set(value);
    }

    public IntegerProperty availableQuantityProperty() {
        return availableQuantity;
    }

    @Override
    public String toString() {
        return "Book{id=" + getId() + ", title='" + getTitle() + "', author='" + getAuthor() + "'}";
    }
}