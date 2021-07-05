package ru.rt.resource.domain;

public class Song {
    public Long id;
    public String title;
    public String musicianName;

    public Song() {}

    public Song(Long id, String title, String musicianName) {
        this.id = id;
        this.title = title;
        this.musicianName = musicianName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMusicianName() {
        return musicianName;
    }

    public void setMusicianName(String musicianName) {
        this.musicianName = musicianName;
    }
}
