module com.expiracy.osumusicplayer2 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;

    opens com.expiracy.osumusicplayer2 to javafx.fxml;
    exports com.expiracy.osumusicplayer2;
}