package com.biblioteka.controller;

import com.biblioteka.dao.BookDAO;
import com.biblioteka.model.Book;
import com.biblioteka.model.User;
import com.biblioteka.util.PDFExporter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.util.Optional;

public class AdminMainController {
    @FXML private Label welcomeLabel;
    @FXML private TableView<Book> booksTable;
    @FXML private TableColumn<Book, Integer> idColumn;
    @FXML private TableColumn<Book, String> titleColumn;
    @FXML private TableColumn<Book, String> authorColumn;
    @FXML private TableColumn<Book, String> isbnColumn;
    @FXML private TableColumn<Book, Integer> yearColumn;
    @FXML private TableColumn<Book, Double> priceColumn;
    @FXML private TableColumn<Book, String> statusColumn;
    @FXML private TableColumn<Book, Integer> quantityColumn;
    @FXML private TextField searchField;

    private User currentUser;
    private BookDAO bookDAO = new BookDAO();
    private ObservableList<Book> booksList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(cellData -> {
            Book book = cellData.getValue();
            return book != null ? book.idProperty().asObject() : null;
        });

        titleColumn.setCellValueFactory(cellData -> {
            Book book = cellData.getValue();
            return book != null ? book.titleProperty() : null;
        });

        authorColumn.setCellValueFactory(cellData -> {
            Book book = cellData.getValue();
            return book != null ? book.authorProperty() : null;
        });

        isbnColumn.setCellValueFactory(cellData -> {
            Book book = cellData.getValue();
            return book != null ? book.isbnProperty() : null;
        });

        yearColumn.setCellValueFactory(cellData -> {
            Book book = cellData.getValue();
            return book != null ? book.yearProperty().asObject() : null;
        });

        priceColumn.setCellValueFactory(cellData -> {
            Book book = cellData.getValue();
            return book != null ? book.priceProperty().asObject() : null;
        });

        statusColumn.setCellValueFactory(cellData -> {
            Book book = cellData.getValue();
            return book != null ? book.statusProperty() : null;
        });

        quantityColumn.setCellValueFactory(cellData -> {
            Book book = cellData.getValue();
            return book != null ? book.availableQuantityProperty().asObject() : null;
        });

        statusColumn.setCellFactory(col -> new TableCell<Book, String>() {
            @Override
            protected void updateItem(String status, boolean empty) {
                super.updateItem(status, empty);
                if (empty || status == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(status);
                    if ("DOSTUPNO".equals(status)) {
                        setStyle("-fx-text-fill: #27ae60; -fx-font-weight: bold;");
                    } else {
                        setStyle("-fx-text-fill: #e74c3c; -fx-font-weight: bold;");
                    }
                }
            }
        });

        priceColumn.setCellFactory(col -> new TableCell<Book, Double>() {
            @Override
            protected void updateItem(Double price, boolean empty) {
                super.updateItem(price, empty);
                if (empty || price == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(String.format("%.2f KM", price));
                    setStyle("-fx-text-fill: #2c3e50;");
                }
            }
        });

        booksTable.setStyle("-fx-text-base-color: #2c3e50;");
        booksTable.setItems(booksList);
        loadBooks();

        searchField.textProperty().addListener((obs, oldVal, newVal) -> filterBooks(newVal));
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
        welcomeLabel.setText("Dobrodošli: " + user.getFullName() + " (Administrator)");
    }

    private void loadBooks() {
        try {
            booksList.clear();
            java.util.List<Book> books = bookDAO.readAll();
            booksList.addAll(books);
        } catch (Exception e) {
            showAlert("Greška", "Greška pri učitavanju knjiga: " + e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    private void filterBooks(String searchText) {
        if (searchText == null || searchText.trim().isEmpty()) {
            loadBooks();
            return;
        }

        try {
            ObservableList<Book> filtered = FXCollections.observableArrayList();
            String search = searchText.toLowerCase();

            for (Book book : bookDAO.readAll()) {
                if (book.getTitle().toLowerCase().contains(search) ||
                        book.getAuthor().toLowerCase().contains(search) ||
                        book.getIsbn().toLowerCase().contains(search)) {
                    filtered.add(book);
                }
            }
            booksList.clear();
            booksList.addAll(filtered);
        } catch (Exception e) {
            showAlert("Greška", "Greška pri pretraživanju: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleAddBook() {
        try {
            FXMLLoader loader = new FXMLLoader(AdminMainController.class.getResource("/com/biblioteka/view/book_form.fxml"));
            Parent root = loader.load();

            BookFormController controller = loader.getController();
            controller.setAdminController(this);

            Stage stage = new Stage();
            stage.setTitle("Dodaj novu knjigu");
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch (Exception e) {
            showAlert("Greška", "Greška pri otvaranju forme: " + e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    @FXML
    private void handleEditBook() {
        Book selected = booksTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Upozorenje", "Molimo odaberite knjigu za uređivanje!", Alert.AlertType.WARNING);
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(AdminMainController.class.getResource("/com/biblioteka/view/book_form.fxml"));
            Parent root = loader.load();

            BookFormController controller = loader.getController();
            controller.setAdminController(this);
            controller.setBook(selected);

            Stage stage = new Stage();
            stage.setTitle("Uredi knjigu");
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch (Exception e) {
            showAlert("Greška", "Greška pri otvaranju forme: " + e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    @FXML
    private void handleDeleteBook() {
        Book selected = booksTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Upozorenje", "Molimo odaberite knjigu za brisanje!", Alert.AlertType.WARNING);
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Potvrda brisanja");
        confirm.setHeaderText("Da li ste sigurni da želite obrisati ovu knjigu?");
        confirm.setContentText(selected.getTitle() + " - " + selected.getAuthor());

        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                bookDAO.delete(selected.getId());
                loadBooks();
                showAlert("Uspjeh", "Knjiga uspješno obrisana!", Alert.AlertType.INFORMATION);
            } catch (Exception e) {
                showAlert("Greška", "Greška pri brisanju: " + e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    private void handleExportPDF() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Sačuvaj PDF");
        fileChooser.setInitialFileName("knjige_izvjestaj.pdf");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("PDF Files", "*.pdf")
        );

        File file = fileChooser.showSaveDialog(booksTable.getScene().getWindow());
        if (file != null) {
            try {
                PDFExporter.exportBooksToPDF(booksList, file.getAbsolutePath());
                showAlert("Uspjeh", "PDF izvještaj uspješno kreiran!\n" + file.getAbsolutePath(),
                        Alert.AlertType.INFORMATION);
            } catch (Exception e) {
                showAlert("Greška", "Greška pri kreiranju PDF-a: " + e.getMessage(), Alert.AlertType.ERROR);
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void handleRefresh() {
        searchField.clear();
        loadBooks();
    }

    @FXML
    private void handleLogout() {
        try {
            FXMLLoader loader = new FXMLLoader(AdminMainController.class.getResource("/com/biblioteka/view/login.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) welcomeLabel.getScene().getWindow();
            stage.setScene(new Scene(root, 400, 350));
            stage.setTitle("Prijava - Biblioteka");
            stage.centerOnScreen();
        } catch (Exception e) {
            showAlert("Greška", "Greška pri odjavi!", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    public void refreshTable() {
        loadBooks();
    }

    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
