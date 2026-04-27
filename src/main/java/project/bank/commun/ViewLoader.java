package project.bank.commun;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class ViewLoader {

    public static void loadInto(Pane container, String fxmlPath) {
        try {
            var resource = ViewLoader.class.getResource(fxmlPath);

            if (resource == null) {
                System.out.println("FXML NOT FOUND: " + fxmlPath);
                return;
            }

            FXMLLoader loader = new FXMLLoader(resource);
            Parent view = loader.load();

            // Replace the entire scene root instead of adding as child
            container.getScene().setRoot(view);

        } catch (IOException e) {
            System.out.println("Error loading: " + fxmlPath);
            e.printStackTrace();
        }
    }
}