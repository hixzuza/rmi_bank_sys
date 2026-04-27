package project.bank.client.viewmodel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.ResourceBundle;
import project.bank.server.db.DatabaseConnection;

public class AdminListAccVM implements Initializable {

    @FXML
    private TableView<Compte> tableView;

    @FXML
    private TableColumn<Compte, String> colClient;

    @FXML
    private TableColumn<Compte, String> colNCompte;

    @FXML
    private TableColumn<Compte, Double> colBalance;

    @FXML
    private TableColumn<Compte, String> dateCreation;

    @FXML
    private Label nomclient2;

    private BanqueServiceImpl service;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            service = new BanqueServiceImpl();

            // Set up table columns
            colNCompte.setCellValueFactory(new PropertyValueFactory<>("numeroCompte"));
            colBalance.setCellValueFactory(new PropertyValueFactory<>("solde"));
            dateCreation.setCellValueFactory(new PropertyValueFactory<>("formattedDateCreation"));
            colClient.setCellValueFactory(new PropertyValueFactory<>("clientName"));

            // Set admin label
            Utilisateur currentUser = SessionManager.getInstance().getCurrentUser();
            if (currentUser != null) {
                String adminName = (currentUser.getNom() != null ? currentUser.getNom() : "") + " " +
                        (currentUser.getPrenom() != null ? currentUser.getPrenom() : "");
                nomclient2.setText(adminName.trim().isEmpty() ? "Admin" : adminName.trim());
            } else {
                nomclient2.setText("Admin");
            }

            // Load all accounts data
            loadAllAccounts();

        } catch (Exception e) {
            System.err.println("Error initializing: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadAllAccounts() {
        try {
            ObservableList<Compte> accountData = FXCollections.observableArrayList();

            // Get all clients directly from database
            String sql = "SELECT id_user, username, nom, prenom FROM UTILISATEUR WHERE role = 'CLIENT'";

            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql);
                 ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    String username = rs.getString("username");
                    String nom = rs.getString("nom");
                    String prenom = rs.getString("prenom");

                    String clientName = (nom != null ? nom : "") + " " + (prenom != null ? prenom : "");
                    clientName = clientName.trim();
                    if (clientName.isEmpty()) {
                        clientName = username;
                    }

                    // Get accounts for this user using existing service method
                    List<Compte> comptes = service.listerComptes(username);

                    if (comptes != null && !comptes.isEmpty()) {
                        for (Compte compte : comptes) {
                            compte.setClientName(clientName);
                            accountData.add(compte);
                        }
                    }
                }
            }

            tableView.setItems(accountData);
            System.out.println("Loaded " + accountData.size() + " account records from database");

        } catch (Exception e) {
            System.err.println("Error loading accounts from database: " + e.getMessage());
            e.printStackTrace();
        }
    }
}