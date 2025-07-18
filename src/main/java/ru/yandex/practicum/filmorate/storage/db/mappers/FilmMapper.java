package ru.yandex.practicum.filmorate.storage.db.mappers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.AgeRating;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class FilmMapper implements RowMapper<Film> {
    private final JdbcTemplate jdbc;

    @Autowired
    public FilmMapper(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
        Film film = new Film();
        film.setId(rs.getLong("id"));
        film.setName(rs.getString("name"));
        film.setDescription(rs.getString("description"));
        film.setReleaseDate(rs.getDate("release_date").toLocalDate());
        film.setDuration(rs.getInt("duration"));
        film.setLikes(getLikes(film));
        film.setGenres(getGenres(film));
        film.setMpa(getAgeRating(rs));

        return film;
    }

    private AgeRating getAgeRating(ResultSet rs) throws SQLException {
        String queryToGetRatingById = "SELECT * FROM age_ratings WHERE id = ?";

        return jdbc.queryForObject(queryToGetRatingById, new AgeRatingMapper(), rs.getLong("age_rating_id"));
    }

    private Set<Genre> getGenres(Film film) {
        String queryToGetGenres = "SELECT g.* FROM genres AS g " +
                "JOIN film_genres AS fg ON g.id = fg.genre_id " +
                "WHERE fg.film_id = ? " +
                "ORDER BY g.id";
        List<Genre> genres = jdbc.query(queryToGetGenres, new GenreMapper(), film.getId());

        return new HashSet<>(genres);
    }

    private Set<Long> getLikes(Film film) {
        String queryToGetLikes = "SELECT user_id FROM likes WHERE film_id = ?";
        List<Long> likes = jdbc.queryForList(queryToGetLikes, Long.class, film.getId());

        return new HashSet<>(likes);
    }
}
