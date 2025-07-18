package ru.yandex.practicum.filmorate.storage.db.repository;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import ru.yandex.practicum.filmorate.storage.db.mappers.FriendshipStatusMapper;
import ru.yandex.practicum.filmorate.storage.db.mappers.UserMapper;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

@Repository("userRepository")
@Slf4j
public class UserRepository implements UserStorage {
    private final JdbcTemplate jdbc;
    private final UserMapper userMapper;

    @Autowired
    public UserRepository(JdbcTemplate jdbc, UserMapper userMapper) {
        this.jdbc = jdbc;
        this.userMapper = userMapper;
    }

    @Override
    public User addUser(User user) {
        final String queryToAdd = "INSERT INTO users (email, login, name, birthday) VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbc.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(queryToAdd, new String[]{"id"});
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getLogin());
            stmt.setString(3, user.getName());
            stmt.setDate(4, Date.valueOf(user.getBirthday()));
            return stmt;
        }, keyHolder);
        user.setId(keyHolder.getKey().longValue());

        return user;
    }

    @Override
    public User updateUser(User user) {
        final String queryToUpdate = "UPDATE users SET email = ?, login = ?, name = ?, birthday = ? WHERE id = ?";

        int numberOfEntries = jdbc.update(queryToUpdate,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId());
        if (numberOfEntries == 0)
            throw new NotFoundException(String.format("Пользователь с указанным id - %s не найден", user.getId()));

        return user;
    }

    @Override
    public Optional<User> getUserById(long id) {
        final String queryToGetUserById = "SELECT * FROM users WHERE id = ?";

        return jdbc.query(queryToGetUserById, userMapper, id).stream().findFirst();
    }

    @Override
    public List<User> getListUsers() {
        final String queryToGetListUsers = "SELECT * FROM users";

        return jdbc.query(queryToGetListUsers, userMapper);
    }

    @Override
    public boolean removeUser(long id) {
        final String queryToDelete = "DELETE FROM users WHERE id = ?";
        jdbc.update(queryToDelete, id);

        return false;
    }

    @Override
    public boolean addFriend(long userId, long friendId) {
        String queryToGetNumberRecords = "SELECT COUNT(*) FROM friends WHERE user_id = ? AND friend_id = ?";
        int count = jdbc.queryForObject(queryToGetNumberRecords, Integer.class, friendId, userId);

        if (count > 0) {
            final String queryFriendshipVerification  = "SELECT status FROM friends WHERE user_id = ? AND friend_id = ?";
            FriendshipStatus friendshipStatus =
                    jdbc.queryForObject(queryFriendshipVerification , new FriendshipStatusMapper(), userId, friendId);

            if (friendshipStatus != null) {
                switch (friendshipStatus) {
                    case ACCEPTED -> {
                        log.info("Пользователи {}, {}, уже являются друзьями", userId, friendId);
                        return true;
                    }
                    case NOT_ACCEPTED -> {
                        log.info("Пользователь {} отправил запрос на дружбу с пользователем {}", userId, friendId);
                        return true;
                    }
                }
            }
        }

        final String queryToFriendship = "INSERT INTO friends (user_id, friend_id, status) VALUES (?, ?, ?)";
        int numberOfEntries =
                jdbc.update(queryToFriendship, userId, friendId, FriendshipStatus.NOT_ACCEPTED.getTextView());

        boolean success = numberOfEntries > 0;
        if (success) {
            log.info("Отправлен запрос на дружбу");
        }

        return success;
    }

    @Override
    public List<User> getListFriends(long userId) {
        final String queryToListFriends = "SELECT u.* FROM users AS u " +
                "INNER JOIN friends AS f ON f.friend_id=u.id " +
                "WHERE f.user_id = ?";

        return jdbc.query(queryToListFriends, userMapper, userId);
    }

    @Override
    public List<User> getListCommonFriends(long userId, long userOtherId) {
        String queryToGetListCommonFriends = "SELECT u.* FROM users AS u " +
                "JOIN friends AS f1 ON u.id = f1.friend_id " +
                "JOIN friends AS f2 ON u.id = f2.friend_id " +
                "WHERE f1.user_id = ? AND f2.user_id = ?";

        return jdbc.query(queryToGetListCommonFriends, userMapper, userId, userOtherId);
    }

    @Override
    public boolean removeFriend(long userId, long friendId) {
        final String queryToDeleteFriend = "DELETE FROM friends WHERE user_id = ? AND friend_id = ?";

        return jdbc.update(queryToDeleteFriend, userId, friendId) > 0;
    }
}
