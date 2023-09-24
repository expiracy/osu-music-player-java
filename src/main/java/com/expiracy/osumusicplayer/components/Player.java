package com.expiracy.osumusicplayer.components;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Player {
    private HBox controls = new HBox();

    private Button shuffle = new Button("\uD83D\uDD00");
    private Button last = new Button("⏮");
    private Button play = new Button();
    private Button pause = new Button();
    private Button next = new Button("⏭");
    private Button repeat = new Button("\uD83D\uDD04");


    public SongsQueue queue = new SongsQueue();

    private Slider seekSlider = new Slider();
    private Slider volumeSlider = new Slider(0, 100, 50);
    public MediaPlayer player = null;

    private boolean repeating = false;

    public PlayerInfo info = new PlayerInfo();

    public Player() {
        try {
            this.initControls();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        this.initSeekSlider();
        this.initVolume();
    }

    private void initVolume() {
        this.volumeSlider.getStyleClass().add("volume-slider");
    }

    private void initSeekSlider() {
        this.seekSlider.getStyleClass().addAll("seek-slider");
    }

    private ImageView getIcon(String path) {
        ImageView image;

        try {
            image = new ImageView(new Image(getClass().getResource(path).toURI().toString()));
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return null;
        }

        image.setFitWidth(35);
        image.setFitHeight(35);

        return image;
    }

    private void initControls() throws URISyntaxException {
        Pane playPause = new Pane();
        playPause.getChildren().addAll(this.play, this.pause);

        this.play.getStyleClass().add("play");
        this.pause.getStyleClass().add("pause");

        ImageView playImage = this.getIcon("images/play.png");
        ImageView pauseImage = this.getIcon("images/pause.png");

        this.play.setGraphic(playImage);
        this.pause.setGraphic(pauseImage);

        this.play.setVisible(true);
        this.pause.setVisible(false);

        this.play.setOnAction(e -> this.invertPlayPause());
        this.pause.setOnAction(e -> this.invertPlayPause());

        this.last.getStyleClass().addAll("last", "control-button");
        this.next.getStyleClass().addAll("next", "control-button");

        this.next.setOnAction(e -> {
            Song next = this.queue.getNext();
            if (next == null) return;
            this.play(next);
        });
        this.last.setOnAction(e -> {
            Song last = this.queue.getLast();
            if (last == null) return;
            this.play(last);
        });

        this.shuffle.getStyleClass().addAll("shuffle", "control-button");
        this.repeat.getStyleClass().addAll("repeat", "control-button");

        this.controls.getStyleClass().addAll("controls");
        this.controls.getChildren().addAll(this.shuffle, this.last, playPause, this.next, this.repeat);
    }

    public void queueSong(Song song) {
        this.queue.add(song);
    }

    private void repeatSong() {
        if (this.repeating) {
            if (!this.repeat.getStyleClass().contains("font-white"))
                this.repeat.getStyleClass().add("font-white");

            this.player.setOnEndOfMedia(() -> this.player.seek(Duration.ZERO));

        } else {
            this.repeat.getStyleClass().remove("font-white");

            this.player.setOnEndOfMedia(this::playQueued);
        }
    }

    private void playQueued() {
        if (!this.queue.isEmpty()) {
            this.play(this.queue.getNext());
        } else {
            this.player.stop();
            this.invertPlayPause();
        }

    }

    public Node getVolumeSliderNode() {
        return this.volumeSlider;
    }

    public Node getPlayerNode() {
        VBox node = new VBox();

        node.getStyleClass().add("player-box");
        node.getChildren().addAll(this.controls, this.seekSlider);

        return node;
    }

    public void play(Song song) {
        this.info.setSong(song);
        this.play(song.getMp3());
    }


    public void play(File mp3) {
        Media media = new Media(mp3.toURI().toString());
        this.play(media);
    }

    public void invertPlayPause() {
        if (this.player == null)
            return;

        if (this.play.isVisible()) {
            this.play.setVisible(false);
            this.pause.setVisible(true);
            this.player.play();
        } else {
            this.play.setVisible(true);
            this.pause.setVisible(false);
            this.player.pause();
        }
    }

    public void play(Media mp3) {

        if (this.player != null)
            this.player.stop();

        this.initPlayer(mp3);
    }

    private void initPlayer(Media mp3) {
        this.player = new MediaPlayer(mp3);

        this.play.setVisible(false);
        this.pause.setVisible(true);

        this.player.play();

        this.player.setOnReady(() -> this.seekSlider.setMax(mp3.getDuration().toSeconds()));

        this.player.currentTimeProperty().addListener((observable, oldValue, newValue) ->
                this.seekSlider.setValue(newValue.toSeconds())
        );

        this.seekSlider.setOnMousePressed(e -> this.player.seek(Duration.seconds(this.seekSlider.getValue())));
        this.seekSlider.setOnMouseDragged(e -> this.player.seek(Duration.seconds(this.seekSlider.getValue())));

        this.repeat.setOnAction(e -> {
            this.repeating = !this.repeating;
            this.repeatSong();
        });

        this.repeatSong();

        this.player.volumeProperty().bind(this.volumeSlider.valueProperty().divide(100));
    }
}
