package formatter;

import formatter.controller.PrimaryController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class SubtitleFormatterApp extends Application {

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        final FXMLLoader loader = new FXMLLoader(getClass().getResource("primary.fxml"));
        final Parent root = loader.load();
        PrimaryController controller = loader.getController();
        controller.setPrimaryStage(primaryStage);

        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("channels4_profile.jpg")));
        primaryStage.setTitle("Formatter");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
}