package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
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

        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        films.put(film.getId(), film);

        return film;
    }

    @Override
    public Film getFilmById(long id) {
        return films.get(id);
    }

    @Override
    public List<Film> getPopularFilms(long count) {
        return films.values().stream()
                .sorted(Comparator.comparingLong((Film film) -> film.getLikes().size()).reversed())
                .limit(count)
                .collect(Collectors.toList());
    }

    @Override
    public List<Film> getListFilms() {
        return new ArrayList<>(films.values());
    }

    @Override
    public boolean removeFilm(long id) {
        return films.remove(id) != null;
    }
}
