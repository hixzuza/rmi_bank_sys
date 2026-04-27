package project.bank.client.viewmodel;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import project.bank.client.model.Compte;
import project.bank.client.model.Utilisateur;
import project.bank.client.model.SessionManager;
import project.bank.server.impl.BanqueServiceImpl;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ClientAllAccVM implements Initializable {

    @FXML
    private TableView<Compte> tableView;

    @FXML
    private TableColumn<Compte, String> colCompte;

    @FXML
    private TableColumn<Compte, Double> colBalence;

    @FXML
    private Label nomclient;


    private BanqueServiceImpl service;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            service = new BanqueServiceImpl();

            // Set up table columns
            colCompte.setCellValueFactory(new PropertyValueFactory<>("numeroCompte"));
            colBalence.setCellValueFactory(new PropertyValueFactory<>("solde"));

            // Format balance with 2 decimal places
            colBalence.setCellFactory(column -> new javafx.scene.control.TableCell<Compte, Double>() {
                @Override
                protected void updateItem(Double item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText(String.format("%.2f", item));
                    }
                }
            });

            // Get current user from session
            Utilisateur currentUser = SessionManager.getInstance().getCurrentUser();

            if (currentUser != null) {
                // Fetch full user details including nom and prenom
                Utilisateur fullUser = service.getUserByUsername(currentUser.getUsername());

                if (fullUser != null) {
                    // Build full name from nom and prenom
                    String nom = fullUser.getNom() != null ? fullUser.getNom() : "";
                    String prenom = fullUser.getPrenom() != null ? fullUser.getPrenom() : "";
                    String fullName = (nom + " " + prenom).trim();

                    // Set the client name (e.g., "A BB" or "M SS")
                    nomclient.setText(fullName.isEmpty() ? currentUser.getUsername() : fullName);

                    // Set username as secondary label
                } else {
                    nomclient.setText(currentUser.getUsername());

                }
            }

            // Load accounts
            loadAccounts();

        } catch (Exception e) {
            System.err.println("Error initializing: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadAccounts() {
        try {
            Utilisateur currentUser = SessionManager.getInstance().getCurrentUser();
            if (currentUser != null) {
                List<Compte> comptes = service.listerComptes(currentUser.getUsername());

                if (comptes != null && !comptes.isEmpty()) {
                    tableView.setItems(FXCollections.observableArrayList(comptes));
                    System.out.println("Loaded " + comptes.size() + " accounts");
                } else {
                    System.out.println("No accounts found for user: " + currentUser.getUsername());
                    tableView.setItems(FXCollections.observableArrayList());
                }
            }
        } catch (Exception e) {
            System.err.println("Error loading accounts: " + e.getMessage());
            e.printStackTrace();
        }
    }
}