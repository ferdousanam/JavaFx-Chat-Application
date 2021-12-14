module com.ferdousanam.javafxchatapp {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;

    opens com.ferdousanam.javafxchatapp to javafx.fxml;
    exports com.ferdousanam.javafxchatapp;
    exports com.ferdousanam.javafxchatappwithoutfxml;
    opens com.ferdousanam.javafxchatappwithoutfxml to javafx.fxml;
}