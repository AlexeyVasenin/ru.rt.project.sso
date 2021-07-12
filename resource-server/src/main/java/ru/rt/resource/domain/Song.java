package ru.rt.resource.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Song {
    public Integer id;
    public String title;
    public String musicianName;
    public Double rating;
    public String duration;
    public byte[] albumCover;
    public String filename;
}
