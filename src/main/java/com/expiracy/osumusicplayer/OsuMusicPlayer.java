package com.expiracy.osumusicplayer;

import com.expiracy.osumusicplayer.parsing.OsuSongsFolderParser;
import com.expiracy.osumusicplayer.parsing.Song;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.Map;
import java.util.TreeMap;

public class OsuMusicPlayer extends Application {
    protected Stage stage;

    protected Pane sideBox = new Pane();
    protected ScrollPane songsBox = new ScrollPane();
    protected Pane playingInfoBox = new Pane();
    protected Pane playerBox = new Pane();

    protected Button configureButton = new Button("Configure");
    protected Button homeButton = new Button("Home");
    protected Button searchButton = new Button("Search");

    Map<Integer, Song> songs = new TreeMap<>();

    private final EventHandler<ActionEvent> configure = event -> {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedFolder = directoryChooser.showDialog(this.stage);

        if (selectedFolder == null)
            return;

        OsuSongsFolderParser osuSongsParser = new OsuSongsFolderParser(selectedFolder);
        this.songs = osuSongsParser.songs;
        this.displaySongs(this.songs);
    };


    private final EventHandler<ActionEvent> playSong = event -> {
        Button button = (Button) event.getSource();
        Song song = this.songs.get(Integer.valueOf(button.getId()));

        ImageView image = (ImageView) this.playingInfoBox.lookup("#playingImage");
        image.setImage(new Image(song.getImage().getAbsolutePath()));

        Label title = (Label) this.playingInfoBox.lookup("#playingTitle");
        title.setText(song.getTitle());

        Label artist = (Label) this.playingInfoBox.lookup("#playingArtist");
        artist.setText(song.getArtist());

    };


    private final EventHandler<ActionEvent> search = event -> {
        System.out.println("Search");
    };

    private final EventHandler<ActionEvent> home = event -> {
        System.out.println("Home");
    };


    @Override
    public void start(Stage stage) {
        this.stage = stage;

        this.sideBox.getStyleClass().add("side-box");
        this.songsBox.getStyleClass().addAll("songs-box", "edge-to-edge");
        this.songsBox.getStylesheets().add(getClass().getResource("scroll-bar.css").toExternalForm());
        this.playingInfoBox.getStyleClass().add("playing-info-box");
        this.playerBox.getStyleClass().add("player-box");

        this.homeButton.getStyleClass().add("side-button");
        this.searchButton.getStyleClass().add("side-button");
        this.configureButton.getStyleClass().add("side-button");
        this.songsBox.setFocusTraversable(false);

        this.homeButton.setOnAction(home);
        this.searchButton.setOnAction(search);
        this.configureButton.setOnAction(configure);

        VBox sideButtons = new VBox();
        sideButtons.getChildren().addAll(this.searchButton, this.homeButton, this.configureButton);
        this.sideBox.getChildren().add(sideButtons);

        VBox artistAndTitle = new VBox();

        Label title = new Label("Title");
        title.setId("playingTitle");
        title.getStyleClass().add("title");
        title.setWrapText(true);

        Label artist = new Label("Artist");
        artist.setId("playingArtist");
        artist.getStyleClass().add("artist");
        artist.setWrapText(true);

        artistAndTitle.getChildren().addAll(title, artist);
        artistAndTitle.setStyle("-fx-alignment: center-left;");

        ImageView image = new ImageView("C:\\Users\\james\\Documents\\.Code\\osuMusicPlayer2\\src\\main\\resources\\com\\expiracy\\osumusicplayer\\image-placeholder.jpg");
        image.setFitHeight(43);
        image.setFitWidth(77);
        image.setId("playingImage");

        HBox songInfo = new HBox();
        songInfo.getChildren().addAll(image, artistAndTitle);
        songInfo.getStyleClass().add("playing-info-box");

        this.playingInfoBox.getChildren().addAll(songInfo);

        GridPane layoutPane = new GridPane();
        layoutPane.setHgap(10);
        layoutPane.setStyle("-fx-background-color: black");

        ColumnConstraints column1 = new ColumnConstraints(300);
        ColumnConstraints column2 = new ColumnConstraints();
        column2.setHgrow(javafx.scene.layout.Priority.ALWAYS);

        RowConstraints row1 = new RowConstraints();
        row1.setVgrow(javafx.scene.layout.Priority.ALWAYS);

        RowConstraints row2 = new RowConstraints(75);

        layoutPane.getColumnConstraints().addAll(column1, column2);
        layoutPane.getRowConstraints().addAll(row1, row2);

        layoutPane.add(this.sideBox, 0, 0);
        layoutPane.add(this.songsBox, 1, 0);
        layoutPane.add(this.playerBox, 1, 1);
        layoutPane.add(this.playingInfoBox, 0, 1);

        layoutPane.getStyleClass().addAll("background-box", "background-boxes");

        Scene scene = new Scene(layoutPane);
        scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());

        this.stage.setScene(scene);
        this.stage.setMaximized(true);
        this.stage.setTitle("Music Player for osu!");
        this.stage.show();
    }

    public void displaySongs(Map<Integer, Song> songs) {
        VBox songButtons = new VBox();

        Integer songNumber = 1;

        for (Map.Entry<Integer, Song> entry : songs.entrySet()) {
            Integer songId = entry.getKey();
            Song song = entry.getValue();

            Button songButton = new Button();

            HBox songInfo = new HBox();
            Label songNumberLabel = new Label(songNumber.toString());
            songNumberLabel.getStyleClass().add("song-number");

            VBox titleAndArtist = new VBox();
            Label title = new Label(song.getTitle());
            title.getStyleClass().add("title");

            Label artist = new Label(song.getArtist());
            artist.getStyleClass().add("artist");

            titleAndArtist.getChildren().addAll(title, artist);

            songInfo.getChildren().addAll(songNumberLabel, titleAndArtist);

            songButton.getStyleClass().add("song-button");
            songButton.setGraphic(songInfo);

            songButton.setId(songId.toString());
            songButton.setOnAction(playSong);
            //ImageView imageView = new ImageView(song.getImage().toURI().toString());

            songButtons.getChildren().add(songButton);

            ++songNumber;
        }

        this.songsBox.setContent(songButtons);
    }

    public static void main(String[] args) {
        launch();
    }
}