package com.expiracy.osumusicplayer.parsing;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OsuBeatmapParser {
    public String title = null;
    public String artist = null;
    public List<String> tags = new LinkedList<>();
    public String imageFileName = null;
    public String audioFileName = null;

    public OsuBeatmapParser(File beatmap) {
        this.parseBeatmap(beatmap);
    }

    public void parseBeatmap(File beatmap) {
        try {
            Scanner reader = new Scanner(beatmap);

            boolean inEvents = false;

            while (reader.hasNextLine()) {
                String line = reader.nextLine();

                if (line.startsWith("Title:")) {
                    this.title = line.substring(6).trim();
                } else if (line.startsWith("Artist:")) {
                    this.artist = line.substring(7).trim();
                } else if (line.startsWith("AudioFilename:")) {
                    this.audioFileName = line.substring(14).trim();
                } else if (line.startsWith("Tags:") && line.length() >= 6) {
                    Collections.addAll(this.tags, line.substring(5).trim().split(" "));
                } else if (line.startsWith("[Events]")) {
                    inEvents = true;
                } else if (inEvents && line.startsWith("0,0,")) {

                    Pattern pattern = Pattern.compile("\"(.*?)\"");
                    Matcher matcher = pattern.matcher(line);

                    if (matcher.find())
                        imageFileName = matcher.group(1);

                    break;
                }
            }
            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
