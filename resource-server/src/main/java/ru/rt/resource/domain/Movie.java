package ru.rt.resource.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "movies")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    public String filename;
    public String title;
    public Integer creationYear;
    public Double rating;
    public byte[] posterImage;
    public String youtubeCode;
    public String description;
    public Boolean withSubscriptionOnly;
}
