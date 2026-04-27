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

public class ClientVirementVM implements Initializable {

    @FXML private TextField compteField;     // Source account
    @FXML private TextField compteField1;    // Destination account
    @FXML private TextField montantField;    // Amount
    @FXML private Button deposerBtn;         // VIREMENT button
    @FXML private Button emptyBtn;           // EMPTY button
    @FXML private Label nomclient2;          // Client name
    @FXML private Label messageLabel;        // Feedback message

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
        String compteSource = compteField.getText().trim();
        String compteDest = compteField1.getText().trim();
        String montantText = montantField.getText().trim();

        // Validate all fields
        if (compteSource.isEmpty() || compteDest.isEmpty() || montantText.isEmpty()) {
            showMessage("Please fill all fields", true);
            return;
        }

        // Check that source and destination are different
        if (compteSource.equals(compteDest)) {
            showMessage("❌ Source and destination accounts must be different", true);
            return;
        }

        try {
            double montant = Double.parseDouble(montantText);

            if (montant <= 0) {
                showMessage("Amount must be greater than 0", true);
                return;
            }

            BanqueServiceImpl service = new BanqueServiceImpl();
            boolean success = service.virement(compteSource, compteDest, montant);

            if (success) {
                showMessage("✅ Transfer successful!\n" + montant + " DH from " + compteSource + " to " + compteDest, false);
                handleEmpty();
            } else {
                showMessage("❌ Transfer failed. Check accounts and balance.", true);
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
        compteField1.setText("");
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