package ru.rt.resource.domain;

public class Book {
    public Long id;
    public String title;
    public String authorName;

    public Book() {}

    public Book(Long id, String title, String authorName) {
        this.id = id;
        this.title = title;
        this.authorName = authorName;
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

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }
}
