package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User addUser(User user) {
        return userStorage.addUser(user);
    }

    public User updateUser(User user) {
        return userStorage.updateUser(user);
    }

    public User getUserById(long userId) {
        return userStorage.getUserById(userId);
    }

    public List<User> getListUsers() {
        return userStorage.getListUsers();
    }

    public boolean removeUser(long userId) {
        return userStorage.removeUser(userId);
    }

    public boolean addFriend(long userId, long friendId) {
        User user = getUserById(userId);
        User friend = getUserById(friendId);

        return user.getFriends().add(friendId) && friend.getFriends().add(userId);
    }

    public List<User> getListFriends(long userId) {
        return getUserById(userId).getFriends().stream().map(this::getUserById).collect(Collectors.toList());
    }

    public List<User> getListCommonFriends(long userId, long userOtherId) {
        Set<Long> friendUserOne = getUserById(userId).getFriends();
        Set<Long> friendUserTwo = getUserById(userOtherId).getFriends();

        if (friendUserOne.isEmpty() || friendUserTwo.isEmpty())
            return new ArrayList<>();

        return friendUserOne.stream()
                .filter(friendUserTwo::contains)
                .collect(Collectors.toSet()).stream()
                .map(this::getUserById)
                .collect(Collectors.toList());
    }

    public boolean removeFriend(long userId, long friendId) {
        User user = getUserById(userId);
        User friend = getUserById(friendId);

        return user.getFriends().remove(friendId) && friend.getFriends().remove(userId);
    }
}