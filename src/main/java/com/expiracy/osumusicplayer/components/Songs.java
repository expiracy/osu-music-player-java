package com.expiracy.osumusicplayer.components;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class Songs {
    public Map<Integer, Song> songs = new HashMap<>();

    public Player player = new Player();

    public Songs(Map<Integer, Song> songs, Player player) {
        this.songs = songs;
        this.player = player;
    }

    public Songs() {

    }

    public Node getNode() {
        return getNode(this.songs);
    }

    public void play(Song song) {
        this.player.play(song.getMp3());
        this.player.info.setSong(song);
    }

    public void play(int songId) {
        Song song = this.songs.get(songId);
        this.play(song);
    }

    public Node getNode(Map<Integer, Song> songs) {
        Integer songNumber = 1;

        VBox songButtons = new VBox();
        songButtons.getStyleClass().add("song-buttons");

        for (Map.Entry<Integer, Song> entry : songs.entrySet()) {
            Song song = entry.getValue();

            Button songButton = new Button();
            songButton.getStyleClass().add("song-button");
            songButton.setId(entry.getKey().toString());

            songButton.setGraphic(song.getNode(songNumber));

            songButton.setOnAction(e -> this.play(entry.getKey()));

            songButtons.getChildren().add(songButton);
            ++songNumber;
        }
        return songButtons;
    }

    public Songs search(String searchTerm) {
        Map<Integer, Song> results = this.songs.entrySet()
                .stream()
                .filter(entry ->
                        entry.getValue().getTitle().toLowerCase().contains(searchTerm)
                        || entry.getValue().getArtist().toLowerCase().contains(searchTerm)
                ).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        return new Songs(results, this.player);
    }

    public void setSongs(Map<Integer, Song> songs) {
        this.songs = songs;
    }

}
