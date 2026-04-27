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

public class AdminDeleteCompteVM implements Initializable {

    @FXML
    private TextField accountNumberField;

    @FXML
    private Button deleteButton;

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
    private void deleteAccount() {
        String accountNumber = accountNumberField.getText().trim();

        // Validation
        if (accountNumber.isEmpty()) {
            showError("Please enter an account number");
            return;
        }

        try {
            boolean success = service.supprimerCompte(accountNumber);

            if (success) {
                statusLabel.setTextFill(Color.web("#5DCF5D"));
                statusLabel.setText("✓ Account " + accountNumber + " deleted successfully!");
                accountNumberField.clear();
            } else {
                showError("Failed to delete account. Account may not exist.");
            }
        } catch (Exception e) {
            showError("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void cancel() {
        accountNumberField.clear();
        statusLabel.setText("");
    }

    private void showError(String message) {
        statusLabel.setTextFill(Color.web("#FF6B6B"));
        statusLabel.setText("✗ " + message);
    }
}