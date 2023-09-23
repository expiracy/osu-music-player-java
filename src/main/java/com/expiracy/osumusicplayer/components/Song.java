package com.expiracy.osumusicplayer.components;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.File;

public class Song {
    private File mp3;
    private File image;
    private String title;
    private String artist;

    public Song() {
        this.mp3 = null;
        this.image = null;
        this.title = null;
        this.artist = null;
    }

    public void setMp3(File mp3) {
        this.mp3 = mp3;
    }

    public void setMp3(String audioPath) {
        this.mp3 = new File(audioPath);
    }

    public void setImage(String imagePath) {
        this.image = new File(imagePath);
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public File getMp3() {
        return this.mp3;
    }

    public File getImage() {
        return this.image;
    }

    public String getTitle() {
        return this.title;
    }

    public String getArtist() {
        return this.artist;
    }

    public Node getNode(Integer number) {
        return getNode(this, number);
    }

    public Node getNode() {
        return getNode(this);
    }

    public static Node getNode(Song song) {
        VBox songInfo = new VBox();
        songInfo.getStyleClass().add("center-left");

        Label title = new Label(song.getTitle());
        title.getStyleClass().add("title");

        Label artist = new Label(song.getArtist());
        artist.getStyleClass().add("artist");

        songInfo.getChildren().addAll(title, artist);

        return songInfo;
    }

    public static Node getNode(Song song, Integer number) {
        HBox songInfo = new HBox();

        songInfo.getStyleClass().addAll("center-left", "spacing-10");

        Label numberLabel = new Label(number.toString());
        numberLabel.getStyleClass().add("song-number");

        Node titleAndArtist = getNode(song);
        songInfo.getChildren().addAll(numberLabel, titleAndArtist);

        return songInfo;    

    }

    @Override
    public String toString() {
        return this.title;
    }
}


