package com.expiracy.osumusicplayer.parsing;

import com.expiracy.osumusicplayer.components.Song;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class OsuSongsFolderParser {
    public Map<Integer, Song> songs = new HashMap<>();

    public OsuSongsFolderParser(File songsFolder) {
        if (!songsFolder.getName().equals("Songs")) return;

        this.parseSongsFolder(songsFolder);
    }

    public void parseSongsFolder(File songsFolder) {
        File[] beatmapFolders = songsFolder.listFiles();

        if (beatmapFolders == null) return;

        for (File beatmapFolder : beatmapFolders) {
            String[] splitFolderName = beatmapFolder.getName().split(" ");

            // Skip the folder if the beatmap ID cannot be obtained via .split()
            if (splitFolderName.length == 0) continue;

            int beatmapId = Integer.parseInt(splitFolderName[0]);

            Song song = this.parseBeatmapFolder(beatmapFolder);

            if (song == null) continue;

            songs.put(beatmapId, song);
        }

    }

    private Song parseBeatmapFolder(File beatmapFolder) {
        File[] contents = beatmapFolder.listFiles();

        if (contents == null)
            return null;

        Song song = new Song();

        for (File file : contents) {

            if (file.isDirectory())
                continue;

            String filename = file.getName().toLowerCase();

            if (filename.endsWith(".osu")) {
                OsuBeatmapParser osuBeatmapParser = new OsuBeatmapParser(file);

                if (osuBeatmapParser.audioFileName == null || osuBeatmapParser.title == null || osuBeatmapParser.artist == null)
                    return null;


                String audioFileName = osuBeatmapParser.audioFileName;

                if (audioFileName.endsWith(".ogg")) {
                    return null;
                }

                song.setMp3(file.getParent() + "/" + osuBeatmapParser.audioFileName);

                song.setTitle(osuBeatmapParser.title);
                song.setArtist(osuBeatmapParser.artist);

                if (osuBeatmapParser.imageFileName == null)
                    song.setImage("image-placeholder.jpg");
                else
                    song.setImage(file.getParent() + "/" + osuBeatmapParser.imageFileName);


                break;
            }

        }

        return song;
    }
}
