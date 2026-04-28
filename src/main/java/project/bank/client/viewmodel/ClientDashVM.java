package project.bank.client.viewmodel;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import project.bank.client.model.SessionManager;

import java.io.IOException;

public class ClientDashVM {

    @FXML
    private StackPane stackPane;

    @FXML
    private void ListAcc() {
        loadView("/project/bank/ClientAllAcc.fxml");
    }

    @FXML
    private void Deposer() {
        loadView("/project/bank/ClientDeposer.fxml");
    }

    @FXML
    private void Retirer() {
        loadView("/project/bank/ClientRetirer.fxml");
    }

    @FXML
    private void Virement() {
        loadView("/project/bank/ClientVirement.fxml");
    }

    @FXML
    private void History() {
        loadView("/project/bank/ClientHistory.fxml");
    }


    @FXML
    private void switchU() {
        try {
            Stage stage = (Stage) stackPane.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/project/bank/Login.fxml"));
            Parent loginView = loader.load();

            stage.getScene().setRoot(loginView);
            stage.setTitle("BANK SYSTEM");
            stage.setWidth(360);
            stage.setHeight(300);  // UNCOMMENT THIS - it was commented
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