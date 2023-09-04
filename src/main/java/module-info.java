module com.expiracy.osumusicplayer {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;

    requires org.controlsfx.controls;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires jave.nativebin.win64;

    opens com.expiracy.osumusicplayer to javafx.fxml;
    exports com.expiracy.osumusicplayer;
    exports com.expiracy.osumusicplayer.parsing;
    opens com.expiracy.osumusicplayer.parsing to javafx.fxml;
    exports com.expiracy.osumusicplayer.components;
    opens com.expiracy.osumusicplayer.components to javafx.fxml;
}