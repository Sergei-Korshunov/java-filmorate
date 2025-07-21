package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

public interface FilmStorage {
    Film addFilm(Film film);

    Film updateFilm(Film film);

    Optional<Film> getFilmById(long id);

    List<Film> getPopularFilms(long count);

    List<Film> getListFilms();

    boolean removeFilm(long id);

    boolean addLike(long filmId, long userId);

    boolean removeLike(long filmId, long userId);
}
