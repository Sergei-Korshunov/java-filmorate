package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.*;

import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private Map<Long, Film> films = new HashMap<>();
    private long countId = 0;

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {
        film.setId(++countId);
        films.put(film.getId(), film);
        log.info("Добавлен фильм '{}'", film.getName());

        return film;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        if (!films.containsKey(film.getId()))
            throw new NotFoundException("Фильм с указанным id (" + film.getId() + ") не найден");

        films.put(film.getId(), film);

        log.info("Обновлен фильм '{}'", film.getName());

        return film;
    }

    @GetMapping
    public List<Film> getAllFilms() {
        log.debug("Список всех фильмов {}", films.values());
        return new ArrayList<>(films.values());
    }
}
