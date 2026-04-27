package project.bank.MAIN;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(
                getClass().getResource("/project/bank/Login.fxml")
        );

        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("BANK SYSTEM");


        try {
            stage.getIcons().add(new Image(
                    Objects.requireNonNull(getClass().getResourceAsStream("/project/bank/img/bank_main_ic.png"))
            ));
        } catch (Exception e) {
            System.out.println("Icon not found, skipping...");
        }

        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }
}