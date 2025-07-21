package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public interface GenreStorage {
    List<Genre> getListGenres();

    Optional<Genre> getGenreById(long id);

    Set<Genre> getGenresForFilm(Film film);

    Map<Long, Set<Genre>> getGenresForAllFilms();
}
