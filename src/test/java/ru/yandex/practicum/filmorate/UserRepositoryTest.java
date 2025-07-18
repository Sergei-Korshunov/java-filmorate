package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;

import ru.yandex.practicum.filmorate.storage.db.mappers.UserMapper;
import ru.yandex.practicum.filmorate.storage.db.repository.UserRepository;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({UserRepository.class, UserMapper.class})
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setEmail("e-mail@e.com");
        user.setLogin("Login");
        user.setName("Name");
        user.setBirthday(LocalDate.of(2025, 7, 18));
    }

    @Test
    void addUser() {
        User addUser = userRepository.addUser(user);

        assertThat(addUser.getId()).isNotNull();
        assertThat(addUser.getEmail()).isEqualTo(user.getEmail());
        assertThat(addUser.getLogin()).isEqualTo(user.getLogin());
        assertThat(addUser.getName()).isEqualTo(user.getName());
        assertThat(addUser.getBirthday()).isEqualTo(user.getBirthday());
    }

    @Test
    void updateUser() {
        User addUser = userRepository.addUser(user);
        addUser.setName("Update the name");
        User updatedUser = userRepository.updateUser(addUser);

        assertThat(updatedUser.getName()).isEqualTo("Update the name");
    }

    @Test
    void getListUsers() {
        userRepository.addUser(user);

        assertThat(userRepository.getListUsers()).hasSize(1);
    }

    @Test
    void getUserById() {
        User addUser = userRepository.addUser(user);
        Optional<User> retrievedUser = userRepository.getUserById(addUser.getId());

        assertThat(retrievedUser)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).isEqualTo(addUser)
                );
    }

    @Test
    void removeUser() {
        User addUser = userRepository.addUser(user);
        userRepository.removeUser(addUser.getId());

        assertThat(userRepository.getUserById(addUser.getId())).isEmpty();
    }
}
