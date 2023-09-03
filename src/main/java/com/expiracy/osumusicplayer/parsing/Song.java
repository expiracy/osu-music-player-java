package com.expiracy.osumusicplayer.parsing;

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

    public void setImage(File image) {
        this.image = image;
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

    public String getMp3Path() {
        return this.mp3.getAbsolutePath();
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("<span class='song-button-title-text'>")
                .append(this.title).append("</span>")
                .append("\n")
                .append("<span class='song-button-artist-text'>")
                .append(this.artist)
                .append("</span>");

        return sb.toString();
    }
}


