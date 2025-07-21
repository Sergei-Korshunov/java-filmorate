package ru.yandex.practicum.filmorate.storage.db.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import ru.yandex.practicum.filmorate.model.AgeRating;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class AgeRatingMapper implements RowMapper<AgeRating> {

    @Override
    public AgeRating mapRow(ResultSet rs, int rowNum) throws SQLException {
        AgeRating rating = new AgeRating();
        rating.setId(rs.getLong("id"));
        rating.setName(rs.getString("name"));

        return rating;
    }
}
