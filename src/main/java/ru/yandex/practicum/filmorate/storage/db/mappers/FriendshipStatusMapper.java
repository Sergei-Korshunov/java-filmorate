package ru.yandex.practicum.filmorate.storage.db.mappers;

import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.storage.db.repository.FriendshipStatus;

import java.sql.ResultSet;
import java.sql.SQLException;

public class FriendshipStatusMapper implements RowMapper<FriendshipStatus> {

    @Override
    public FriendshipStatus mapRow(ResultSet rs, int rowNum) throws SQLException {
        return FriendshipStatus.valueOf(rs.getString("status"));
    }
}
