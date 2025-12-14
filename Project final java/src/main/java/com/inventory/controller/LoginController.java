package com.inventory.controller;

import com.inventory.App;
import com.inventory.dao.UserDAO;
import com.inventory.model.User;
import com.inventory.util.SessionManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label errorLabel;

    private UserDAO userDAO = new UserDAO();

    @FXML
    public void handleLogin(ActionEvent event) {
        // recup champs
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Veuillez remplir tous les champs.");
            return;
        }

        // appel service auth
        User user = userDAO.authenticate(username, password);

        if (user != null) {
            errorLabel.setStyle("-fx-text-fill: green;");
            errorLabel.setText("Connexion réussie !");

            SessionManager.setCurrentUser(user);

            try {
                App.setRoot("dashboard");
            } catch (java.io.IOException e) {
                e.printStackTrace();
                errorLabel.setText("Erreur lors du chargement du Dashboard.");
            }
        } else {
            // affiche erreur rouge
            errorLabel.setStyle("-fx-text-fill: red;");
            errorLabel.setText("Nom d'utilisateur ou mot de passe incorrect.");
        }
    }
}
