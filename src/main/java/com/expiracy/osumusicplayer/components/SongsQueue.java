package com.expiracy.osumusicplayer.components;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class SongsQueue {
    private int current = 0;
    private List<Song> queue = new ArrayList<>();

    public void add(Song song) {
        this.queue.add(song);
    }

    public Song getNext() {
        this.current++;

        if (this.current >= this.queue.size() || this.isEmpty()) {
            this.current = this.queue.size() - 1;
            return null;
        }

        return this.queue.get(this.current);
    }

    public Song getLast() {
        this.current--;

        if (this.current < 0 || this.isEmpty()) {
            this.current = 0;
            return null;
        }

        return this.queue.get(this.current);
    }

    public boolean isEmpty() {
        return this.queue.isEmpty();
    }

    public List<Song> getList() {
        return this.queue.subList(this.current, this.queue.size());
    }
}
