package project.bank.client.viewmodel;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import project.bank.client.model.SessionManager;
import project.bank.client.model.Utilisateur;
import project.bank.server.impl.BanqueServiceImpl;

import java.net.URL;
import java.util.ResourceBundle;

public class ClientDeposerVM implements Initializable {

    @FXML private TextField compteField;
    @FXML private TextField montantField;
    @FXML private Button deposerBtn;
    @FXML private Button emptyBtn;
    @FXML private Label nomclient2;
    @FXML private Label messageLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Display current user's name
        Utilisateur currentUser = SessionManager.getInstance().getCurrentUser();
        if (currentUser != null) {
            String fullName = (currentUser.getNom() != null ? currentUser.getNom() : "") +
                    " " +
                    (currentUser.getPrenom() != null ? currentUser.getPrenom() : "");
            nomclient2.setText(fullName.trim());
        }
    }

    @FXML
    private void handleDeposit() {
        String numeroCompte = compteField.getText().trim();
        String montantText = montantField.getText().trim();

        if (numeroCompte.isEmpty() || montantText.isEmpty()) {
            showMessage("Please fill all fields", true);
            return;
        }

        try {
            double montant = Double.parseDouble(montantText);

            if (montant <= 0) {
                showMessage("Amount must be greater than 0", true);
                return;
            }

            BanqueServiceImpl service = new BanqueServiceImpl();
            boolean success = service.deposer(numeroCompte, montant);

            if (success) {
                showMessage("✅ Deposit successful! +" + montant + " DH", false);
                handleEmpty();
            } else {
                showMessage("❌ Deposit failed. Check account number.", true);
            }

        } catch (NumberFormatException e) {
            showMessage("❌ Invalid amount format", true);
        } catch (Exception e) {
            showMessage("❌ Error: " + e.getMessage(), true);
            e.printStackTrace();
        }
    }

    @FXML
    private void handleEmpty() {
        compteField.setText("");
        montantField.setText("");
        messageLabel.setText("");
    }

    private void showMessage(String message, boolean isError) {
        if (messageLabel != null) {
            messageLabel.setText(message);
            messageLabel.setStyle(isError ?
                    "-fx-text-fill: red; -fx-font-weight: bold;" :
                    "-fx-text-fill: green; -fx-font-weight: bold;");
        }
    }
}