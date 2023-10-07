package com.expiracy.osumusicplayer.components;

import java.util.ArrayList;
import java.util.List;

public class SongHistory {
    private int current = -1;
    private final List<Song> history = new ArrayList<>();

    public void add(Song song) {
        this.history.add(song);
        this.current++;
    }

    public Song getNext() {
        this.current++;

        if (this.current >= this.history.size()) {
            this.current = this.history.size() - 1;
            return null;
        }

        return this.history.get(this.current);
    }

    public Song getLast() {
        this.current--;

        if (this.current < 0 || this.history.isEmpty()) {
            this.current = 0;
            return null;
        }

        return this.history.get(this.current);
    }

    public List<Song> getList() {
        List<Song> reversedSongs = new ArrayList<>();

        for (int i = this.history.size() - 1; i >= 0; i--)
            reversedSongs.add(this.history.get(i));

        return reversedSongs;
    }
}
