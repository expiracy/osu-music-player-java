package com.expiracy.osumusicplayer;

import com.expiracy.osumusicplayer.components.Songs;
import com.expiracy.osumusicplayer.parsing.OsuSongsFolderParser;
import com.expiracy.osumusicplayer.components.Song;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;


import java.io.File;
import java.util.Map;

public class OsuMusicPlayer extends Application {
    protected Stage stage;

    // Songs box
    protected ScrollPane songsBox = new ScrollPane();

    // Side buttons
    protected VBox sideBox = new VBox();
    protected Button configureButton = new Button("Configure");
    protected Button homeButton = new Button("Home");
    protected Button searchButton = new Button("Search");
    protected TextField searchField = new TextField();

    protected HBox rightBox = new HBox();
    private Songs songs = new Songs();

    private final EventHandler<ActionEvent> configure = event -> {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedFolder = directoryChooser.showDialog(this.stage);

        if (selectedFolder == null)
            return;

        OsuSongsFolderParser osuSongsParser = new OsuSongsFolderParser(selectedFolder);
        this.songs.setSongs(osuSongsParser.songs);
        this.songsBox.setContent(this.songs.getNode());
    };


    private final EventHandler<ActionEvent> playSong = event -> {
        this.songs.play(Integer.valueOf(((Button) event.getSource()).getId()));
    };

    @Override
    public void start(Stage stage) {
        this.stage = stage;

        this.initSongsBox();
        this.initSideBox();
        this.initRightBox();

        Scene scene = new Scene(this.createGrid());
        scene.getStylesheets().add(getClass().getResource("css/styles.css").toExternalForm());
        scene.getStylesheets().add(getClass().getResource("css/player.css").toExternalForm());
        scene.getStylesheets().add(getClass().getResource("css/player-info.css").toExternalForm());

        this.stage.setScene(scene);
        this.stage.setMaximized(true);
        this.stage.setTitle("Music Player for osu!");
        this.stage.show();
        this.sideBox.requestFocus();
    }

    private void initRightBox() {
        this.rightBox = new HBox();
        this.rightBox.getStyleClass().add("volume-box");
        this.rightBox.getChildren().add(this.songs.player.getVolumeSliderNode());
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
        layoutPane.add(this.songs.player.getPlayerNode(), 1, 1);
        layoutPane.add(this.rightBox, 2, 1);
        layoutPane.add(this.songs.player.info.getNode(), 0, 1);

        layoutPane.getStyleClass().addAll("background-box", "background-boxes");

        return layoutPane;
    }

    private void initSongsBox() {
        this.songsBox.getStylesheets().add(getClass().getResource("css/scroll-bar.css").toExternalForm());

        this.songsBox.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        this.songsBox.getStyleClass().addAll("songs-box");

        this.songsBox.setContent(this.songs.getNode());
    }

    private void initSideBox() {
        this.sideBox.getStyleClass().add("side-box");
        this.homeButton.getStyleClass().add("side-button");
        this.searchButton.getStyleClass().add("side-button");
        this.configureButton.getStyleClass().add("side-button");

        this.searchField.getStyleClass().add("side-button");
        this.searchField.setPromptText("Search...");


        this.searchField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                Songs results = this.songs.search(this.searchField.getText().toLowerCase());
                this.searchField.clear();
                this.songsBox.setContent(results.getNode());
                this.sideBox.requestFocus();
            }
        });

        this.configureButton.setOnAction(this.configure);

        this.sideBox.getChildren().addAll(this.searchField, this.homeButton, this.configureButton);
    }

    public static void main(String[] args) {
        launch();
    }
}