package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Long, Film> films = new HashMap<>();
    private long countId = 0;

    @Override
    public Film addFilm(Film film) {
        film.setId(++countId);
        films.put(film.getId(), film);
        log.info("Добавлен фильм '{}'", film.getName());

        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        if (!films.containsKey(film.getId()))
            throw new NotFoundException(String.format("Фильм с указанным id - %s не найден", film.getId()));

        films.put(film.getId(), film);

        log.info("Обновлен фильм '{}'", film.getName());

        return film;
    }

    @Override
    public Film getFilmById(long id) {
        if (!films.containsKey(id))
            throw new NotFoundException(String.format("Фильм с указанным id - %s не найден", id));

        return films.get(id);
    }

    @Override
    public List<Film> getPopularFilms(long count) {
        List<Film> popularFilms = films.values().stream()
                .sorted(Comparator.comparingLong((Film film) -> film.getLikes().size()).reversed())
                .limit(count)
                .collect(Collectors.toList());
        log.debug("Список популярных фильмов в колличестве {}: {}", count, popularFilms);

        return popularFilms;
    }

    @Override
    public List<Film> getListFilms() {
        log.debug("Список всех фильмов {}", films.values());
        return new ArrayList<>(films.values());
    }

    @Override
    public boolean removeFilm(long id) {
        if (films.containsKey(id)) {
            films.remove(id);
            return true;
        }
        throw new NotFoundException(String.format("Фильм с указанным id - %s не найден", id));
    }
}
