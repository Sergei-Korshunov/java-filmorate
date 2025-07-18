package ru.yandex.practicum.filmorate.storage.db.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import ru.yandex.practicum.filmorate.storage.db.mappers.GenreMapper;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.List;
import java.util.Optional;

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
}
