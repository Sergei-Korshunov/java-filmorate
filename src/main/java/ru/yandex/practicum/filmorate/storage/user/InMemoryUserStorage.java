package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;

import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Slf4j
@Component("inMemoryUserStorage")
public class InMemoryUserStorage implements UserStorage {

    private Map<Long, User> users = new HashMap<>();
    private long countId = 0;

    @Override
    public User addUser(User user) {
        user.setId(++countId);
        checkUserForName(user);
        users.put(user.getId(), user);

        return user;
    }

    protected void checkUserForName(User user) {
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
    }

    @Override
    public User updateUser(User user) {
        checkUserForName(user);
        users.put(user.getId(), user);

        return user;
    }

    @Override
    public Optional<User> getUserById(long id) {
        return Optional.of(users.get(id));
    }

    @Override
    public List<User> getListUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public boolean removeUser(long id) {
        return users.remove(id) != null;
    }

    @Override
    public boolean addFriend(long userId, long friendId) {
        return false;
    }

    @Override
    public List<User> getListFriends(long userId) {
        return null;
    }

    @Override
    public List<User> getListCommonFriends(long userId, long userOtherId) {
        return null;
    }

    @Override
    public boolean removeFriend(long userId, long friendId) {
        return false;
    }
}
