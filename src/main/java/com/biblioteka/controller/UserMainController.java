package com.biblioteka.controller;

import com.biblioteka.dao.BookDAO;
import com.biblioteka.dao.LoanDAO;
import com.biblioteka.dao.ReviewDAO;
import com.biblioteka.dao.ReservationDAO;
import com.biblioteka.model.Book;
import com.biblioteka.model.BookWithRating;
import com.biblioteka.model.Loan;
import com.biblioteka.model.User;
import com.biblioteka.model.Reservation;
import java.time.LocalDate;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.Modality;
import java.util.Optional;

public class UserMainController {
    @FXML
    private Label welcomeLabel;
    @FXML
    private TableView<BookWithRating> booksTable;
    @FXML
    private TableColumn<BookWithRating, Integer> idColumn;
    @FXML
    private TableColumn<BookWithRating, String> titleColumn;
    @FXML
    private TableColumn<BookWithRating, String> authorColumn;
    @FXML
    private TableColumn<BookWithRating, String> isbnColumn;
    @FXML
    private TableColumn<BookWithRating, Integer> yearColumn;
    @FXML
    private TableColumn<BookWithRating, Double> priceColumn;
    @FXML
    private TableColumn<BookWithRating, String> statusColumn;
    @FXML
    private TableColumn<BookWithRating, Integer> quantityColumn;
    @FXML
    private TableColumn<BookWithRating, String> ratingColumn;
    @FXML
    private TextField searchField;

    @FXML
    private TableView<Loan> loansTable;
    @FXML
    private TableColumn<Loan, Integer> loanIdColumn;
    @FXML
    private TableColumn<Loan, String> loanBookTitleColumn;
    @FXML
    private TableColumn<Loan, LocalDate> loanDateColumn;

    @FXML
    private TableView<Reservation> reservationsTable;
    @FXML
    private TableColumn<Reservation, Integer> reservationIdColumn;
    @FXML
    private TableColumn<Reservation, String> reservationBookTitleColumn;
    @FXML
    private TableColumn<Reservation, LocalDate> reservationDateColumn;

    private User currentUser;
    private BookDAO bookDAO = new BookDAO();
    private LoanDAO loanDAO = new LoanDAO();
    private ReviewDAO reviewDAO = new ReviewDAO();
    private ReservationDAO reservationDAO = new ReservationDAO();
    private ObservableList<BookWithRating> booksList = FXCollections.observableArrayList();
    private ObservableList<Loan> loansList = FXCollections.observableArrayList();
    private ObservableList<Reservation> reservationsList = FXCollections.observableArrayList();

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

        ratingColumn.setCellValueFactory(cellData -> {
            BookWithRating book = cellData.getValue();
            if (book != null && book.getAverageRating() > 0) {
                String stars = "★".repeat((int) Math.round(book.getAverageRating())) +
                        "☆".repeat(5 - (int) Math.round(book.getAverageRating()));
                return new javafx.beans.property.SimpleStringProperty(
                        String.format("%s (%.1f)", stars, book.getAverageRating())
                );
            }
            return new javafx.beans.property.SimpleStringProperty("Nema ocjena");
        });

        statusColumn.setCellFactory(col -> new TableCell<BookWithRating, String>() {
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

        priceColumn.setCellFactory(col -> new TableCell<BookWithRating, Double>() {
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

        reservationIdColumn.setCellValueFactory(cellData -> {
            Reservation reservation = cellData.getValue();
            return reservation != null ? reservation.idProperty().asObject() : null;
        });

        reservationBookTitleColumn.setCellValueFactory(cellData -> {
            Reservation reservation = cellData.getValue();
            return reservation != null ? reservation.bookTitleProperty() : null;
        });

        reservationDateColumn.setCellValueFactory(cellData -> {
            Reservation reservation = cellData.getValue();
            return reservation != null ? reservation.reservationDateProperty() : null;
        });

        booksTable.setStyle("-fx-text-base-color: #2c3e50;");
        booksTable.setItems(booksList);
        loansTable.setStyle("-fx-text-base-color: #2c3e50;");
        loansTable.setItems(loansList);
        reservationsTable.setStyle("-fx-text-base-color: #2c3e50;");
        reservationsTable.setItems(reservationsList);

        loadBooks();

        searchField.textProperty().addListener((obs, oldVal, newVal) -> filterBooks(newVal));
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
        welcomeLabel.setText("Dobrodošli: " + user.getFullName() + " (Korisnik)");
        loadUserLoans();
        loadUserReservations();
    }

    private void loadBooks() {
        try {
            booksList.clear();
            java.util.List<BookWithRating> books = bookDAO.getAllBooksWithRatings();
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

    private void loadUserReservations() {
        try {
            reservationsList.clear();
            if (currentUser != null) {
                java.util.List<Reservation> reservations = reservationDAO.getReservationsByUser(currentUser.getUsername());
                reservationsList.addAll(reservations);
            }
        } catch (Exception e) {
            showAlert("Greška", "Greška pri učitavanju rezervacija: " + e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    private void filterBooks(String searchText) {
        if (searchText == null || searchText.trim().isEmpty()) {
            loadBooks();
            return;
        }

        try {
            ObservableList<BookWithRating> filtered = FXCollections.observableArrayList();
            String search = searchText.toLowerCase();

            for (BookWithRating book : bookDAO.getAllBooksWithRatings()) {
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
        BookWithRating selected = booksTable.getSelectionModel().getSelectedItem();
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
                loadUserReservations();
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
                loanDAO.returnBook(selected.getId());
                bookDAO.increaseQuantity(selected.getBookId());

                loadBooks();
                loadUserLoans();
                loadUserReservations();

                showAlert("Uspjeh", "Knjiga uspješno vraćena!", Alert.AlertType.INFORMATION);
            } catch (Exception e) {
                showAlert("Greška", "Greška pri vraćanju knjige: " + e.getMessage(), Alert.AlertType.ERROR);
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void handleReserveBook() {
        BookWithRating selected = booksTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Upozorenje", "Molimo odaberite knjigu za rezervaciju!", Alert.AlertType.WARNING);
            return;
        }

        if (selected.getAvailableQuantity() > 0) {
            showAlert("Upozorenje", "Ova knjiga je trenutno dostupna za pozajmljivanje. Nema potrebe za rezervacijom!", Alert.AlertType.INFORMATION);
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Potvrda rezervacije");
        confirm.setHeaderText("Da li želite rezervisati ovu knjigu?");
        confirm.setContentText(selected.getTitle() + " - " + selected.getAuthor() +
                "\n\nBićete obavješteni kada knjiga postane dostupna.");

        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                Reservation reservation = new Reservation(
                        0,
                        selected.getId(),
                        selected.getTitle(),
                        currentUser.getUsername(),
                        LocalDate.now(),
                        "AKTIVNA"
                );
                reservationDAO.create(reservation);

                loadUserReservations();
                showAlert("Uspjeh", "Knjiga uspješno rezervisana!", Alert.AlertType.INFORMATION);
            } catch (Exception e) {
                showAlert("Greška", "Greška pri rezervaciji: " + e.getMessage(), Alert.AlertType.ERROR);
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void handleCancelReservation() {
        Reservation selected = reservationsTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Upozorenje", "Molimo odaberite rezervaciju za otkazivanje!", Alert.AlertType.WARNING);
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Potvrda otkazivanja");
        confirm.setHeaderText("Da li želite otkazati ovu rezervaciju?");
        confirm.setContentText(selected.getBookTitle());

        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                reservationDAO.cancelReservation(selected.getId());
                loadUserReservations();
                showAlert("Uspjeh", "Rezervacija uspješno otkazana!", Alert.AlertType.INFORMATION);
            } catch (Exception e) {
                showAlert("Greška", "Greška pri otkazivanju: " + e.getMessage(), Alert.AlertType.ERROR);
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void handleLeaveReview() {
        BookWithRating selected = booksTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Upozorenje", "Molimo odaberite knjigu za recenziju!", Alert.AlertType.WARNING);
            return;
        }

        try {
            // Provjeri da li je korisnik vratio ovu knjigu
            java.util.List<Loan> returnedLoans = loanDAO.getReturnedLoansByUserAndBook(
                    currentUser.getFullName(),
                    selected.getId()
            );

            if (returnedLoans.isEmpty()) {
                showAlert("Upozorenje", "Možete ostaviti recenziju samo za knjige koje ste već vratili!", Alert.AlertType.WARNING);
                return;
            }

            // Provjeri da li je korisnik već ostavio recenziju za ovu knjigu
            if (reviewDAO.hasUserReviewedBook(currentUser.getUsername(), selected.getId())) {
                showAlert("Upozorenje", "Već ste ostavili recenziju za ovu knjigu!", Alert.AlertType.INFORMATION);
                return;
            }

            // Otvori formu za recenziju
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/biblioteka/view/review_form.fxml"));
            Parent root = loader.load();

            ReviewFormController controller = loader.getController();
            controller.setBook(selected);
            controller.setCurrentUser(currentUser);
            controller.setUserMainController(this);

            Stage stage = new Stage();
            stage.setTitle("Ostavi Recenziju");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

        } catch (Exception e) {
            showAlert("Greška", "Greška pri otvaranju forme za recenziju: " + e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    @FXML
    public void handleRefresh() {
        searchField.clear();
        loadBooks();
        loadUserLoans();
        loadUserReservations();
    }

    public void refreshTables() {
        loadBooks();
        loadUserLoans();
        loadUserReservations();
    }

    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
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
}

