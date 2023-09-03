package com.expiracy.osumusicplayer;

import com.expiracy.osumusicplayer.components.Player;
import com.expiracy.osumusicplayer.data.Songs;
import com.expiracy.osumusicplayer.parsing.OsuSongsFolderParser;
import com.expiracy.osumusicplayer.data.Song;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;


import java.io.File;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class OsuMusicPlayer extends Application {
    protected Stage stage;

    // Songs box
    protected ScrollPane songsBox = new ScrollPane();
    protected VBox songButtons = new VBox();

    // Playing info box
    protected Pane playingInfoBox = new HBox();

    // Side buttons
    protected VBox sideBox = new VBox();
    protected Button configureButton = new Button("Configure");
    protected Button homeButton = new Button("Home");
    protected Button searchButton = new Button("Search");
    protected TextField searchField = new TextField();

    protected HBox rightBox = new HBox();
    public Player player = new Player();

    // Songs
    private Map<Integer, Song> songsMap = new TreeMap<>();

    private Songs songs = new Songs();

    private final EventHandler<ActionEvent> configure = event -> {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedFolder = directoryChooser.showDialog(this.stage);

        if (selectedFolder == null)
            return;

        OsuSongsFolderParser osuSongsParser = new OsuSongsFolderParser(selectedFolder);
        this.songsMap = osuSongsParser.songs;
        this.displaySongs(this.songsMap);
    };


    private final EventHandler<ActionEvent> playSong = event -> {
        Button button = (Button) event.getSource();
        Song song = this.songsMap.get(Integer.valueOf(button.getId()));

        ImageView image = (ImageView) this.playingInfoBox.lookup("#playingImage");
        image.setImage(new Image(song.getImage().toURI().toString()));

        Label title = (Label) this.playingInfoBox.lookup("#playingTitle");
        title.setText(song.getTitle());

        System.out.println(song.getMp3().getAbsolutePath());
        System.out.println(song.getTitle());
        Label artist = (Label) this.playingInfoBox.lookup("#playingArtist");
        artist.setText(song.getArtist());

        this.player.play(song.getMp3());



    };


    private void searchSongs(String searchTerm) {
        Map<Integer, Song> filteredMap = this.songsMap.entrySet().stream()
                .filter(entry -> entry.getValue().getTitle().toLowerCase().contains(searchTerm) ||
                        entry.getValue().getArtist().toLowerCase().contains(searchTerm))
                .collect(Collectors.toMap(Entry::getKey, Entry::getValue));

        this.displaySongs(filteredMap);
    }

    private final EventHandler<ActionEvent> home = event -> {
        this.displaySongs(this.songsMap);
    };


    @Override
    public void start(Stage stage) {
        this.stage = stage;

        this.initSongsBox();
        this.initSideBox();
        this.initPlayingInfoBox();
        this.initRightBox();

        Scene scene = new Scene(this.createGrid());
        scene.getStylesheets().add(getClass().getResource("css/styles.css").toExternalForm());
        scene.getStylesheets().add(getClass().getResource("css/player.css").toExternalForm());

        this.stage.setScene(scene);
        this.stage.setMaximized(true);
        this.stage.setTitle("Music Player for osu!");
        this.stage.show();
    }

    private void initRightBox() {
        this.rightBox = new HBox();
        this.rightBox.getStyleClass().add("volume-box");
        this.rightBox.getChildren().add(this.player.getVolumeSliderNode());
    }

    private GridPane createGrid() {
        GridPane layoutPane = new GridPane();
        layoutPane.setHgap(10);
        layoutPane.setStyle("-fx-background-color: black");

        ColumnConstraints column1 = new ColumnConstraints(300);
        ColumnConstraints column2 = new ColumnConstraints();
        ColumnConstraints column3 = new ColumnConstraints(300);
        column2.setHgrow(Priority.ALWAYS);

        RowConstraints row1 = new RowConstraints();
        row1.setVgrow(Priority.ALWAYS);
        RowConstraints row2 = new RowConstraints(75);

        layoutPane.getColumnConstraints().addAll(column1, column2, column3);
        layoutPane.getRowConstraints().addAll(row1, row2);

        GridPane.setColumnSpan(this.songsBox, 2);

        layoutPane.add(this.sideBox, 0, 0);
        layoutPane.add(this.songsBox, 1, 0);
        layoutPane.add(this.player.getPlayerNode(), 1, 1);
        layoutPane.add(this.rightBox, 2, 1);
        layoutPane.add(this.playingInfoBox, 0, 1);

        layoutPane.getStyleClass().addAll("background-box", "background-boxes");

        return layoutPane;
    }

    private ImageView getControlImage(String filename) {
        File file = new File("src/main/resources/com/expiracy/osumusicplayer/" + filename);
        ImageView image = new ImageView(new Image(file.toURI().toString()));
        image.setFitWidth(35);
        image.setFitHeight(35);

        return image;
    }

    private void initSongsBox() {
        this.songsBox.getStylesheets().add(getClass().getResource("css/scroll-bar.css").toExternalForm());

        this.songsBox.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        this.songsBox.getStyleClass().addAll("songs-box");

        this.songButtons.getStyleClass().addAll("song-buttons");
        this.songsBox.setContent(this.songButtons);
    }

    private void initSideBox() {
        this.sideBox.getStyleClass().add("side-box");
        this.homeButton.getStyleClass().add("side-button");
        this.searchButton.getStyleClass().add("side-button");
        this.configureButton.getStyleClass().add("side-button");

        this.searchField.getStyleClass().add("side-button");
        this.searchField.setPromptText("Search...");

        this.homeButton.setOnAction(this.home);

        this.searchField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                this.searchSongs(this.searchField.getText().toLowerCase());
                this.searchField.clear();
                this.sideBox.requestFocus();
            }
        });

        this.configureButton.setOnAction(this.configure);

        this.sideBox.getChildren().addAll(this.searchField, this.homeButton, this.configureButton);
    }

    private void initPlayingInfoBox() {
        this.playingInfoBox.getStyleClass().add("playing-info-box");

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

        File placeHolderFile = new File("src/main/resources/com/expiracy/osumusicplayer/image-placeholder.jpg");
        ImageView image = new ImageView(new Image(placeHolderFile.toURI().toString()));

        image.setFitHeight(43);
        image.setFitWidth(77);

        image.setId("playingImage");

        this.playingInfoBox.getChildren().addAll(image, artistAndTitle);
    }

    public void displaySongs(Map<Integer, Song> songs) {

        Integer songNumber = 1;
        this.songButtons.getChildren().clear();
        for (Entry<Integer, Song> entry : songs.entrySet()) {
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
            songButton.setOnAction(this.playSong);

            this.songButtons.getChildren().add(songButton);

            ++songNumber;
        }
    }

    public static void main(String[] args) {
        launch();
    }
}