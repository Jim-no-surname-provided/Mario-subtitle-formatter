module formatter {
    requires transitive javafx.controls;
    requires transitive javafx.fxml;
    requires transitive com.google.gson;

    opens formatter to javafx.fxml;
    exports formatter;

    opens formatter.model to com.google.gson;
    opens formatter.controller to javafx.fxml;
}
