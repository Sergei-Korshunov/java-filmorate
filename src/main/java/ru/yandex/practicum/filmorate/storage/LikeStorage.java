package ru.yandex.practicum.filmorate.storage;

import java.util.Map;
import java.util.Set;

public interface LikeStorage {
    Set<Long> getLikesByFilmId(long filmId);

    Map<Long, Set<Long>> getLikesForAllFilms();
}
