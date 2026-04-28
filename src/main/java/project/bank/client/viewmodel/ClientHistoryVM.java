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
import project.bank.client.model.SessionManager;
import project.bank.client.model.Transaction;
import project.bank.client.model.Utilisateur;
import project.bank.server.impl.BanqueServiceImpl;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ClientHistoryVM implements Initializable {

    @FXML private TableView<Transaction> tableView;
    @FXML private TableColumn<Transaction, String> colDateHeure;
    @FXML private TableColumn<Transaction, String> colType;
    @FXML private TableColumn<Transaction, Double> colMontant;
    @FXML private TableColumn<Transaction, String> colCompteSource;
    @FXML private TableColumn<Transaction, String> colCompteDest;
    @FXML private Label nomclient2;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colDateHeure.setCellValueFactory(new PropertyValueFactory<>("formattedDateOp"));
        colType.setCellValueFactory(new PropertyValueFactory<>("typeOp"));
        colMontant.setCellValueFactory(new PropertyValueFactory<>("montant"));
        colCompteSource.setCellValueFactory(new PropertyValueFactory<>("numeroCompte"));
        colCompteDest.setCellValueFactory(new PropertyValueFactory<>("compteDest"));

        // display current user name
        Utilisateur currentUser = SessionManager.getInstance().getCurrentUser();
        if (currentUser != null) {
            String fullName = (currentUser.getNom() != null ? currentUser.getNom() : "") +
                    " " +
                    (currentUser.getPrenom() != null ? currentUser.getPrenom() : "");
            nomclient2.setText(fullName.trim());

            // load transaction history
            loadHistory(currentUser.getUsername());
        }
    }

    private void loadHistory(String username) {
        try {
            BanqueServiceImpl service = new BanqueServiceImpl();

            // get all accounts for this user
            List<Compte> comptes = service.listerComptes(username);

            ObservableList<Transaction> allTransactions = FXCollections.observableArrayList();

            // get history for each account
            for (Compte compte : comptes) {
                List<Transaction> transactions = service.getHistorique(compte.getNumeroCompte());
                if (transactions != null) {
                    allTransactions.addAll(transactions);
                }
            }

            // sort by date
            FXCollections.sort(allTransactions, (t1, t2) -> {
                if (t1.getDateOp() == null || t2.getDateOp() == null) return 0;
                return t2.getDateOp().compareTo(t1.getDateOp());
            });

            tableView.setItems(allTransactions);
            System.out.println("load " + allTransactions.size() + " transactions");

        } catch (Exception e) {
            System.err.println("error loading history: " + e.getMessage());
            e.printStackTrace();
        }
    }
}