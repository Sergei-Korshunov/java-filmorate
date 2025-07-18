package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage {
    User addUser(User user);

    User updateUser(User user);

    Optional<User> getUserById(long id);

    List<User> getListUsers();

    boolean removeUser(long id);

    boolean addFriend(long userId, long friendId);

    List<User> getListFriends(long userId);

    List<User> getListCommonFriends(long userId, long userOtherId);

    boolean removeFriend(long userId, long friendId);
}
