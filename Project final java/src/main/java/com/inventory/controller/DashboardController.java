package com.inventory.controller;

import com.inventory.App;
import com.inventory.model.User;
import com.inventory.util.SessionManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class DashboardController {

    @FXML
    private Label welcomeLabel;

    @FXML
    private Button btnCategories;

    @FXML
    private VBox contentArea;

    @FXML
    public void initialize() {
        // init dashboard
        User user = SessionManager.getCurrentUser();

        if (user != null) {
            welcomeLabel.setText("Bienvenue, " + user.getUsername() + " !");

            // cache si pas admin
            if (!"ADMIN".equals(user.getRole())) {
                btnCategories.setVisible(false);
                btnCategories.setManaged(false);
            }
        }
    }

    @FXML
    public void showProducts(ActionEvent event) {
        // affichage produits
        loadView("products");
    }

    @FXML
    public void showCategories(ActionEvent event) {
        loadView("categories");
    }

    // methode pour charger fxml
    private void loadView(String fxml) {
        try {
            javafx.fxml.FXMLLoader fxmlLoader = new javafx.fxml.FXMLLoader(
                    App.class.getResource("/fxml/" + fxml + ".fxml"));
            javafx.scene.Parent view = fxmlLoader.load();
            contentArea.getChildren().setAll(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleLogout(ActionEvent event) throws IOException {
        SessionManager.clearSession();
        App.setRoot("login");
    }
}
