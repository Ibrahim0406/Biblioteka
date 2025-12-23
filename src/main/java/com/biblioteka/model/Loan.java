package com.biblioteka.model;

import javafx.beans.property.*;
import java.time.LocalDate;

public class Loan {
    private final IntegerProperty id;
    private final IntegerProperty bookId;
    private final StringProperty bookTitle;
    private final StringProperty borrower;
    private final ObjectProperty<LocalDate> loanDate;
    private final ObjectProperty<LocalDate> returnDate;

    public Loan() {
        this(0, 0, "", "", LocalDate.now(), null);
    }

    public Loan(int id, int bookId, String bookTitle, String borrower,
                LocalDate loanDate, LocalDate returnDate) {
        this.id = new SimpleIntegerProperty(id);
        this.bookId = new SimpleIntegerProperty(bookId);
        this.bookTitle = new SimpleStringProperty(bookTitle);
        this.borrower = new SimpleStringProperty(borrower);
        this.loanDate = new SimpleObjectProperty<>(loanDate);
        this.returnDate = new SimpleObjectProperty<>(returnDate);
    }

    // ID
    public int getId() { return id.get(); }
    public void setId(int value) { id.set(value); }
    public IntegerProperty idProperty() { return id; }

    // Book ID
    public int getBookId() { return bookId.get(); }
    public void setBookId(int value) { bookId.set(value); }
    public IntegerProperty bookIdProperty() { return bookId; }

    // Book Title
    public String getBookTitle() { return bookTitle.get(); }
    public void setBookTitle(String value) { bookTitle.set(value); }
    public StringProperty bookTitleProperty() { return bookTitle; }

    // Borrower
    public String getBorrower() { return borrower.get(); }
    public void setBorrower(String value) { borrower.set(value); }
    public StringProperty borrowerProperty() { return borrower; }

    // Loan Date
    public LocalDate getLoanDate() { return loanDate.get(); }
    public void setLoanDate(LocalDate value) { loanDate.set(value); }
    public ObjectProperty<LocalDate> loanDateProperty() { return loanDate; }

    // Return Date
    public LocalDate getReturnDate() { return returnDate.get(); }
    public void setReturnDate(LocalDate value) { returnDate.set(value); }
    public ObjectProperty<LocalDate> returnDateProperty() { return returnDate; }

    @Override
    public String toString() {
        return "Loan{id=" + getId() + ", book='" + getBookTitle() + "', borrower='" + getBorrower() + "'}";
    }
}