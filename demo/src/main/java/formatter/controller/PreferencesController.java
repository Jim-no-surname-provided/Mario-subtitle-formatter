package formatter.controller;

import formatter.model.FormatterSettings;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class PreferencesController {

    private Stage stage;

    @FXML
    private Button cancelBtn;
    @FXML
    private Button saveBtn;

    @FXML
    private TextField maxLenSubtitleTxt;
    @FXML
    private TextField maxLenLineTxt;
    @FXML
    private TextField preferedSplitPointsTxt;
    @FXML
    private TextField endOfSentenceTxt;

    private FormatterSettings settings;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void init() {
        settings = FormatterSettings.fromJson();
        initializeFieldContents();
        setListeners();
    }

    private void setListeners() {
        maxLenSubtitleTxt.textProperty().addListener((x, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                maxLenSubtitleTxt.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

        maxLenLineTxt.textProperty().addListener((x, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                maxLenSubtitleTxt.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

        cancelBtn.setOnAction(e -> initializeFieldContents());
        saveBtn.setOnAction(e -> {
            saveFieldContents();
            stage.close();
        });

    }

    private void initializeFieldContents() {
        maxLenSubtitleTxt.textProperty().set(Integer.toString(settings.maxLenSubtitle()));
        maxLenLineTxt.textProperty().set(Integer.toString(settings.maxLenLine()));

        preferedSplitPointsTxt.textProperty().set(new String(settings.preferedSplitPoints()));
        endOfSentenceTxt.textProperty().set(new String(settings.endOfSentence()));
    }

    private void saveFieldContents() {
        settings.maxLenSubtitle(Integer.parseInt(maxLenSubtitleTxt.getText()));
        settings.maxLenLine(Integer.parseInt(maxLenLineTxt.getText()));

        settings.preferedSplitPoints(preferedSplitPointsTxt.getText().toCharArray());
        settings.endOfSentence(endOfSentenceTxt.getText().toCharArray());
        FormatterSettings.toJson(settings);
    }

}
