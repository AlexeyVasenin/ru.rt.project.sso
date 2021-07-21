package ru.rt.resource.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.rt.resource.domain.Song;
import ru.rt.resource.repos.ISongRepo;
import ru.rt.resource.rest.MusicController;
import ru.rt.resource.utils.TypesConverter;

import java.util.List;
import java.util.Map;

/**
 * Сервис для работы с сущностями Song, связывающий между собой контроллер {@link MusicController} и репозиторий
 * {@link ISongRepo}, а также реализующий {@link ISongService}.
 * <p>
 *
 * @author Alexey Baidin
 */
@Service
public class SongServiceImpl implements ISongService {

    private final ISongRepo songRepo;

    private final TypesConverter typesConverter;

    @Autowired
    public SongServiceImpl(ISongRepo songRepo, TypesConverter typesConverter) {
        this.songRepo = songRepo;
        this.typesConverter = typesConverter;
    }

    @Override
    public List<Song> getAllSongs() {
        return songRepo.findAll();
    }

    @Override
    public List<Map.Entry<Long, String>> getAllSongsIdsAndFilenames() {
        List<Object[]> allSongsIdsAndFilenames = songRepo.getAllSongsIdsAndFilenames();
        return typesConverter.convertObjectArrayToMapList(allSongsIdsAndFilenames);
    }

    @Override
    public void setByteArrayToImageOfSongCoverById(byte[] albumCover, Long id) {
        songRepo.setByteArrayToImageOfSongCoverById(albumCover, id);
    }
}
