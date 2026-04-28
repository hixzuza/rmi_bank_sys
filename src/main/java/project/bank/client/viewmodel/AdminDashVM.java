package project.bank.client.viewmodel;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import project.bank.client.model.SessionManager;

import java.io.IOException;

public class AdminDashVM {

    @FXML
    private StackPane stackPane;

    @FXML
    private void ListUSER() {
        loadView("/project/bank/view/AdminListAcc.fxml");
    }

    @FXML
    private void createACC() {
        loadView("/project/bank/view/AdminCreateCompte.fxml");
    }

    @FXML
    private void delectACC() {
        loadView("/project/bank/view/AdminDeleteCompte.fxml");
    }
    @FXML
    private void switchU() {
        try {
            Stage stage = (Stage) stackPane.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/project/bank/view/Login.fxml"));
            Parent loginView = loader.load();

            stage.getScene().setRoot(loginView);
            stage.setTitle("BANK SYSTEM");
            stage.setWidth(360);
            stage.setHeight(300);  // Make sure this is set
            stage.setMinWidth(360);
            stage.setMinHeight(300);
            stage.setResizable(false);
            stage.centerOnScreen();

            SessionManager.getInstance().clear();

        } catch (IOException e) {
            System.out.println("error loading login view");
            e.printStackTrace();
        }
    }

    private void loadView(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent view = loader.load();
            stackPane.getChildren().setAll(view);
        } catch (IOException e) {
            System.out.println("error loading view: " + fxmlPath);
            e.printStackTrace();
        }
    }
}