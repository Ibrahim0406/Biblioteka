package com.biblioteka.controller;

import com.biblioteka.dao.BookDAO;
import com.biblioteka.dao.LoanDAO;
import com.biblioteka.model.Book;
import com.biblioteka.model.Loan;
import com.biblioteka.model.User;
import java.time.LocalDate;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.util.Optional;

public class UserMainController {
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

    @FXML private TableView<Loan> loansTable;
    @FXML private TableColumn<Loan, Integer> loanIdColumn;
    @FXML private TableColumn<Loan, String> loanBookTitleColumn;
    @FXML private TableColumn<Loan, LocalDate> loanDateColumn;

    private User currentUser;
    private BookDAO bookDAO = new BookDAO();
    private LoanDAO loanDAO = new LoanDAO();
    private ObservableList<Book> booksList = FXCollections.observableArrayList();
    private ObservableList<Loan> loansList = FXCollections.observableArrayList();

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

        loanIdColumn.setCellValueFactory(cellData -> {
            Loan loan = cellData.getValue();
            return loan != null ? loan.idProperty().asObject() : null;
        });

        loanBookTitleColumn.setCellValueFactory(cellData -> {
            Loan loan = cellData.getValue();
            return loan != null ? loan.bookTitleProperty() : null;
        });

        loanDateColumn.setCellValueFactory(cellData -> {
            Loan loan = cellData.getValue();
            return loan != null ? loan.loanDateProperty() : null;
        });

        booksTable.setStyle("-fx-text-base-color: #2c3e50;");
        booksTable.setItems(booksList);
        loansTable.setStyle("-fx-text-base-color: #2c3e50;");
        loansTable.setItems(loansList);

        loadBooks();

        searchField.textProperty().addListener((obs, oldVal, newVal) -> filterBooks(newVal));
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
        welcomeLabel.setText("Dobrodošli: " + user.getFullName() + " (Korisnik)");
        loadUserLoans();
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

    private void loadUserLoans() {
        try {
            loansList.clear();
            if (currentUser != null) {
                java.util.List<Loan> loans = loanDAO.getLoansByBorrower(currentUser.getFullName());
                loansList.addAll(loans);
            }
        } catch (Exception e) {
            showAlert("Greška", "Greška pri učitavanju pozajmica: " + e.getMessage(), Alert.AlertType.ERROR);
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
    private void handleBorrowBook() {
        Book selected = booksTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Upozorenje", "Molimo odaberite knjigu za pozajmljivanje!", Alert.AlertType.WARNING);
            return;
        }

        if (selected.getAvailableQuantity() <= 0) {
            showAlert("Upozorenje", "Ova knjiga trenutno nije dostupna!", Alert.AlertType.WARNING);
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Potvrda pozajmljivanja");
        confirm.setHeaderText("Da li želite pozajmiti ovu knjigu?");
        confirm.setContentText(selected.getTitle() + " - " + selected.getAuthor());

        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                Loan loan = new Loan(0, selected.getId(), selected.getTitle(),
                        currentUser.getFullName(), LocalDate.now(), null);
                loanDAO.create(loan);

                bookDAO.decreaseQuantity(selected.getId());

                loadBooks();
                loadUserLoans();
                showAlert("Uspjeh", "Knjiga uspješno pozajmljena!", Alert.AlertType.INFORMATION);
            } catch (Exception e) {
                showAlert("Greška", "Greška pri pozajmljivanju: " + e.getMessage(), Alert.AlertType.ERROR);
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void handleReturnBook() {
        Loan selected = loansTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Upozorenje", "Molimo odaberite knjigu za vraćanje!", Alert.AlertType.WARNING);
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Potvrda vraćanja");
        confirm.setHeaderText("Da li želite vratiti ovu knjigu?");
        confirm.setContentText(selected.getBookTitle());

        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                // Mark loan as returned
                loanDAO.returnBook(selected.getId());

                // Increase book quantity back
                bookDAO.increaseQuantity(selected.getBookId());

                // Reload both tables
                loadBooks();
                loadUserLoans();

                showAlert("Uspjeh", "Knjiga uspješno vraćena!", Alert.AlertType.INFORMATION);
            } catch (Exception e) {
                showAlert("Greška", "Greška pri vraćanju knjige: " + e.getMessage(), Alert.AlertType.ERROR);
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void handleRefresh() {
        searchField.clear();
        loadBooks();
        loadUserLoans();
    }

    @FXML
    private void handleLogout() {
        try {
            FXMLLoader loader = new FXMLLoader(UserMainController.class.getResource("/com/biblioteka/view/login.fxml"));
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

    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
