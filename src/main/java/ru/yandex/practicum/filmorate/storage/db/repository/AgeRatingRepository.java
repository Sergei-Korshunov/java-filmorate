package ru.yandex.practicum.filmorate.storage.db.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.storage.db.mappers.AgeRatingMapper;
import ru.yandex.practicum.filmorate.model.AgeRating;
import ru.yandex.practicum.filmorate.storage.AgeRatingStorage;

import java.util.List;
import java.util.Optional;

@Repository
public class AgeRatingRepository implements AgeRatingStorage {
    private final JdbcTemplate jdbc;
    private final AgeRatingMapper ageRatingMapper;

    @Autowired
    public AgeRatingRepository(JdbcTemplate jdbc, AgeRatingMapper ageRatingMapper) {
        this.jdbc = jdbc;
        this.ageRatingMapper = ageRatingMapper;
    }

    @Override
    public List<AgeRating> getListAgeRatings() {
        String queryToGetListRatings = "SELECT * FROM age_ratings ORDER BY id";

        return jdbc.query(queryToGetListRatings, ageRatingMapper);
    }

    @Override
    public Optional<AgeRating> getAgeRatingById(long id) {
        String queryToGetRatingById = "SELECT * FROM age_ratings WHERE id = ?";

        return jdbc.query(queryToGetRatingById, ageRatingMapper, id)
                .stream()
                .findFirst();
    }
}
