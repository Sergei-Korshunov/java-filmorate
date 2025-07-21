package ru.yandex.practicum.filmorate.storage.db.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import ru.yandex.practicum.filmorate.model.Like;
import ru.yandex.practicum.filmorate.storage.LikeStorage;
import ru.yandex.practicum.filmorate.storage.db.mappers.LikeMapper;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository
public class LikeRepository implements LikeStorage {
    private final JdbcTemplate jdbc;

    @Autowired
    public LikeRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public Set<Long> getLikesByFilmId(long filmId) {
        String queryToGetLikes = "SELECT * FROM likes WHERE film_id = ?";
        List<Like> likes = jdbc.query(queryToGetLikes, new LikeMapper(), filmId);

        Set<Long> userIdHashSet = new HashSet<>();
        likes.forEach(like -> userIdHashSet.add(like.getUserId()));

        return userIdHashSet;
    }
}
