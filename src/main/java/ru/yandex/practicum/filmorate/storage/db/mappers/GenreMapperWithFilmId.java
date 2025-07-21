package ru.yandex.practicum.filmorate.storage.db.mappers;

import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class GenreMapperWithFilmId implements RowMapper<Map<Long, Genre>> {
    @Override
    public Map<Long, Genre> mapRow(ResultSet rs, int rowNum) throws SQLException {
        Map<Long, Genre> genresWithFilmId = new HashMap<>();
        genresWithFilmId.put(
                rs.getLong("film_genres.film_id"),
                new Genre(rs.getLong("genres.id"), rs.getString("genres.name"))
        );

        return genresWithFilmId;
    }
}
