package ru.rt.resource.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.rt.resource.domain.Song;
import ru.rt.resource.repos.ISongRepo;

import java.util.List;

@Service
public class SongService {
    private final ISongRepo songRepo;

    @Autowired
    public SongService(ISongRepo songRepo) {
        this.songRepo = songRepo;
    }

    public List<Song> getAllSongs() {
        return songRepo.findAll();
    }

    public List<String> getAllSongsFilenames() {
        return songRepo.getAllSongsFilenames();
    }
}
