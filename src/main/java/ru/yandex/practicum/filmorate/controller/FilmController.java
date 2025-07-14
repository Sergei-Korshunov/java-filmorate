package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.*;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {
        log.info("Добавлен фильм '{}'", film.getName());
        return filmService.addFilm(film);
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        Film updatedFilm = filmService.updateFilm(film);
        if (updatedFilm != null)
            log.info("Обновлен фильм '{}'", film.getName());

        return updatedFilm;
    }

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable long id) {
        log.info("Запрос на поиск фильма по id - {}", id);
        return filmService.getFilmById(id);
    }

    @GetMapping("/popular")
    public List<Film> getPopularFilms(@RequestParam(defaultValue = "10") int count) {
        List<Film> popularFilms = filmService.getPopularFilms(count);
        log.debug("Список популярных фильмов в колличестве {}: {}", count, popularFilms);

        return popularFilms;
    }

    @GetMapping
    public List<Film> getAllFilms() {
        List<Film> films = filmService.getListFilm();
        log.debug("Список всех фильмов {}", films);

        return films;
    }

    @DeleteMapping("/{id}")
    public boolean removeFilm(@PathVariable long id) {
        log.info("Удаление фильма по id - {}", id);
        return filmService.removeFilm(id);
    }

    @PutMapping("/{id}/like/{userId}")
    public boolean addLike(@PathVariable long id, @PathVariable long userId) {
        log.info("Пользователь с id - {} поставил оценку фильму с id - {}", userId, id);
        return filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public boolean removeLike(@PathVariable long id, @PathVariable long userId) {
        log.info("Пользователь с id - {} убрал оценку фильму с id - {}", userId, id);
        return filmService.removeLike(id, userId);
    }

}
