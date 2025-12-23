package com.biblioteka;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import com.biblioteka.database.DatabaseConnection;
import javafx.scene.Parent;


public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // Inicijalizacija baze podataka
            DatabaseConnection.getConnection();

            // Učitavanje login ekrana
            FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("/com/biblioteka/view/login.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root, 800, 600);
            primaryStage.setTitle("Prijava - Biblioteka");
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Greška pri pokretanju aplikacije: " + e.getMessage());
        }
    }

    @Override
    public void stop() {
        DatabaseConnection.closeConnection();
        System.out.println("Aplikacija zatvorena.");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
