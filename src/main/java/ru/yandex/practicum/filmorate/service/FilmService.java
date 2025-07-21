package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;
import ru.yandex.practicum.filmorate.storage.AgeRatingStorage;
import ru.yandex.practicum.filmorate.storage.LikeStorage;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;

@Service
public class FilmService {
    @Qualifier("filmRepository")
    private final FilmStorage filmStorage;
    private final GenreStorage genreStorage;
    private final AgeRatingStorage ageRatingStorage;
    private final LikeStorage likeStorage;
    @Qualifier("userRepository")
    private final UserStorage userStorage;

    @Autowired
    public FilmService(
            @Qualifier("filmRepository") FilmStorage filmStorage,
            GenreStorage genreStorage,
            AgeRatingStorage ageRatingStorage,
            LikeStorage likeStorage,
            @Qualifier("userRepository") UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.genreStorage = genreStorage;
        this.ageRatingStorage = ageRatingStorage;
        this.likeStorage = likeStorage;
        this.userStorage = userStorage;
    }

    public Film addFilm(Film film) {
        validateDataFilm(film);

        return filmStorage.addFilm(film);
    }

    private void validateDataFilm(Film film) {
        ageRatingStorage.getAgeRatingById(film.getMpa().getId())
                .orElseThrow(() ->
                        new NotFoundException(String.format("Рейтинг с id - %s не найден.", film.getMpa().getId())));

        if (film.getGenres() != null) {
            for (Genre genre : film.getGenres()) {
                genreStorage.getGenreById(genre.getId())
                        .orElseThrow(() ->
                                new NotFoundException(String.format("Жанр с id - %s не найден.", genre.getId())));
            }
        }
    }

    public Film updateFilm(Film film) {
        validateDataFilm(film);
        filmExists(film.getId());

        return filmStorage.updateFilm(film);
    }

    private Film filmExists(long id) {
        return filmStorage.getFilmById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Фильм с указанным id - %s не найден", id)));
    }

    public Film getFilmById(long filmId) {
        Film film = filmExists(filmId);
        film.setGenres(genreStorage.getGenresForFilm(film));
        film.setLikes(likeStorage.getLikesByFilmId(filmId));

        return film;
    }

    public List<Film> getPopularFilms(long count) {
        if (count < 0) {
            throw new IllegalArgumentException("Количество фильмов не может быть отрицательным числом");
        }

        List<Film> films = filmStorage.getPopularFilms(count);
        Map<Long, Set<Genre>> genres = genreStorage.getGenresForAllFilms();

        for (Film film : films) {
            film.setGenres(genres.get(film.getId()));
            film.setLikes(likeStorage.getLikesByFilmId(film.getId()));
        }

        return films;
    }

    public List<Film> getListFilm() {
        List<Film> films = filmStorage.getListFilms();
        Map<Long, Set<Genre>> genres = genreStorage.getGenresForAllFilms();

        for (Film film : films) {
            film.setGenres(genres.get(film.getId()));
            film.setLikes(likeStorage.getLikesByFilmId(film.getId()));
        }

        return films;
    }

    public boolean removeFilm(long id) {
        getFilmById(id);

        return filmStorage.removeFilm(filmExists(id).getId());
    }

    public boolean addLike(long filmId, long userId) {
        getFilmById(filmId);
        userStorage.getUserById(userId);

        return filmStorage.addLike(filmId, userId);
    }

    public boolean removeLike(long filmId, long userId) {
        getFilmById(filmId);
        userStorage.getUserById(userId);

        return filmStorage.removeLike(filmId, userId);
    }
}
