package com.expiracy.osumusicplayer.data;

import java.util.Map;
import java.util.stream.Collectors;

public class Songs {
    private Map<Integer, Song> songs;

    public Songs() {

    }
    public Songs(Map<Integer, Song> songs) {
        this.songs = songs;
    }

    private Map<Integer, Song> searchSongs(String searchTerm) {
        return this.songs.entrySet()
                .stream()
                .filter(entry ->
                        entry.getValue().getTitle().toLowerCase().contains(searchTerm)
                        || entry.getValue().getArtist().toLowerCase().contains(searchTerm)
                ).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
