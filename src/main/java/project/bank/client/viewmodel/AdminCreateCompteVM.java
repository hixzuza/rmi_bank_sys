
package project.bank.client.viewmodel;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import project.bank.client.model.SessionManager;
import project.bank.client.model.Utilisateur;
import project.bank.server.impl.BanqueServiceImpl;

import java.net.URL;
import java.util.ResourceBundle;

public class AdminCreateCompteVM implements Initializable {

    @FXML
    private TextField usernameField;

    @FXML
    private TextField balanceField;

    @FXML
    private Button createButton;

    @FXML
    private Button cancelButton;

    @FXML
    private Label adminName;

    @FXML
    private Label statusLabel;

    private BanqueServiceImpl service;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            service = new BanqueServiceImpl();

            // Set admin name
            Utilisateur currentUser = SessionManager.getInstance().getCurrentUser();
            if (currentUser != null) {
                String adminFullName = (currentUser.getNom() != null ? currentUser.getNom() : "") + " " +
                        (currentUser.getPrenom() != null ? currentUser.getPrenom() : "");
                adminName.setText(adminFullName.trim().isEmpty() ? "Admin" : adminFullName.trim());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void createAccount() {
        String username = usernameField.getText().trim();
        String balanceText = balanceField.getText().trim();

        // Validation
        if (username.isEmpty()) {
            showError("Please enter a username");
            return;
        }

        if (balanceText.isEmpty()) {
            showError("Please enter an initial balance");
            return;
        }

        double balance;
        try {
            balance = Double.parseDouble(balanceText);
            if (balance < 0) {
                showError("Balance cannot be negative");
                return;
            }
        } catch (NumberFormatException e) {
            showError("Please enter a valid number for balance");
            return;
        }

        try {
            boolean success = service.creerCompte(username, balance);

            if (success) {
                statusLabel.setTextFill(Color.web("#5DCF5D"));
                statusLabel.setText("✓ Account created successfully!");
                usernameField.clear();
                balanceField.clear();
            } else {
                showError("Failed to create account. User may not exist.");
            }
        } catch (Exception e) {
            showError("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void cancel() {
        usernameField.clear();
        balanceField.clear();
        statusLabel.setText("");
    }

    private void showError(String message) {
        statusLabel.setTextFill(Color.web("#FF6B6B"));
        statusLabel.setText("✗ " + message);
    }
}