package com.biblioteka.controller;

import com.biblioteka.dao.ReviewDAO;
import com.biblioteka.model.Book;
import com.biblioteka.model.Review;
import com.biblioteka.model.User;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.time.LocalDate;

public class ReviewFormController {
    @FXML private Label bookTitleLabel;
    @FXML private ToggleButton star1;
    @FXML private ToggleButton star2;
    @FXML private ToggleButton star3;
    @FXML private ToggleButton star4;
    @FXML private ToggleButton star5;
    @FXML private Label ratingLabel;
    @FXML private TextArea commentArea;

    private Book book;
    private User currentUser;
    private int selectedRating = 0;
    private ReviewDAO reviewDAO = new ReviewDAO();
    private UserMainController userMainController;

    @FXML
    public void initialize() {
        // Initialize star buttons
        updateStarDisplay();
    }

    public void setBook(Book book) {
        this.book = book;
        bookTitleLabel.setText(book.getTitle() + " - " + book.getAuthor());
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    public void setUserMainController(UserMainController controller) {
        this.userMainController = controller;
    }

    @FXML
    private void handleStarClick() {
        // Determine which star was clicked
        if (star1.isSelected()) selectedRating = Math.max(selectedRating, 1);
        if (star2.isSelected()) selectedRating = Math.max(selectedRating, 2);
        if (star3.isSelected()) selectedRating = Math.max(selectedRating, 3);
        if (star4.isSelected()) selectedRating = Math.max(selectedRating, 4);
        if (star5.isSelected()) selectedRating = Math.max(selectedRating, 5);

        // If clicked on already selected star, deselect
        if (!star1.isSelected() && selectedRating == 1) selectedRating = 0;
        if (!star2.isSelected() && selectedRating == 2) selectedRating = 1;
        if (!star3.isSelected() && selectedRating == 3) selectedRating = 2;
        if (!star4.isSelected() && selectedRating == 4) selectedRating = 3;
        if (!star5.isSelected() && selectedRating == 5) selectedRating = 4;

        updateStarDisplay();
    }

    private void updateStarDisplay() {
        // Update star colors based on selection
        star1.setSelected(selectedRating >= 1);
        star2.setSelected(selectedRating >= 2);
        star3.setSelected(selectedRating >= 3);
        star4.setSelected(selectedRating >= 4);
        star5.setSelected(selectedRating >= 5);

        star1.setStyle(selectedRating >= 1 ?
                "-fx-font-size: 24; -fx-background-color: transparent; -fx-text-fill: #f39c12;" :
                "-fx-font-size: 24; -fx-background-color: transparent; -fx-text-fill: #bdc3c7;");
        star2.setStyle(selectedRating >= 2 ?
                "-fx-font-size: 24; -fx-background-color: transparent; -fx-text-fill: #f39c12;" :
                "-fx-font-size: 24; -fx-background-color: transparent; -fx-text-fill: #bdc3c7;");
        star3.setStyle(selectedRating >= 3 ?
                "-fx-font-size: 24; -fx-background-color: transparent; -fx-text-fill: #f39c12;" :
                "-fx-font-size: 24; -fx-background-color: transparent; -fx-text-fill: #bdc3c7;");
        star4.setStyle(selectedRating >= 4 ?
                "-fx-font-size: 24; -fx-background-color: transparent; -fx-text-fill: #f39c12;" :
                "-fx-font-size: 24; -fx-background-color: transparent; -fx-text-fill: #bdc3c7;");
        star5.setStyle(selectedRating >= 5 ?
                "-fx-font-size: 24; -fx-background-color: transparent; -fx-text-fill: #f39c12;" :
                "-fx-font-size: 24; -fx-background-color: transparent; -fx-text-fill: #bdc3c7;");

        if (selectedRating == 0) {
            ratingLabel.setText("Nema ocjene");
        } else {
            ratingLabel.setText(selectedRating + "/5 zvjezdica");
        }
    }

    @FXML
    private void handleSave() {
        if (selectedRating == 0) {
            showAlert("Upozorenje", "Molimo odaberite ocjenu!", Alert.AlertType.WARNING);
            return;
        }

        String comment = commentArea.getText().trim();
        if (comment.isEmpty()) {
            showAlert("Upozorenje", "Molimo napišite recenziju!", Alert.AlertType.WARNING);
            return;
        }

        try {
            Review review = new Review(
                    0,
                    book.getId(),
                    book.getTitle(),
                    currentUser.getUsername(),
                    selectedRating,
                    comment,
                    LocalDate.now()
            );

            reviewDAO.create(review);
            showAlert("Uspjeh", "Recenzija uspješno sačuvana!", Alert.AlertType.INFORMATION);

            if (userMainController != null) {
                userMainController.handleRefresh();
            }

            closeWindow();
        } catch (Exception e) {
            showAlert("Greška", "Greška pri čuvanju recenzije: " + e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    @FXML
    private void handleCancel() {
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) bookTitleLabel.getScene().getWindow();
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
