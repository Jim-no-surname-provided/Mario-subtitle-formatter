package formatter.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import formatter.SubtitleFormatterApp;
import formatter.inout.InOut;
import formatter.model.FormatterSettings;
import formatter.model.SubtitleFormatter;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class PrimaryController {

    private Stage primaryStage;

    @FXML
    private Button examineInputBtn;

    @FXML
    private Button examineOutputBtn;

    @FXML
    private Button formatBtn;

    @FXML
    private TextField inputFileTxt;
    private String inputFilePath;

    @FXML
    private TextField outputFileTxt;
    private String outputFilePath;

    @FXML
    private MenuItem preferencesBtn;

    @FXML
    private MenuItem quitBtn;

    @FXML
    private Label warningTxt;

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    @FXML
    void initialize() {
        preferencesBtn.setOnAction(e -> {

            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(SubtitleFormatterApp.class.getResource("preferences.fxml"));
                Scene scene = new Scene(loader.load());
                PreferencesController controller = loader.getController();
                Stage stage = new Stage();
                controller.setStage(stage);
                controller.init();
                stage.setTitle("Preferences");
                stage.setScene(scene);
                stage.getIcons().add(primaryStage.getIcons().get(0));
                stage.initOwner(primaryStage);
                stage.show();

            } catch (IOException ex) {
                ex.printStackTrace();
            }

        });

        examineInputBtn.setOnAction(E -> {

            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Search for an input text");

            if (isValidDirectory(inputFilePath)) {
                fileChooser.setInitialDirectory(new File(inputFilePath).toPath().getParent().toFile());
            }

            File file = fileChooser.showOpenDialog(primaryStage);
            if (file != null) {
                inputFilePath = file.getAbsolutePath();
                inputFileTxt.textProperty().set(inputFilePath);
            }

        });
        examineOutputBtn.setOnAction(E -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Search for an output text");

            if (isValidDirectory(outputFilePath)) {
                fileChooser.setInitialDirectory(new File(outputFilePath).toPath().getParent().toFile());
            } else if (isValidDirectory(inputFilePath)) {
                fileChooser.setInitialDirectory(new File(inputFilePath).toPath().getParent().toFile());
            }

            File file = fileChooser.showSaveDialog(primaryStage);
            if (file != null) {
                outputFilePath = file.getAbsolutePath();
                outputFileTxt.textProperty().set(outputFilePath);
            }
        });

        quitBtn.setOnAction(e -> primaryStage.close());

        formatBtn.setOnAction(e -> {
            FormatterSettings formatterSettings = FormatterSettings.fromJson();

            String inFileName = inputFilePath;
            String outFileName = outputFilePath;
            if (!isValidFile(inputFilePath, true) || !isValidFile(outputFilePath, false)) {
                setWarning(true);
                return;
            }

            String original = InOut.readFile(inFileName);
            SubtitleFormatter formatter = new SubtitleFormatter(original, formatterSettings);
            formatter.format();
            InOut.writeFile(outFileName, formatter.getFormatted());
            Alert alert = new Alert(AlertType.INFORMATION, "Done :)", ButtonType.OK);
            setWarning(false);
            alert.showAndWait();

        });

    }

    private boolean isValidFile(String path, boolean hasToExist) {
        if (path == null) {
            return false;
        }
        Path p = new File(path).toPath();

        if (hasToExist && !Files.exists(p)) {
            return false;
        }
        if (!path.endsWith(".txt")) {
            return false;
        }

        return true;
    }

    private boolean isValidDirectory(String path) {
        if (path == null) {
            return false;
        }
        Path p = new File(path).toPath().getParent();
        return Files.isDirectory(p);
    }

    private void setWarning(boolean bool) {
        warningTxt.setVisible(bool);
    }
}