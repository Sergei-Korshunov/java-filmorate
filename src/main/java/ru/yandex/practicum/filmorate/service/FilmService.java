package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.List;

@Service
public class FilmService {
    private UserService userService;
    private FilmStorage filmStorage;

    @Autowired
    public FilmService(UserService userService, FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
        this.userService = userService;
    }

    public Film addFilm(Film film) {
        return filmStorage.addFilm(film);
    }

    public Film updateFilm(Film film) {
        return filmStorage.updateFilm(film);
    }

    public Film getFilmById(long id) {
        return filmStorage.getFilmById(id);
    }

    public List<Film> getListFilm() {
        return filmStorage.getListFilms();
    }

    public boolean removeFilm(long id) {
        return filmStorage.removeFilm(id);
    }

    public boolean addLike(long filmId, long userId) {
        Film film = getFilmById(filmId);
        userService.getUserById(userId);

        if (!film.getLikes().contains(userId))  {
            return film.getLikes().add(userId);
        }
        throw new ValidationException(
                String.format("Пользователю с id - %s уже понравился фильм '%s'", userId, film.getName()));
    }

    public boolean removeLike(long filmId, long userId) {
        Film film = getFilmById(filmId);
        userService.getUserById(userId);

        if (film.getLikes().contains(userId)) {
            return film.getLikes().remove(userId);
        }
        throw new ValidationException(
                String.format("Пользователь с id - %s не оценивал фильм '%s'", userId, film.getName()));
    }

    public List<Film> getPopularFilms(long count) {
        return filmStorage.getPopularFilms(count);
    }
}
