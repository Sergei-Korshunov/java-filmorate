package ru.yandex.practicum.filmorate.storage.db.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.db.mappers.GenreMapper;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;
import ru.yandex.practicum.filmorate.storage.db.mappers.GenreMapperWithFilmId;

import java.util.*;

@Repository
public class GenreRepository implements GenreStorage {
    private final JdbcTemplate jdbc;
    private final GenreMapper genreMapper;

    @Autowired
    public GenreRepository(JdbcTemplate jdbc, GenreMapper genreMapper) {
        this.jdbc = jdbc;
        this.genreMapper = genreMapper;
    }

    @Override
    public List<Genre> getListGenres() {
        String queryToGetListGenres = "SELECT * FROM genres ORDER BY id";

        return jdbc.query(queryToGetListGenres, genreMapper);
    }

    @Override
    public Optional<Genre> getGenreById(long id) {
        String queryToGetGenreById = "SELECT * FROM genres WHERE id = ?";

        return jdbc.query(queryToGetGenreById, genreMapper, id)
                .stream()
                .findFirst();
    }

    @Override
    public Set<Genre> getGenresForFilm(Film film) {
        String queryToGetGenresForFilm = "SELECT g.* FROM genres AS g " +
                "JOIN film_genres AS fg ON g.id = fg.genre_id " +
                "WHERE fg.film_id = ? " +
                "ORDER BY g.id";
        List<Genre> genres = jdbc.query(queryToGetGenresForFilm, genreMapper, film.getId());

        return new HashSet<>(genres);
    }

    @Override
    public Map<Long, Set<Genre>> getGenresForAllFilms() {
        String queryToGetGenresForAllFilms = "SELECT fg.film_id, g.id, g.name FROM film_genres AS fg " +
                "JOIN genres g ON fg.genre_id = g.id";
        List<Map<Long, Genre>> listGenresWithFilmId = jdbc.query(queryToGetGenresForAllFilms, new GenreMapperWithFilmId());

        Map<Long, Set<Genre>> genresWithFilmId = new HashMap<>();
        listGenresWithFilmId.forEach(longGenreMap -> {
            for (Map.Entry<Long, Genre> entry : longGenreMap.entrySet()) {
                genresWithFilmId.computeIfAbsent(entry.getKey(), aLong -> new HashSet<>()).add(entry.getValue());
            }
        });

        return genresWithFilmId;
    }
}
