package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ru.yandex.practicum.filmorate.storage.db.repository.LikeRepository;

import java.util.Map;
import java.util.Set;

@Service
public class LikeService {
    private final LikeRepository likeRepository;

    @Autowired
    public LikeService(LikeRepository likeRepository) {
        this.likeRepository = likeRepository;
    }

    public Set<Long> getLikesByFilmId(long filmId) {
        return likeRepository.getLikesByFilmId(filmId);
    }

    public Map<Long, Set<Long>> getLikesAllFilmId() {
        return likeRepository.getLikesForAllFilms();
    }
}
