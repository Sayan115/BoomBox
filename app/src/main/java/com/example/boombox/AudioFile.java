package com.example.boombox;

public class AudioFile {
    // Fields to store the information of each audio file
    private String name;
    private String path;
    private long duration;

    // Constructor
    public AudioFile (String name, String path, long duration) {
        this.name = name;
        this.path = path;
        this.duration = duration;
    }

    // Getters and setters
    public String getName () {
        return name;
    }

    public void setName (String name) {
        this.name = name;
    }

    public String getPath () {
        return path;
    }

    public void setPath (String path) {
        this.path = path;
    }

    public long getDuration () {
        return duration;
    }

    public void setDuration (long duration) {
        this.duration = duration;
    }
}
