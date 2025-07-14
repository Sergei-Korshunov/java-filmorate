package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;

import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
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
    public User getUserById(long id) {
        return users.get(id);
    }

    @Override
    public List<User> getListUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public boolean removeUser(long id) {
        return users.remove(id) != null;
    }
}
