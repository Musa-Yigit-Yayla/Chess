module com.mycompany.chessfx {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.mycompany.chessfx to javafx.fxml;
    exports com.mycompany.chessfx;
}
