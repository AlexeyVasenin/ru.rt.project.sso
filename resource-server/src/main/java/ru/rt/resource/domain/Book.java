package ru.rt.resource.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Book {
    public Integer id;
    public String title;
    public String authorName;
    public Double rating;
    public String filename;
    public byte[] coverImage;
    public String[] genres;
    public String description;
}
