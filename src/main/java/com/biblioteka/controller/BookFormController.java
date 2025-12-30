package com.biblioteka.controller;

import com.biblioteka.dao.BookDAO;
import com.biblioteka.model.Book;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.LocalDate;

public class BookFormController {
    @FXML private TextField titleField;
    @FXML private TextField authorField;
    @FXML private TextField isbnField;
    @FXML private TextField yearField;
    @FXML private TextField priceField;
    @FXML private TextField quantityField;
    @FXML private Button saveButton;

    private BookDAO bookDAO = new BookDAO();
    private AdminMainController adminController;
    private Book currentBook = null;

    public void setAdminController(AdminMainController controller) {
        this.adminController = controller;
    }

    public void setBook(Book book) {
        this.currentBook = book;
        titleField.setText(book.getTitle());
        authorField.setText(book.getAuthor());
        isbnField.setText(book.getIsbn());
        yearField.setText(String.valueOf(book.getYear()));
        priceField.setText(String.valueOf(book.getPrice()));
        quantityField.setText(String.valueOf(book.getAvailableQuantity()));
        saveButton.setText("Ažuriraj");
    }

    @FXML
    private void handleSave() {
        if (!validateInput()) {
            return;
        }

        try {
            String title = titleField.getText().trim();
            String author = authorField.getText().trim();
            String isbn = isbnField.getText().trim();
            int year = Integer.parseInt(yearField.getText().trim());
            double price = Double.parseDouble(priceField.getText().trim());
            int quantity = Integer.parseInt(quantityField.getText().trim());

            if (currentBook == null) {
                Book newBook = new Book(0, title, author, isbn, year, price, "DOSTUPNO", quantity);
                bookDAO.create(newBook);
                showAlert("Uspjeh", "Knjiga uspješno dodana!", Alert.AlertType.INFORMATION);
            } else {
                currentBook.setTitle(title);
                currentBook.setAuthor(author);
                currentBook.setIsbn(isbn);
                currentBook.setYear(year);
                currentBook.setPrice(price);
                currentBook.setAvailableQuantity(quantity);
                currentBook.setStatus(quantity > 0 ? "DOSTUPNO" : "NEDOSTUPNO");
                bookDAO.update(currentBook);
                showAlert("Uspjeh", "Knjiga uspješno ažurirana!", Alert.AlertType.INFORMATION);
            }

            adminController.refreshTable();
            handleCancel();

        } catch (NumberFormatException e) {
            showAlert("Greška", "Molimo unesite validne brojeve za godinu, cijenu i količinu!", Alert.AlertType.ERROR);
        } catch (Exception e) {
            showAlert("Greška", "Greška pri snimanju: " + e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    private boolean validateInput() {
        if (titleField.getText().trim().isEmpty()) {
            showAlert("Validacija", "Naslov ne može biti prazan!", Alert.AlertType.WARNING);
            titleField.requestFocus();
            return false;
        }

        if (authorField.getText().trim().isEmpty()) {
            showAlert("Validacija", "Autor ne može biti prazan!", Alert.AlertType.WARNING);
            authorField.requestFocus();
            return false;
        }

        if (isbnField.getText().trim().isEmpty()) {
            showAlert("Validacija", "ISBN ne može biti prazan!", Alert.AlertType.WARNING);
            isbnField.requestFocus();
            return false;
        }

        int trenutnaGodina = LocalDate.now().getYear();

        try {
            int year = Integer.parseInt(yearField.getText().trim());
            if (year < 1450 || year > trenutnaGodina) {
                showAlert("Validacija", "Godina mora biti između 1450 i " + trenutnaGodina, Alert.AlertType.WARNING);
                yearField.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            showAlert("Validacija", "Godina mora biti broj!", Alert.AlertType.WARNING);
            yearField.requestFocus();
            return false;
        }

        try {
            double price = Double.parseDouble(priceField.getText().trim());
            if (price < 0) {
                showAlert("Validacija", "Cijena ne može biti negativna!", Alert.AlertType.WARNING);
                priceField.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            showAlert("Validacija", "Cijena mora biti broj!", Alert.AlertType.WARNING);
            priceField.requestFocus();
            return false;
        }

        try {
            int quantity = Integer.parseInt(quantityField.getText().trim());
            if (quantity < 0) {
                showAlert("Validacija", "Količina ne moze biti negativna!", Alert.AlertType.WARNING);
                quantityField.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            showAlert("Validacija", "Količina mora biti broj!", Alert.AlertType.WARNING);
            quantityField.requestFocus();
            return false;
        }

        return true;
    }

    @FXML
    private void handleCancel() {
        Stage stage = (Stage) titleField.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
