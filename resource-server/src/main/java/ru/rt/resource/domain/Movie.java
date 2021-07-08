package ru.rt.resource.domain;

public class Movie {
    public Long id;
    public String title;
    public Long creationYear;

    public Movie() {}

    public Movie(Long id, String title, Long creationYear) {
        this.id = id;
        this.title = title;
        this.creationYear = creationYear;
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

    public Long getCreationYear() {
        return creationYear;
    }

    public void setCreationYear(Long creationYear) {
        this.creationYear = creationYear;
    }
}
