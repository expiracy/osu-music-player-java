package com.expiracy.osumusicplayer.components;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.File;
import java.net.URISyntaxException;

public class PlayerInfo {
    public HBox node;

    private Label title;
    private Label artist;
    private ImageView image;

    public PlayerInfo() {
        this.initImage();
        this.initInfo();
        this.initNode();
    }

    private void initNode() {
        this.node = new HBox();
        this.node.getStyleClass().addAll("center-left", "spacing", "player-info");
        this.node.getChildren().addAll(this.image, this.getSongInfoNode());
    }

    private void initInfo() {
        this.title = new Label("Title");
        this.artist = new Label("Artist");

        this.title.getStyleClass().add("title");
        this.artist.getStyleClass().add("artist");

        this.title.setWrapText(true);
        this.artist.setWrapText(true);
    }

    private void initImage() {
        ImageView image;

        try {
            image = new ImageView(new Image(getClass().getResource("images/image-placeholder.jpg").toURI().toString()));
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return;
        }

        image.setFitWidth(77);
        image.setFitHeight(43);

        this.image = image;
    }

    public Node getSongInfoNode() {
        VBox songInfo = new VBox();
        songInfo.getChildren().addAll(this.title, this.artist);

        songInfo.getStyleClass().addAll("center-left");

        return songInfo;
    }

    public void setImage(File image) {
        this.image.setImage(new Image(image.toURI().toString()));
    }

    public void setSong(Song song) {
        this.title.setText(song.getTitle());
        this.artist.setText(song.getArtist());
        this.setImage(song.getImage());
    }

    public Node getNode() {
        return this.node;
    }
}
