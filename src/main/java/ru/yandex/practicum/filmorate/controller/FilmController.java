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
    private long countId = 1;

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film newFilm) {
        newFilm.setId(countId++);
        films.put(countId, newFilm);
        log.info("Добавлен фильм '{}'", newFilm.getName());

        return newFilm;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film updateFilm) {
        if (!films.containsKey(updateFilm.getId()))
            throw new NotFoundException("Фильм с указанным id (" + updateFilm.getId() + ") не найден");

        Film newFilm = films.get(updateFilm.getId());
        newFilm.setName(updateFilm.getName());
        newFilm.setDescription(updateFilm.getDescription());
        newFilm.setReleaseDate(updateFilm.getReleaseDate());
        newFilm.setDuration(updateFilm.getDuration());

        log.info("Обнавлен фильм '{}'", updateFilm.getName());

        return newFilm;
    }

    @GetMapping
    public List<Film> getAllFilms() {
        log.debug("Список всех фильмов {}", films.values());
        return new ArrayList<>(films.values());
    }
}
