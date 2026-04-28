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
    private void initialize() {
        // No more toggle button or checkbox initialization needed
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

        if (username.isEmpty() || password.isEmpty()) {
            setMessage("please fill all fields", true);
            return;
        }

        BanqueServiceImpl service = new BanqueServiceImpl();
        boolean success = service.authentifier(username, password);
        System.out.println("login success: " + success + " username: " + username);

        if (success) {
            // Fetch complete user info from server (includes role)
            Utilisateur user = service.getUserByUsername(username);

            if (user == null) {
                setMessage("error: Could not retrieve user information", true);
                return;
            }

            // Store in session
            SessionManager.getInstance().setCurrentUser(user);

            // Get role directly from the user object from database
            String role = user.getRole();
            boolean isAdmin = "ADMIN".equalsIgnoreCase(role);

            setMessage("Login successful\n Redirecting...", false);

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
                        stage.setTitle("BANK SYSTEM - Admin Dashboard");
                    } else {
                        ViewLoader.loadInto(root, "/project/bank/ClientDash.fxml");
                        stage.setTitle("BANK SYSTEM - Client Dashboard");
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
            setMessage("Invalid username or password. Please try again.", true);
        }
    }

    public void setMessage(String message, boolean isError) {
        resLOGIN_label.setText(message);
        if (isError) {
            resLOGIN_label.setStyle("-fx-text-fill: red; -fx-font-size: 14px;");
        } else {
            resLOGIN_label.setStyle("-fx-text-fill: green; -fx-font-size: 14px;");
        }
    }

    public String getUsername() {
        return TxtField_login.getText();
    }

    public String getPassword() {
        return passField_login.getText();
    }
}