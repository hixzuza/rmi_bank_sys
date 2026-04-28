package project.bank.client.viewmodel;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import project.bank.client.model.SessionManager;
import project.bank.client.model.Utilisateur;
import project.bank.commun.ClientRMI;
import project.bank.commun.IBanqueService;

import java.net.URL;
import java.rmi.RemoteException;
import java.util.ResourceBundle;

public class AdminCreateCompteVM implements Initializable {

    @FXML
    private TextField firstNameField;

    @FXML
    private TextField lastNameField;

    @FXML
    private TextField balanceField;

    @FXML
    private Label adminName;

    @FXML
    private Label statusLabel;

    private IBanqueService service;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            // Connect to RMI server
            service = ClientRMI.getService();

            // Set admin name from session
            Utilisateur currentUser = SessionManager.getInstance().getCurrentUser();
            if (currentUser != null) {
                String adminFullName = (currentUser.getNom() != null ? currentUser.getNom() : "") + " " +
                        (currentUser.getPrenom() != null ? currentUser.getPrenom() : "");
                adminName.setText(adminFullName.trim().isEmpty() ? "Admin" : adminFullName.trim());
            } else {
                adminName.setText("Admin");
            }

        } catch (Exception e) {
            e.printStackTrace();
            showError("failed to connect to server: " + e.getMessage());
        }
    }

    @FXML
    private void createAccount() {
        String firstName = firstNameField.getText().trim();
        String lastName = lastNameField.getText().trim();
        String balanceText = balanceField.getText().trim();

        // Validation
        if (firstName.isEmpty()) {
            showError("please enter client first name");
            return;
        }

        if (lastName.isEmpty()) {
            showError("please enter client last name");
            return;
        }

        if (balanceText.isEmpty()) {
            showError("please enter a balance");
            return;
        }

        double balance;
        try {
            balance = Double.parseDouble(balanceText);
            if (balance < 0) {
                showError("balance cannot be negative");
                return;
            }
        } catch (NumberFormatException e) {
            showError("please enter a valid number for balance");
            return;
        }

        try {
            // Combine first name and last name to pass as "titulaire"
            String fullName = firstName + " " + lastName;

            // Call RMI method with full name and balance
            boolean success = service.creerCompte(fullName, balance);

            if (success) {
                statusLabel.setTextFill(Color.web("#5DCF5D"));
                statusLabel.setText("account created successfully for: " + firstName + " " + lastName);
                firstNameField.clear();
                lastNameField.clear();
                balanceField.clear();
            } else {
                showError("failed to create account. Client not found with name: " + firstName + " " + lastName);
            }
        } catch (RemoteException e) {
            showError("server error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void cancel() {
        firstNameField.clear();
        lastNameField.clear();
        balanceField.clear();
        statusLabel.setText("");
    }

    private void showError(String message) {
        statusLabel.setTextFill(Color.web("#FF6B6B"));
        statusLabel.setText("✗ " + message);
    }
}