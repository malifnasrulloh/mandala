module com.mandala {
    requires javafx.fxml;
    requires transitive javafx.graphics;
    requires transitive javafx.controls;

    opens com.mandala to javafx.fxml;
    exports com.mandala;
}
