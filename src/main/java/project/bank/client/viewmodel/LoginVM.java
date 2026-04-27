package project.bank.client.viewmodel;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import project.bank.client.model.SessionManager;
import project.bank.client.model.Utilisateur;
import project.bank.commun.ViewLoader;
import project.bank.server.impl.BanqueServiceImpl;

import java.rmi.RemoteException;
import java.sql.SQLException;

public class LoginVM {
    @FXML
    Label resLOGIN_label;
    @FXML
    PasswordField passField_login;
    @FXML
    TextField TxtField_login;
    @FXML
    Button login_btn, empty_btn;
    @FXML
    ToggleButton adm_cli_btn;
    @FXML
    CheckBox adminCheckBox;

    @FXML
    private void initialize() {
        if (adm_cli_btn != null) {
            adm_cli_btn.setText("CLIENT");
            adminCheckBox.setSelected(false);
        }
    }

    @FXML
    private void Empty_btn() {
        TxtField_login.setText("");
        passField_login.setText("");
        resLOGIN_label.setText("");
    }

    @FXML
    private void Login_btn() throws RemoteException, SQLException {
        String username = TxtField_login.getText();
        String password = passField_login.getText();
        boolean isAdmin = adminCheckBox.isSelected();

        if (username.isEmpty() || password.isEmpty()) {
            setMessage("\nPlease fill all fields", true);
            return;
        }

        BanqueServiceImpl service = new BanqueServiceImpl();
        boolean success = service.authentifier(username, password);
        System.out.println("login : " + success + "  username: " + username + " admin: " + isAdmin);

        if (success) {
            // Fetch complete user info from server
            Utilisateur user = service.getUserByUsername(username);

            if (user == null) {
                // If server doesn't return user, create basic user object
                user = new Utilisateur();
                user.setUsername(username);
                user.setRole(isAdmin ? "ADMIN" : "CLIENT");
            }

            // Store in session
            SessionManager.getInstance().setCurrentUser(user);

            setMessage("\nlogin successful", false);
            new Thread(() -> {
                try {
                    Thread.sleep(800);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                javafx.application.Platform.runLater(() -> {
                    Pane root = (Pane) login_btn.getScene().getRoot();
                    Stage stage = (Stage) login_btn.getScene().getWindow();

                    stage.setResizable(false);

                    if (isAdmin) {
                        ViewLoader.loadInto(root, "/project/bank/AdminDash.fxml");
                        stage.setTitle("BANK SYSTEM Admin Dashboard");
                    } else {
                        ViewLoader.loadInto(root, "/project/bank/ClientDash.fxml");
                        stage.setTitle("BANK SYSTEM Client Dashboard");
                    }

                    stage.setMinWidth(1300);
                    stage.setMinHeight(730);
                    stage.setWidth(1300);
                    stage.setHeight(730);
                    stage.setResizable(true);
                    stage.centerOnScreen();
                });
            }).start();

        } else {
            setMessage("\n invalid info \n\n try again ", true);
        }
    }

    public void setMessage(String message, boolean isError) {
        resLOGIN_label.setText(message);
        if (isError) {
            resLOGIN_label.setStyle("-fx-text-fill: red;");
        } else {
            resLOGIN_label.setStyle("-fx-text-fill: green;");
        }
    }

    public String getUsername() {
        return TxtField_login.getText();
    }

    public String getPassword() {
        return passField_login.getText();
    }
}