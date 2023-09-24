package com.expiracy.osumusicplayer;

import com.expiracy.osumusicplayer.components.Songs;
import com.expiracy.osumusicplayer.parsing.OsuSongsFolderParser;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;


import java.io.File;

public class OsuMusicPlayer extends Application {
    protected Stage stage;
    // Songs box
    protected ScrollPane songsBox = new ScrollPane();
    protected VBox sideBar = new VBox();
    protected HBox rightBox = new HBox();
    // Songs object contains Player and PlayerInfo objects
    private Songs songs = new Songs();

    @Override
    public void start(Stage stage) {
        this.stage = stage;

        this.initSongsBox();
        this.initSideBar();
        this.initRightBox();

        Scene scene = new Scene(this.createGrid());
        scene.getStylesheets().add(getClass().getResource("css/styles.css").toExternalForm());
        scene.getStylesheets().add(getClass().getResource("css/player.css").toExternalForm());
        scene.getStylesheets().add(getClass().getResource("css/player-info.css").toExternalForm());
        scene.getStylesheets().add(getClass().getResource("css/font.css").toExternalForm());
        scene.getStylesheets().add(getClass().getResource("css/context-menu.css").toExternalForm());

        stage.setScene(scene);
        stage.setMaximized(true);
        stage.setTitle("Music Player for osu!");
        stage.show();

        this.sideBar.requestFocus();
    }

    private void initRightBox() {
        this.rightBox = new HBox();
        this.rightBox.getStyleClass().addAll("center-right", "padding-8");

        this.rightBox.getChildren().add(this.songs.player.getVolumeSliderNode());
    }

    private GridPane createGrid() {
        GridPane layoutPane = new GridPane();
        layoutPane.setHgap(8);
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

        layoutPane.add(this.sideBar, 0, 0);
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

    private void initSideBar() {
        this.sideBar.getStyleClass().add("side-box");

        Button homeButton = new Button("\uD83C\uDFE0   Home");
        Button searchButton = new Button("Search");
        Button queueButton = new Button("Q   Queue");
        Button configureButton = new Button("\uD83D\uDD27   Configure");
        TextField searchField = new TextField();

        homeButton.getStyleClass().add("side-button");
        searchButton.getStyleClass().add("side-button");
        queueButton.getStyleClass().add("side-button");
        configureButton.getStyleClass().add("side-button");
        searchField.getStyleClass().add("side-button");

        searchField.setPromptText("\uD83D\uDD0D   Search...");

        searchField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                Songs results = this.songs.search(searchField.getText().toLowerCase());
                searchField.clear();
                this.songsBox.setContent(results.getNode());
                this.sideBar.requestFocus();
            }
        });

        queueButton.setOnAction(e -> {
            this.songsBox.setContent(this.songs.getQueue().getNode());
        });

        configureButton.setOnAction(e -> {
            DirectoryChooser directoryChooser = new DirectoryChooser();
            File selectedFolder = directoryChooser.showDialog(this.stage);

            if (selectedFolder == null)
                return;

            OsuSongsFolderParser osuSongsParser = new OsuSongsFolderParser(selectedFolder);

            this.songs.setSongs(osuSongsParser.songs);
            this.songsBox.setContent(this.songs.getNode());
        });

        homeButton.setOnAction(e -> this.songsBox.setContent(this.songs.getNode()));

        this.sideBar.getChildren().addAll(searchField, homeButton, queueButton, configureButton);
    }

    public static void main(String[] args) {
        launch();
    }
}