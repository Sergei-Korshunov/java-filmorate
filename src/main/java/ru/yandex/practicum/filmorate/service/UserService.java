package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class UserService {
    @Qualifier("userRepository")
    private final UserStorage userStorage;

    @Autowired
    public UserService(@Qualifier("userRepository") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User addUser(User user) {
        return userStorage.addUser(user);
    }

    public User updateUser(User user) {
        userExists(user.getId());
        return userStorage.updateUser(user);
    }

    private User userExists(long id) {
        Optional<User> user = userStorage.getUserById(id);

        if (user.isPresent()) {
            return user.get();
        }

        throw new NotFoundException(String.format("Пользователь с указанным id - %s не найден", id));
    }

    public User getUserById(long userId) {
        return userExists(userId);
    }

    public List<User> getListUsers() {
        return userStorage.getListUsers();
    }

    public boolean removeUser(long userId) {
        return userStorage.removeUser(userExists(userId).getId());
    }

    public boolean addFriend(long userId, long friendId) {
        getUserById(userId);
        getUserById(friendId);
        return userStorage.addFriend(userId, friendId);
    }

    public List<User> getListFriends(long userId) {
        getUserById(userId);
        return userStorage.getListFriends(userId);
    }

    public List<User> getListCommonFriends(long userId, long userOtherId) {
        getUserById(userId);
        getUserById(userOtherId);
        return userStorage.getListCommonFriends(userId, userOtherId);
    }

    public boolean removeFriend(long userId, long friendId) {
        getUserById(userId);
        getUserById(friendId);
        return userStorage.removeFriend(userId, friendId);
    }
}