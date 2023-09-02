module com.expiracy.osumusicplayer {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;

    opens com.expiracy.osumusicplayer to javafx.fxml;
    exports com.expiracy.osumusicplayer;
    exports com.expiracy.osumusicplayer.parsing;
    opens com.expiracy.osumusicplayer.parsing to javafx.fxml;
}