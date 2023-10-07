package com.expiracy.osumusicplayer.components;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Songs {
    public enum Content {
        HOME,
        QUEUE,
        HISTORY,
        SEARCH
    }

    public List<Song> songs = new ArrayList<>();
    public Player player = new Player();

    public Songs(List<Song> songs, Player player) {
        this.songs = songs;
        this.player = player;
    }

    public Songs(ScrollPane songsBox) {
        this.player.content = Content.HOME;
        this.player.setSongsBox(songsBox);
    }

    public Node getNode() {
        return this.getNode(this.songs);
    }

    public void play(Song song) {
        this.player.playAndRecord(song);
    }

    public Node getNode(List<Song> songs) {
        Integer songNumber = 1;

        VBox songButtons = new VBox();
        songButtons.getStyleClass().add("song-buttons");

        for (Song song : songs) {

            Button songButton = new Button();
            songButton.getStyleClass().add("song-button");

            songButton.setOnContextMenuRequested(e -> this.getContextMenuNode(song).show(songButton, e.getScreenX(), e.getScreenY()));

            // Element ID is the song number
            songButton.setId(song.getId().toString());

            songButton.setGraphic(song.getNode(songNumber));

            songButton.setOnMouseClicked(e -> {
                if (!e.getButton().equals(MouseButton.PRIMARY))
                    return;

                if (e.getClickCount() >= 2)
                    this.play(song);
            });

            songButtons.getChildren().add(songButton);

            ++songNumber;
        }
        return songButtons;
    }

    public Songs search(String searchTerm) {

        List<Song> results = this.songs
                .stream()
                .filter(song ->
                        song.getTitle().toLowerCase().contains(searchTerm)
                                || song.getArtist().toLowerCase().contains(searchTerm)
                ).collect(Collectors.toList());

        this.player.content = Content.SEARCH;

        return new Songs(results, this.player);
    }

    public void setSongs(List<Song> songs) {
        this.songs = songs;
    }

    public Songs getHome() {
        this.player.content = Content.HOME;
        return this;
    }

    public Songs getQueue() {
        this.player.content = Content.QUEUE;
        return new Songs(this.player.queue.getList(), this.player);
    }

    public Songs getHistory() {
        this.player.content = Content.HISTORY;
        return new Songs(this.player.history.getList(), this.player);
    }

    public ContextMenu getContextMenuNode(Song song) {
        ContextMenu contextMenu = new ContextMenu();
        contextMenu.getStyleClass().addAll("context-menu", "font-off-white", "font-main", "font-12", "center-left", "font-bold");
        MenuItem queue = new MenuItem("Queue");

        queue.setOnAction(e -> this.player.queueSong(song));

        contextMenu.getItems().addAll(queue);

        return contextMenu;
    }

}
