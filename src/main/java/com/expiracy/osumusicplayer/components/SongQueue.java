package com.expiracy.osumusicplayer.components;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class SongQueue {
    private final Queue<Song> queue = new LinkedList<>();

    public void add(Song song) {
        this.queue.add(song);
    }

    public Song getNext() {
        if (this.queue.isEmpty())
            return null;

        return this.queue.poll();
    }

    public List<Song> getList() {
        return this.queue.stream().toList();
    }
}
