package com.biblioteka.controller;


import com.biblioteka.dao.UserDAO;
import com.biblioteka.model.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class LoginController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label messageLabel;

    private UserDAO userDAO = new UserDAO();

    @FXML
    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showMessage("Molimo unesite korisničko ime i lozinku!", false);
            return;
        }

        try {
            User user = userDAO.authenticate(username, password);
            if (user != null) {
                showMessage("Uspješna prijava! Dobrodošli " + user.getFullName(), true);
                openMainWindow(user);
            } else {
                showMessage("Pogrešno korisničko ime ili lozinka!", false);
            }
        } catch (Exception e) {
            showMessage("Greška pri prijavi: " + e.getMessage(), false);
            e.printStackTrace();
        }
    }

    private void openMainWindow(User user) {
        try {
            FXMLLoader loader = new FXMLLoader(LoginController.class.getResource("/com/biblioteka/view/main.fxml"));
            Parent root = loader.load();

            MainController controller = loader.getController();
            controller.setCurrentUser(user);

            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(new Scene(root, 1000, 700));
            stage.setTitle("Biblioteka - Sistem za upravljanje knjigama");
            stage.centerOnScreen();
        } catch (Exception e) {
            showMessage("Greška pri otvaranju glavnog prozora!", false);
            e.printStackTrace();
        }
    }

    private void showMessage(String message, boolean success) {
        messageLabel.setText(message);
        messageLabel.setStyle(success ? "-fx-text-fill: green;" : "-fx-text-fill: red;");
    }

    @FXML
    private void handleExit() {
        System.exit(0);
    }
}