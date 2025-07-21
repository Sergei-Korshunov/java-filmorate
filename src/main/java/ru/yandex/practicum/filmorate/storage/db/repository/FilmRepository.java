package ru.yandex.practicum.filmorate.storage.db.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import ru.yandex.practicum.filmorate.storage.db.mappers.FilmMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.*;

@Repository("filmRepository")
public class FilmRepository implements FilmStorage {
    private final JdbcTemplate jdbc;
    private final FilmMapper filmMapper;

    @Autowired
    public FilmRepository(JdbcTemplate jdbc, FilmMapper filmMapper) {
        this.jdbc = jdbc;
        this.filmMapper = filmMapper;
    }

    @Override
    public Film addFilm(Film film) {
        final String queryToAdd = "INSERT INTO films (name, description, release_date, duration, age_rating_id) " +
                "VALUES (?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbc.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(queryToAdd, new String[]{"id"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setDate(3, Date.valueOf(film.getReleaseDate()));
            stmt.setInt(4, film.getDuration());
            stmt.setLong(5, film.getMpa().getId());
            return stmt;
        }, keyHolder);
        film.setId(keyHolder.getKey().longValue());
        addFilmGenres(film);

        return film;
    }

    private void addFilmGenres(Film film) {
        if (film.getGenres() != null && !film.getGenres().isEmpty()) {
            String queryToInsertGenres = "INSERT INTO film_genres (film_id, genre_id) VALUES (?, ?)";
            for (Genre genre : film.getGenres()) {
                jdbc.update(queryToInsertGenres, film.getId(), genre.getId());
            }
        }
    }

    @Override
    public Film updateFilm(Film film) {
        final String queryToUpdate = "UPDATE films SET name = ?, description = ?, release_date = ?, " +
                "duration = ?, age_rating_id = ? WHERE id = ?";

        jdbc.update(queryToUpdate,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId());

        removeGenres(film.getId());
        addFilmGenres(film);

        return film;
    }

    private boolean removeGenres(long filmId) {
        final String queryToDeleteGenres = "DELETE FROM film_genres WHERE film_id = ?";

        return jdbc.update(queryToDeleteGenres, filmId) > 0;
    }

    @Override
    public Optional<Film> getFilmById(long id) {
        String queryToGetFilmById = "SELECT f.*, ar.id, ar.name FROM films AS f " +
                "LEFT JOIN age_ratings AS ar ON f.age_rating_id = ar.id " +
                "WHERE f.id = ?";

        return jdbc.query(queryToGetFilmById, filmMapper, id)
                .stream()
                .findFirst();
    }

    @Override
    public List<Film> getPopularFilms(long count) {
        String queryToGetPopularFilms = "SELECT f.*, ar.id AS ar_id, ar.name AS ar_name, " +
                "COUNT(l.user_id) AS likes_count " +
                "FROM films AS f " +
                "LEFT JOIN age_ratings AS ar ON f.age_rating_id = ar.id " +
                "LEFT JOIN likes AS l ON f.id = l.film_id " +
                "GROUP BY f.id, ar_id, ar_name " +
                "ORDER BY likes_count DESC " +
                "LIMIT ?";

        return jdbc.query(queryToGetPopularFilms, filmMapper, count);
    }

    @Override
    public List<Film> getListFilms() {
        String queryToGetListFilms = "SELECT f.*, ar.id, ar.name FROM films AS f " +
                "LEFT JOIN age_ratings AS ar ON f.age_rating_id = ar.id";

        return jdbc.query(queryToGetListFilms, filmMapper);
    }

    @Override
    public boolean removeFilm(long id) {
        final String queryToDelete = "DELETE FROM films WHERE id = ?";

        return jdbc.update(queryToDelete, id) > 0;
    }

    @Override
    public boolean addLike(long filmId, long userId) {
        String queryToAddLike = "INSERT INTO likes (film_id, user_id) VALUES (?, ?)";

        return jdbc.update(queryToAddLike, filmId, userId) > 0;
    }

    @Override
    public boolean removeLike(long filmId, long userId) {
        String queryToRemoveLike = "DELETE FROM likes WHERE film_id = ? AND user_id = ?";

        return jdbc.update(queryToRemoveLike, filmId, userId) > 0;
    }
}
