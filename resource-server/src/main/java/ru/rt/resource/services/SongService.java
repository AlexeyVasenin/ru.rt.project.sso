package ru.rt.resource.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.rt.resource.domain.Song;
import ru.rt.resource.repos.ISongRepo;
import ru.rt.resource.utils.TypesConverter;

import java.util.List;
import java.util.Map;

//todo A. Baidin описание класса
@Service
public class SongService {

    private final ISongRepo songRepo;

    private final TypesConverter typesConverter;

    @Autowired
    public SongService(ISongRepo songRepo, TypesConverter typesConverter) {
        this.songRepo = songRepo;
        this.typesConverter = typesConverter;
    }

    public List<Song> getAllSongs() {
        return songRepo.findAll();
    }

    public List<Map.Entry<Long, String>> getAllSongsIdsAndFilenames() {
        List<Object[]> allSongsIdsAndFilenames = songRepo.getAllSongsIdsAndFilenames();
        return typesConverter.convertObjectArrayToMapList(allSongsIdsAndFilenames);
    }

    public void setByteArrayToImageOfSongCoverById(byte[] albumCover, Long id) {
        songRepo.setByteArrayToImageOfSongCoverById(albumCover, id);
    }
}
