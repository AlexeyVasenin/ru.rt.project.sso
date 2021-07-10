package ru.rt.resource.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Movie {
    public Integer id;
    public String fileName;
    public String title;
    public Integer creationYear;
    public Double rating;
    public byte[] posterImage;
}
